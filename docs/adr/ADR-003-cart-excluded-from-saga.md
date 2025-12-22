# ADR-003: Cart Service Excluded from Saga Participation

## Status
Accepted

## Context
The checkout flow includes both business-critical state (orders,
inventory, payments) and UI/session state (cart). Including the Cart
Service in the saga would require idempotency, compensation logic,
and increase failure coupling.

## Decision
We decided to exclude Cart Service from saga participation.

Cart is cleared synchronously during order placement.
Cart does not publish or consume Kafka events in v1.

## Alternatives Considered
- Event-driven cart clearing  
  Rejected due to unnecessary saga complexity and eventual consistency
  issues for UI state.

## Consequences
- Simpler saga flow
- Cart remains UI-focused
- Reduced failure propagation
