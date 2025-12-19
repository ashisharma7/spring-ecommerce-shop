# Inventory Service Architecture

## 1. Goal
Manages stock levels and ensures atomic stock reservations using Optimistic Locking.

## 2. Kafka Event Strategy

### 📥 Consumed Events (What it hears)
| Event Name | Source | Action Taken |
| :--- | :--- | :--- |
| `OrderCreatedEvent` | Order Service | Attempt to reserve stock. If success -> Emit `Reserved`. If fail -> Emit `Failed`. |
| `OrderCancelledEvent` | Order Service | **Compensating Transaction:** Release previously reserved stock back to the pool. |
| `PaymentFailedEvent` | Payment Service | **Compensating Transaction:** Release stock (since the user didn't pay). |

### 📤 Produced Events (What it says)
| Event Name | Trigger | Payload | Destination |
| :--- | :--- | :--- | :--- |
| `InventoryReservedEvent` | Stock successfully locked | `orderId` | Payment Service, Order Service |
| `StockReservationFailedEvent` | Not enough stock | `orderId`, `reason` | Order Service |

## 3. Domain Model

### Inventory Entity
This entity tracks the physical stock count. It uses **Optimistic Locking** to handle high concurrency (preventing "lost updates").

| Field | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PK, Not Null | Unique Inventory Record ID |
| `product_id` | String | Unique, Not Null | Reference to Catalog Product ID |
| `quantity` | Integer | Not Null, >= 0 | Actual physical stock available |
| `reserved` | Integer | Default 0 | Stock "on hold" for pending orders |
| `version` | Long | Default 0 | **Optimistic Lock** version number |
| `updated_at` | Timestamp | Default `now()` | Audit timestamp |

> **Business Rule:** `quantity - reserved` must always be `>= 0`.

## 4. API Endpoints

### Public / System
- `GET /api/inventory/{productId}` - Check stock for a single item.

### Admin Only (`ROLE_ADMIN`)
- `POST /api/inventory/add` - Manually add stock.
- `POST /api/inventory/audit` - Manually correct stock count.

### System Only (Internal Sagas)
- `POST /api/inventory/check` - Triggered by Cart/Order Service.
    - **Purpose:** Bulk stock check for Cart validation.
    - **Payload:** List of Product IDs `["id-1", "id-2"]`.
    - **Response:** Map of ID to Quantity `{"id-1": 10, "id-2": 0}`.
    - **Type:** Synchronous (REST).

## 5. Data Storage
- **Primary:** PostgreSQL (`inventory_db`)
- **Tables:** (`inventory`)