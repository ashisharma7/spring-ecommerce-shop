# ADR-007: Docker Volumes for Database Persistence

## Status
Accepted

## Context
Containers are ephemeral by design. Without explicit data persistence,
database contents are lost whenever containers are stopped, recreated,
or removed.

Losing database state during development causes:
- repeated schema recreation
- loss of test data
- broken local workflows

## Decision
We decided to use Docker named volumes to persist PostgreSQL data
outside the container lifecycle.

The Postgres data directory (`/var/lib/postgresql/data`) is mapped to
a Docker-managed named volume.

## Alternatives Considered
- No volume (ephemeral database)  
  Rejected due to data loss on container restart.
- Bind-mounting a local filesystem directory  
  Rejected due to portability and OS-specific path issues.

## Consequences
- Database data survives container restarts and rebuilds.
- Developers can safely stop/start Docker without losing state.
- The setup remains portable across machines and environments.
