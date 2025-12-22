# ADR-004: Lazy Loading Strategy in JPA

## Status
Accepted

## Context
JPA relationships default to eager loading in some cases, which can
cause performance issues such as N+1 queries and excessive data loading.

## Decision
We decided to:
- Use `FetchType.LAZY` for collections and associations by default
- Control data fetching explicitly in service layer queries

## Alternatives Considered
- Eager loading everywhere  
  Rejected due to performance risks and lack of control.

## Consequences
- Better performance predictability
- Clear ownership of data loading decisions
- Slightly more explicit query design
