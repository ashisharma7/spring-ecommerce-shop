# Order Service Architecture

## 1. Goal
The central Orchestrator. It manages the Order Lifecycle and drives the Saga.

## 2. Kafka Event Strategy

### 📤 Produced Events (What it says)
| Event Name | Trigger | Payload | Destination |
| :--- | :--- | :--- | :--- |
| `OrderCreatedEvent` | User places new order | `orderId`, `userId`, `itemsList`, `totalAmount` | **1. Inventory Service** (Reserve Stock)<br>**2. Cart Service** (Clear Items) |
| `OrderCancelledEvent` | Payment fails or User cancels | `orderId`, `reason` | **Inventory Service** (Release stock) |

### 📥 Consumed Events (What it hears)
| Event Name | Source | Action Taken |
| :--- | :--- | :--- |
| `InventoryReservedEvent` | Inventory Service | Update status to `PENDING_PAYMENT` (or auto-trigger payment). |
| `StockReservationFailedEvent` | Inventory Service | Mark order as `CANCELLED`. Notify user. |
| `PaymentSuccessEvent` | Payment Service | Mark order as `CONFIRMED`. Ready for shipping. |
| `PaymentFailedEvent` | Payment Service | Mark order as `CANCELLED`. Emit `OrderCancelledEvent`. |

## 3. Domain Model

### Order Entity (Root)
Represents the checkout transaction.

| Field | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PK, Not Null | Unique Order ID |
| `order_number` | String | Unique, Not Null | Human-readable ID (e.g., "ORD-2025-101") |
| `user_id` | String | Not Null | Keycloak User ID |
| `status` | Enum | Not Null | `CREATED`, `PENDING_PAYMENT`, `CONFIRMED`, `CANCELLED` |
| `total_amount` | BigDecimal | Not Null | Final transaction value |
| `created_at` | Timestamp | Default `now()` | Order placement time |

### OrderItem Entity (Child)
A snapshot of the product at the time of purchase. Even if the product price changes later, this record remains historic.

| Field | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PK, Not Null | Unique Item ID |
| `order_id` | UUID | FK, Not Null | Link to parent `Order` |
| `product_id` | String | Not Null | Reference to Catalog Product |
| `product_name`| String | Not Null | **Snapshot** of name |
| `price` | BigDecimal | Not Null | **Snapshot** of unit price |
| `quantity` | Integer | Not Null | Quantity purchased |

## 4. API Endpoints

### User Endpoints (`ROLE_USER`)
- `POST /api/orders` - Place a new order.
- `GET /api/orders` - Get **my** order history.
- `GET /api/orders/{id}` - Get details of **my** specific order.

### Admin Endpoints (`ROLE_ADMIN`)
- `GET /api/admin/orders` - View **all** orders across the system.
- `PUT /api/admin/orders/{id}/status` - Force update status.

## 5. Data Storage
- **Primary:** PostgreSQL (`order_db`), Tables (`orders`, `order_item`)

## 6. External Integrations

### Inventory Service (Pre-Check)
Before emitting the `OrderCreatedEvent`, the Order Service MAY perform a quick synchronous check to fail fast.
- **Endpoint Called:** `POST http://inventory-service/api/inventory/check`
- **Logic:** If stock is 0, reject request immediately with `400 Bad Request`. Do not start Saga.