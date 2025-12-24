# ADR-012: RestClient for Synchronous Catalog Communication

## Status
Accepted

## Context
The Order Service requires synchronous communication with the Catalog Service
to validate products and fetch pricing snapshots during order creation.

While Spring Cloud OpenFeign is a popular declarative choice for this pattern,
it introduces significant abstraction ("magic"), requires additional dependencies,
and can hide underlying HTTP client configuration details like timeouts and
error handling.

We need a solution that offers:
- Fine-grained control over connection and read timeouts
- Explicit error handling for 4xx/5xx responses
- Minimal additional dependencies
- A modern, fluent API (avoiding the legacy `RestTemplate`)

## Decision
We decided to use Spring Framework's native `RestClient` for synchronous
service-to-service communication.

In `HttpCatalogClient`, we explicitly configure a `SimpleClientHttpRequestFactory`
to enforce strict timeouts (connect: 1000ms, read: 2000ms). This ensures that
network issues in the Catalog Service do not cascade and indefinitely hang
the Order Service.

## Alternatives Considered
- **Spring Cloud OpenFeign**
  Rejected to avoid "magic" and implicit behavior. We prefer seeing explicit
  HTTP calls and configuration during this phase of development.
- **RestTemplate**
  Rejected as it is in maintenance mode and offers a less fluent API.
- **WebClient**
  Rejected to avoid pulling in the reactive stack (`spring-boot-starter-webflux`)
  into a blocking, servlet-based application.

## Consequences
- We have full programmatic control over the HTTP request/response cycle.
- Timeout configurations are explicit and visible in the code.
- Dependencies are kept lightweight (no need for `spring-cloud-starter-openfeign`).
- Boilerplate is slightly higher compared to Feign, but readability and debuggability are improved.