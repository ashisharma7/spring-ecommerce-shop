# ADR-008: Custom PostgreSQL Port for Local Development

## Status
Accepted

## Context
During local development on Windows, multiple PostgreSQL instances
(Docker-based and host-installed) may attempt to bind to the default
port 5432, leading to non-deterministic connection behavior.

This caused authentication failures where the application connected
to an unintended PostgreSQL instance.

## Decision
We decided to expose the Dockerized PostgreSQL instance on a non-default
host port (5433) while keeping the container port unchanged (5432).

Application configuration explicitly targets this custom port.

## Alternatives Considered
- Stopping host-installed PostgreSQL  
  Rejected due to potential impact on other projects.
- Relying on Docker DNS only  
  Not applicable since the application runs on the host.

## Consequences
- Eliminates port collision issues.
- Makes the target database explicit.
- Improves reliability of local development on Windows.
