# High-Level System Architecture

## 1. System Overview
The **E-Commerce Shop** is a cloud-native, distributed system designed to handle high-concurrency retail operations. It follows the **Database-per-Service** pattern and uses **Event-Driven Architecture (EDA)** for asynchronous communication between domains.

### Key Architectural Patterns
- **Microservices:** Loosely coupled services based on Business Domains (DDD).
- **Event-Driven:** Uses Apache Kafka for eventual consistency and decoupling.
- **Saga Pattern:** Choreography-based distributed transactions (Order -> Inventory -> Payment).
- **CQRS (Lite):** Separation of heavy reads (Catalog/Redis) from writes (Postgres).
- **API Gateway:** Centralized entry point for routing, rate limiting, and auth.

---

## 2. Event-Driven Backbone (Kafka)
The system relies on a **Choreography Saga** to process orders without direct HTTP coupling between services.

### The "Order Fulfillment" Saga
1. **User** places order -> **Order Service** (Status: `CREATED`)
    - *Event:* `OrderCreatedEvent`
2. **Inventory Service** consumes event -> Reserves Stock
    - *Success:* Emits `InventoryReservedEvent`
    - *Fail:* Emits `StockReservationFailedEvent` (Compensating Transaction)
3. **Payment Service** consumes event -> Charges User
    - *Success:* Emits `PaymentSuccessEvent`
    - *Fail:* Emits `PaymentFailedEvent` (Compensating Transaction)
4. **Order Service** consumes final events
    - *Success:* Updates status to `CONFIRMED`
    - *Fail:* Updates status to `CANCELLED`

### Other Event Flows
- **Cart Cleanup:** Cart Service listens to `OrderCreatedEvent` to delete the user's cart items after purchase.

---

## 3. Security Architecture (Identity)
We use **Keycloak** as the centralized Identity Provider. Services do not manage passwords.

### Authentication Flow (OAuth2 / OIDC)
1. **Frontend** redirects user to Keycloak Login Page.
2. User authenticates; Keycloak returns an **Access Token (JWT)**.
3. **Frontend** attaches `Authorization: Bearer <token>` to every API request.
4. **API Gateway** validates the JWT signature (using JWK Set URI).
5. **Microservices** trust the validated token passed by the Gateway and extract User ID/Roles.

### User Roles (RBAC)
The system enforces permissions based on Keycloak Realm Roles found in the JWT.

| Role | Scope | Description |
| :--- | :--- | :--- |
| `ROLE_USER` | **Customer** | Can buy products, manage personal cart, view own orders. |
| `ROLE_ADMIN` | **Staff** | Can create products, adjust inventory, view all global orders, cancel any order. |

---

## 4. Infrastructure Strategy (Local-First)
The entire stack runs locally using **Docker Compose**, mimicking a production Kubernetes environment.

### Core Infrastructure
- **PostgreSQL 16:** Primary persistence. Each service gets a dedicated schema/database.
- **Redis:** Used by Cart Service and as a Level-2 Cache for Catalog.
- **Apache Kafka:** The central nervous system for messaging.
- **Zipkin (Future):** For distributed tracing.
- **Prometheus + Grafana (Future):** For monitoring.