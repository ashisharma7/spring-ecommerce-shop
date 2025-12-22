# ADR-010: Schema-Level Privileges for Service Database Users

## Status
Accepted

## Context
Each service uses a dedicated database and database user.
While database-level privileges were granted, Flyway migrations
failed due to missing schema-level permissions.

PostgreSQL separates database privileges from schema privileges.

## Decision
We decided to explicitly grant USAGE and CREATE privileges on the
`public` schema to each service-specific database user.

Default privileges are also configured to allow future object access.

## Alternatives Considered
- Using a shared superuser for all services  
  Rejected due to poor isolation and security practices.
- Running Flyway as a separate admin user  
  Rejected to keep ownership aligned with service responsibility.

## Consequences
- Flyway migrations can create and manage tables.
- Each service fully owns its database schema.
- Least-privilege access is preserved.
