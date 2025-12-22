# ADR-002: Domain Model Contains Business Behavior

## Status
Accepted

## Context
A common approach in Spring applications is the anemic domain model,
where entities are simple data holders and all business logic resides
in service classes. This often leads to duplicated rules and invalid
state transitions.

## Decision
We decided that domain entities (e.g., Order) may contain business
behavior such as state transitions and invariant enforcement.

Entities must not:
- Call repositories
- Publish events
- Access external services

They may:
- Enforce valid state transitions
- Protect invariants
- Express domain language

## Alternatives Considered
- Anemic domain model  
  Rejected due to poor encapsulation and rule duplication.

## Consequences
- Business rules are centralized and protected.
- Entities remain dependency-free.
- Service layer focuses on orchestration, not validation.
