# ADR-001: Database Schema Management Strategy

## Status
Accepted

## Context
Hibernate provides automatic schema generation via `ddl-auto` options
such as `update` and `create`. While convenient, these approaches can
lead to non-deterministic schema changes and hidden production risks.

## Decision
We decided to disable Hibernate-driven schema generation
(`ddl-auto: validate`) and manage database schema evolution using
Flyway versioned SQL migrations.

## Alternatives Considered
- `ddl-auto: update`  
  Rejected due to non-deterministic behavior and lack of auditability.
- Manual SQL execution  
  Rejected due to lack of version tracking and repeatability.

## Consequences
- Schema changes must be explicit and versioned.
- Initial setup requires writing migration scripts.
- Startup will fail fast if schema and entities diverge.

This decision improves reliability, repeatability, and production safety.
