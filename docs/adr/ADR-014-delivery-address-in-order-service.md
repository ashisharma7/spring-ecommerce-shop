# ADR-014: Include Delivery Address as Value Object in Order Service

## Status
Accepted

## Context
An Order represents a commitment to purchase goods. For physical goods, a destination
address is a fundamental part of this contract.

Currently, the `Order` entity lacks shipping information. We do not yet have a dedicated
**Shipping Service**. We need to decide where and how to store this address data to:
1. Support the frontend checkout flow.
2. Maintain a complete historical record of the order.
3. Enable future integration with logistics providers.

## Decision
We will model the Delivery Address as a **Value Object** within the **Order Service** domain
and persist it directly in the `orders` table.

### Implementation Details:
1.  **Value Object:** Create an immutable `Address` class (Street, City, State, Zip, Country).
    It has no identity of its own.
2.  **Embedding:** Use JPA `@Embedded` to include `Address` within the `Order` entity.
3.  **Persistence:** Use `@AttributeOverrides` on the `Order` entity to map the address fields
    to specific columns (e.g., `shipping_street`, `shipping_city`).
4.  **API Contract:** The `CreateOrderRequest` must include a valid delivery address.

## Alternatives Considered

### 1. Separate Address Table (`OneToOne`)
* **Pros:** Normalization; saves space if users reuse exact addresses.
* **Cons:** Requires joins to fetch order details. Addresses in e-commerce should generally
  be historical snapshots (if a user moves, old orders shouldn't change).
* **Verdict:** Rejected. Snapshotting is safer and performance is better with a flat table.

### 2. Separate Shipping Service
* **Pros:** strict separation of concerns.
* **Cons:** Over-engineering for the current stage. The "Order" aggregate is incomplete
  without a destination.
* **Verdict:** Rejected for now. We can extract this later if logic becomes complex.

## Consequences
* **Schema Change:** The `orders` table will expand by 5 columns (`shipping_street`, etc.).
* **API Breaking Change:** Clients must now provide `deliveryAddress` in the POST request.
* **Simplicity:** No extra database joins are required to view an order's destination.
* **Data Integrity:** Old orders preserve the address they were shipped to, even if the user updates their profile later.