# ADR-011: Enforced UTC Timezone Across the Application

## Status
Accepted

## Context
Relying on JVM or OS default timezones caused inconsistent behavior
and startup failures due to unsupported timezone identifiers
(e.g., Asia/Calcutta) in PostgreSQL.

Distributed systems require consistent time representation.

## Decision
We decided to enforce UTC at all critical layers:
- Application startup
- Hibernate JDBC configuration
- Jackson serialization

The application does not rely on OS or developer machine defaults.

## Alternatives Considered
- Using JVM default timezone only  
  Rejected due to non-determinism across environments.
- Using local timezones (e.g., IST)  
  Rejected due to portability and DST risks.

## Consequences
- All persisted and serialized timestamps are in UTC.
- Application behavior is consistent across machines.
- Timezone-related startup failures are avoided.
