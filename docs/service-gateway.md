# API Gateway Architecture

## 1. Goal
The single entry point for all external traffic. It acts as the "Edge Server" handling routing, security, and protocol translation.

## 2. Security & Authentication (Future: Keycloak)
We will **not** build custom login logic. We will use **Keycloak** as the Identity Provider (IdP).
- **Pattern:** OAuth2 / OpenID Connect (OIDC).
- **Flow:** Frontend -> Keycloak (Get Token) -> Gateway (Validate Token) -> Service.

## 3. Routing Configuration

| Route ID | Path Pattern | Destination | Auth Required? | Required Role |
| :--- | :--- | :--- | :--- | :--- |
| `auth-route` | `/login`, `/register` | Keycloak | No | Public |
| `catalog-read` | `/api/products/**` (GET) | Catalog | No | Public |
| `catalog-write`| `/api/products/**` (POST/PUT) | Catalog | **Yes** | `ROLE_ADMIN` |
| `cart-route` | `/api/cart/**` | Cart | **Yes** | `ROLE_USER` |
| `order-user` | `/api/orders` (POST/GET) | Order | **Yes** | `ROLE_USER` |
| `order-admin` | `/api/orders/all` | Order | **Yes** | `ROLE_ADMIN` |
| `inventory-read`| `/api/inventory/**` (GET) | Inventory | **Yes** | `ROLE_ADMIN` |

## 4. Cross-Cutting Concerns
- **CORS:** Allowed origins (e.g., `http://localhost:3000`).
- **Rate Limiting:** Protect backend services from flooding.