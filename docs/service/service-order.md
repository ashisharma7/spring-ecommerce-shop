# Order Service Architecture

## 1. Goal
The **Saga Initiator and Source of Truth** for the Order lifecycle.

The Order Service owns the Order state and reacts to domain events emitted by other services.
It does **not** issue commands to other services; instead, it participates in a **choreography-based saga**
by publishing and consuming business events.

---

## 2. Kafka Event Strategy

### 📤 Produced Events (What it says)

| Event Name | Trigger | Payload | Destination |
| :--- | :--- | :--- | :--- |
| `OrderCreatedEvent` | User places new order | `orderId`, `userId`, `itemsList`, `totalAmount` | **Inventory Service** (Reserve Stock) |
| `OrderCancelledEvent` | Payment fails or user cancellation | `orderId`, `reason` | **Inventory Service** (Release Stock) |

> **Note:** Cart is cleared **synchronously** during order placement and does not participate in the saga.

---

### 📥 Consumed Events (What it hears)

| Event Name | Source | Action Taken |
| :--- | :--- | :--- |
| `InventoryReservedEvent` | Inventory Service | Update status to `PENDING_PAYMENT` |
| `StockReservationFailedEvent` | Inventory Service | Mark order as `CANCELLED` |
| `PaymentSuccessEvent` | Payment Service | Mark order as `CONFIRMED` |
| `PaymentFailedEvent` | Payment Service | Mark order as `CANCELLED` and emit `OrderCancelledEvent` |

---

## 3. Domain Model

### Order Entity (Aggregate Root)
Represents a checkout transaction and the single source of truth for order state.

| Field | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PK, Not Null | Unique Order ID |
| `order_number` | String | Unique, Not Null | Human-readable order number |
| `user_id` | String | Not Null | Keycloak User ID |
| `status` | Enum | Not Null | `CREATED`, `PENDING_PAYMENT`, `CONFIRMED`, `CANCELLED` |
| `total_amount` | BigDecimal | Not Null | Final transaction value |
| `created_at` | Timestamp | Default `now()` | Order creation time |

---

### OrderItem Entity (Child)
A snapshot of product data at the time of purchase.
This ensures historical correctness even if catalog data changes later.

| Field | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PK, Not Null | Unique item ID |
| `order_id` | UUID | FK, Not Null | Parent Order |
| `product_id` | String | Not Null | Catalog product reference |
| `product_name` | String | Not Null | Snapshot of name |
| `price` | BigDecimal | Not Null | Snapshot of unit price |
| `quantity` | Integer | Not Null | Quantity purchased |

---

## 4. API Endpoints

### User Endpoints (`ROLE_USER`)
- `POST /api/orders` — Place a new order (clears Cart synchronously).
- `GET /api/orders` — Retrieve **my** order history.
- `GET /api/orders/{id}` — Retrieve details of a specific order.

### Admin Endpoints (`ROLE_ADMIN`)
- `GET /api/admin/orders` — View all orders.
- `PUT /api/admin/orders/{id}/status` — Force update order status (manual intervention).

---

## 5. Order State Machine

| Current State | Event | Next State |
|-------------|------|------------|
| `CREATED` | `InventoryReservedEvent` | `PENDING_PAYMENT` |
| `CREATED` | `StockReservationFailedEvent` | `CANCELLED` |
| `PENDING_PAYMENT` | `PaymentSuccessEvent` | `CONFIRMED` |
| `PENDING_PAYMENT` | `PaymentFailedEvent` | `CANCELLED` |

### State Rules
- State transitions are **idempotent**
- Duplicate events are safely ignored
- A `CONFIRMED` or `CANCELLED` order is terminal

---

## 6. Data Storage
- **Primary Database:** PostgreSQL (`order_db`)
- **Tables:** (`orders`, `order_items`)
---

## 7. Failure Handling

### Inventory Failure
- Inventory emits `StockReservationFailedEvent`
- Order transitions to `CANCELLED`
- Saga ends

### Payment Failure
- Payment emits `PaymentFailedEvent`
- Order transitions to `CANCELLED`
- Order emits `OrderCancelledEvent`
- Inventory releases reserved stock

---

## 8. Non-Responsibilities

The Order Service does **not**:
- Validate inventory availability synchronously
- Process payments
- Manage cart state asynchronously
- Access other service databases

All such responsibilities belong to their respective services.

---

## 9. Summary

- Order Service is the **Saga Initiator and State Authority**
- Communication is **event-driven**
- Business invariants are enforced via state transitions
- The design favors decoupling, resilience, and correctness
