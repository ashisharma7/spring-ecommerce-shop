# Global Event Registry

## 1. Event Flow (The Saga Choreography)
The system uses a **Choreography-based Saga** for Order Fulfillment. There is no central "controller" telling everyone what to do step-by-step; instead, services react to events like dominoes.

## 2. Topic & Event Definitions

| Topic Name | Event Name | Producer Service | Consumer Service | Payload (Key Fields) |
| :--- | :--- | :--- | :--- | :--- |
| `order-events` | `OrderCreatedEvent` | **Order** | Inventory, Cart | `orderId`, `itemsList`, `userId` |
| `order-events` | `OrderCancelledEvent` | **Order** | Inventory | `orderId`, `reason` |
| `inventory-events` | `InventoryReservedEvent` | **Inventory** | Payment | `orderId`, `totalAmount` |
| `inventory-events` | `StockReservationFailedEvent` | **Inventory** | Order | `orderId`, `reason` |
| `payment-events` | `PaymentSuccessEvent` | **Payment** | Order | `orderId`, `paymentId` |
| `payment-events` | `PaymentFailedEvent` | **Payment** | Order, Inventory | `orderId`, `reason` |

## 3. Failure & Rollback Scenarios
- **If Stock Fails:** Inventory emits `StockReservationFailed` -> Order Service sets status to `CANCELLED`.
- **If Payment Fails:** Payment emits `PaymentFailed` -> Order Service sets status `CANCELLED` -> Inventory Service listens and **Releases Stock**.