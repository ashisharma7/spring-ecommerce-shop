# Cart Service Architecture

## 1. Goal
The **Cart Service** provides low-latency, temporary storage for user shopping sessions. It handles high-frequency writes (users adding/removing items quickly) and is optimized for speed using **Redis**.

## 2. Domain Model (Redis Objects)

Since Redis is a NoSQL store, we define the **Object Structure** (Java Class) rather than SQL tables. These objects are serialized to JSON and stored.

### Cart (Root Object)
* **Key Pattern:** `cart:{userId}` (e.g., `cart:12345`)
* **TTL (Time-To-Live):** 30 Days (Resets on every write activity)

| Field | Type | Description |
| :--- | :--- | :--- |
| `userId` | String | Keycloak User ID (matches the key) |
| `items` | List<CartItem> | Collection of products |
| `totalPrice` | BigDecimal | Dynamic total (Calculated on save) |

### CartItem (Nested Object)
| Field | Type | Description |
| :--- | :--- | :--- |
| `productId` | String | Reference to Catalog Product ID |
| `productName` | String | Cached name (to avoid fetching Catalog for every view) |
| `quantity` | Integer | Selection count |
| `price` | BigDecimal | Cached unit price |

---

## 3. Kafka Event Strategy
The Cart Service acts as a **Listener** to clean up data after a purchase.

### 📥 Consumed Events (What it hears)
| Event Name | Source | Action Taken |
| :--- | :--- | :--- |
| `OrderCreatedEvent` | Order Service | **Clear Cart:** Identify the `userId` from the event payload and delete their cart key (`cart:{userId}`) from Redis. |

### 📤 Produced Events
*None for now.* (The Cart Service does not emit events in V1).

---

## 4. API Endpoints
All endpoints are secured and require the `ROLE_USER` authority. The `userId` is extracted automatically from the JWT Token (Principal).

### User Operations (`ROLE_USER`)

| Method | Endpoint | Description | Payload Example |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/cart` | Get current user's cart | - |
| `POST` | `/api/cart/items` | Add/Update item in cart | `{ "productId": "p-101", "quantity": 1 }` |
| `DELETE` | `/api/cart/items/{productId}` | Remove specific item | - |
| `DELETE` | `/api/cart` | Clear entire cart (Manual) | - |

---

## 5. External Integrations

### Inventory Service
The Cart Service communicates **Synchronously** (via Feign Client / REST) with the Inventory Service to validate stock availability in real-time.

- **Trigger:** When User views the Cart (`GET /api/cart`).
- **Endpoint Called:** `POST http://inventory-service/api/inventory/check`
- **Logic:**
    1. Fetch Cart from Redis.
    2. Extract all `productIds`.
    3. Call Inventory Service batch check.
    4. If any item has `quantity < requested`, mark it as "Out of Stock" in the UI response (but do not delete it from Redis automatically).

## 6. Technology Stack
- **Database:** Redis (Alpine Image)
- **Library:** Spring Data Redis
- **Serialization:** Jackson (JSON)
