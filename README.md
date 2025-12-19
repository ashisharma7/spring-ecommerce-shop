# E-Commerce Shop

## 🎯 Project Overview
A cloud-native, distributed e-commerce system built to demonstrate **Event-Driven Architecture**, **Microservices Patterns**, and **High-Performance Java**.

This project mimics a real-world enterprise environment, focusing on:
- **Decoupled Architecture:** Independent services communicating via Apache Kafka.
- **Distributed Transactions:** Handling failures using the **Saga Pattern** (Choreography).
- **Scalability:** High-speed caching (Redis) and asynchronous processing.
- **Security:** Role-Based Access Control (RBAC) using **Keycloak**.

---

## 🏗️ High-Level Architecture
The system follows the **Database-per-Service** pattern. Each service owns its data and communicates asynchronously for write operations.

### 🔌 Service Registry & Ports

| Service                    | Port | Database | Responsibilities |
|:---------------------------| :--- | :--- | :--- |
| **API Gateway**            | `8080` | - | Routing, Rate Limiting, JWT Validation. |
| **Catalog Service**        | `8081` | PostgreSQL | Product Management (Source of Truth). |
| **Cart Service**           | `8082` | Redis | Temporary User Sessions (High-speed write). |
| **Order Service**          | `8083` | PostgreSQL | **Saga Orchestrator**, Order Lifecycle. |
| **Inventory Service**      | `8084` | PostgreSQL | Stock Management, Optimistic Locking. |
| **Payment Service**        | `8085` | PostgreSQL | **Mock** Provider (Simulates Stripe/Razorpay). |
| **Keycloak**               | `8180` | PostgreSQL | Identity Provider (Auth Server). |
| **PostgreSQL**             | `5432` | DB | Primary Database (All Schemas). |
| **Redis**                  | `6379` | Cache | Cart Data & Caching. |
| **Kafka**                  | `9092` | Broker | Event Streaming. |
| **Zipkin(Planned)**        | `9411` | Tool | Distributed Tracing UI. |

---

## 🛡️ Security & Roles
Authentication is handled by **Keycloak** (OAuth2 / OIDC). Services do not handle passwords.

| Role | Scope | Permissions |
| :--- | :--- | :--- |
| **`ROLE_USER`** | Customer | Place orders, manage cart, view personal history. |
| **`ROLE_ADMIN`** | Staff | Add products, adjust inventory, view all global orders. |

---

## 🚀 Tech Stack

### Backend & Core
* **Language:** Java 21
* **Framework:** Spring Boot 3.3
* **Build Tool:** Maven

### Infrastructure (Local Cloud)
* **Orchestration:** Docker Compose
* **Messaging:** Apache Kafka
* **Databases:** PostgreSQL 16, Redis
* **Identity:** Keycloak

### Observability (Planned)
* **Tracing:** Zipkin
* **Monitoring:** Prometheus & Grafana

### Frontend
* **Framework:** React + Vite
* **Styling:** Tailwind CSS/Bootstrap

---

## 📖 Documentation
Detailed architectural decisions are documented in the `docs/` directory.

### System Design
* [**High-Level Architecture**](docs/architecture.md) - The Master Blueprint.
* [**Global Event Registry**](docs/events.md) - Kafka Topics & Event Flow.
* [**API Gateway & Routing**](docs/service-gateway.md) - Edge Server specs.

### Service Contracts
* [**Catalog Service**](docs/service-catalog.md) - Product domain.
* [**Order Service**](docs/service-order.md) - Saga orchestration.
* [**Inventory Service**](docs/service-inventory.md) - Stock & Concurrency.
* [**Cart Service**](docs/service-cart.md) - Redis caching strategy.
* [**Payment Service**](docs/service-payment.md) - Mock payment logic.

---

## 🛠️ Getting Started (Local)

### Prerequisites
* Docker & Docker Compose
* Java 21 JDK
* Maven

### Start Infrastructure
Run the helper script to spin up the databases and Kafka:
```bash
sh scripts/infra-up.sh
sh scripts/build-all.sh
```

## Author
Ashish Sharma  
Software Engineer
