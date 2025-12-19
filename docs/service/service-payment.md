# Payment Service Architecture

## 1. Goal
To simulate a third-party payment gateway (like Stripe or Razorpay). This is a **Mock Provider** designed to fail randomly (80% Success / 20% Fail) to test distributed transactions.

## 2. Event Strategy
- **Consumes:** `InventoryReservedEvent` (Only pays if stock is secured).
- **Produces:** `PaymentSuccessEvent` OR `PaymentFailedEvent`.

## 3. Domain Model

### Payment Entity
Stores the transaction attempt result.

| Field | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PK, Not Null | Unique Payment ID |
| `order_id` | UUID | FK, Not Null | Link to the Order being paid for |
| `amount` | BigDecimal | Not Null | Amount charged |
| `status` | Enum | Not Null | `SUCCESS`, `FAILED` |
| `reference_number` | String | Nullable | Mock Bank Transaction ID |
| `created_at` | Timestamp | Default `now()` | Audit timestamp |

## 4. API Endpoints
- **Internal Only:** `POST /api/payments/process` (Attempt to charge).

## 5. Data Storage
- **Primary:** PostgreSQL (`payment_db`), Tables (`payments`)
