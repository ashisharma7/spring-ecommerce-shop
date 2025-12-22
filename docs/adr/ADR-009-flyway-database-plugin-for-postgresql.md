# ADR-009: Flyway Database Plugin for PostgreSQL 16

## Status
Accepted

## Context
Flyway core no longer bundles full support for all databases.
Support for newer PostgreSQL versions (16.x) is provided via
a dedicated database plugin module.

Using only Flyway core resulted in:
"Unsupported Database: PostgreSQL 16.x"

## Decision
We decided to explicitly include the PostgreSQL-specific Flyway
database plugin (`flyway-database-postgresql`) alongside
Spring Boot's Flyway starter.

## Alternatives Considered
- Downgrading PostgreSQL  
  Rejected to avoid unnecessary infra constraints.
- Disabling Flyway database validation  
  Rejected due to safety concerns.

## Consequences
- Enables Flyway support for PostgreSQL 16.x.
- Keeps schema migrations safe and version-aware.
- Aligns with Flyway's modular architecture.
