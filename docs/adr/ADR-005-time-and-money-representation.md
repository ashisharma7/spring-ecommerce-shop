# ADR-005: Time and Money Representation

## Status
Accepted

## Context
Incorrect handling of time and monetary values is a common source
of bugs in distributed systems.

## Decision
- Use `BigDecimal` for all monetary values
- Use `Instant` for all timestamps in domain models
- Store timestamps in UTC
- Serialize dates as ISO-8601 strings in APIs

## Alternatives Considered
- `double` for money  
  Rejected due to precision errors.
- `java.sql.Timestamp`  
  Rejected due to legacy design and timezone ambiguity.

## Consequences
- Financial calculations are precise and safe
- Time handling is consistent across services
- APIs are predictable and frontend-friendly
