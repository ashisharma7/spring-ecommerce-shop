# E-Commerce Shop

This repository contains a **learning-focused microservices system** designed to understand and practice real-world backend architecture concepts.

The project follows a **design-first approach**, where service boundaries, data ownership, and communication patterns are finalized before implementation. The goal is not rapid feature delivery, but building a correct, scalable, and cloud-ready system using industry-standard practices.

---

## 🎯 Learning Objectives

This project is built to learn and apply:

- Microservices architecture and service decomposition
- Database-per-service and clear data ownership
- Event-driven communication using Kafka
- Saga-based choreography for distributed workflows
- Failure handling and eventual consistency
- Local-first development using Docker and Docker Compose
- Clean Git workflows and professional repository structure
- Designing systems that are cloud-ready without being cloud-dependent

---

## 🧠 How This Project Is Being Built

The system is developed incrementally using the following principles:

1. **Design before code**  
   Architecture and service responsibilities are documented before implementation.

2. **Independent services**  
   Each microservice is a standalone Spring Boot application with its own lifecycle, configuration, and database.

3. **Explicit communication**
    - REST is used for synchronous request–response interactions.
    - Kafka is used for asynchronous, event-driven communication between services.

4. **Local-first, cloud-ready**  
   All services are designed to run locally using Docker Compose, with a structure that allows easy migration to cloud platforms later.

5. **Incremental complexity**  
   Infrastructure and features are added gradually to avoid premature over-engineering.

---

## 🏗️ Planned High-Level Architecture

**Core Services (v1):**
- Catalog Service — product and pricing information
- Cart Service — user cart and temporary state
- Order Service — order lifecycle and orchestration
- Inventory Service — stock reservation and release
- Payment Service — payment processing (mock)

**Supporting Components:**
- API Gateway — routing and authentication
- Kafka — asynchronous event communication
- PostgreSQL — persistent storage per service
- Redis — in-memory storage for cart data

---

## 📂 Repository Structure

```
shop-microservices/
├── services/        # Independent Spring Boot microservices
├── frontend/        # React applications
├── infra/           # Local infrastructure (Docker) and future cloud configs
├── docs/            # Architecture and design documentation
├── scripts/         # Helper scripts for local development
└── README.md
```

---

## 🚧 Project Status

**In progress**

Current focus:
- Repository structure and Git hygiene
- Service boundary definition
- Architecture and API design documentation

---

## 📖 Documentation
All architectural decisions and service designs are documented in the `docs/` directory.

## Author
Ashish Sharma  
Software Engineer
