# ADR-006: Postgres Initialization Script and Service-Specific DB Users

## Status
Accepted

## Context
The project uses a single PostgreSQL instance for local development.
Multiple services (Order, Inventory, Payment, Catalog) require their own
databases and logical isolation.

Manual database creation leads to:
- non-reproducible setups
- environment drift
- undocumented assumptions

Additionally, using a single superuser for all services hides
data-access boundaries and weakens security discipline.

## Decision
We decided to use a Postgres initialization SQL script (`init.sql`)
mounted into the container via Docker Compose.

The script:
- creates one database per service
- creates a dedicated database user per service
- grants each user access only to its own database

This initialization runs automatically on first container startup.

## Alternatives Considered
- Manual database creation via psql or GUI  
  Rejected due to lack of reproducibility and automation.
- One shared database and user for all services  
  Rejected due to poor isolation and unclear ownership.

## Consequences
- Database setup is fully reproducible and automated.
- Each service has clear database ownership.
- Local development mirrors production isolation principles.
- Slight upfront SQL effort is required, but avoids future ambiguity.
