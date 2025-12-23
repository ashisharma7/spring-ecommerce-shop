# Catalog Service Architecture

## 1. Goal
The Catalog Service is the authoritative source for Product information. It manages the "Nouns" of the system. It is optimized for **high-volume reads**.

## 2. Domain Model
We will not store Inventory (stock counts) here. That belongs to the Inventory Service.

### Category Entity
Allows hierarchical organization (e.g., Electronics -> Mobile Phones).
| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | UUID | Primary Key |
| `name` | String | e.g., "Smartphones" |
| `description` | String | Short category description |
| `parent_id` | UUID | Self-referencing FK for sub-categories (Nullable) |

### Product Entity
| Field | Type | Description                    |
| :--- | :--- |:-------------------------------|
| `id` | UUID | Primary Key                    |
| `name` | String | Product Name                   |
| `description` | Text | Detailed HTML/Text description |
| `price` | BigDecimal | Base price (INR)               |
| `image_url` | String | URL to image                   |
| `category_id` | UUID | **Foreign Key** to Category    |
| `active` | Boolean | Soft delete flag               |

## 3. API Contract (v1)

### Reads (Public)
- `GET /api/categories` - List all top-level categories.
- `GET /api/categories/{id}/products` - Get all products in a specific category.
- `GET /api/products` - List all active products (Paginated).
- `GET /api/products/{id}` - Get single product details.

### Writes (Admin Only - `ROLE_ADMIN`)
- `POST /api/categories` - Create a new category.
- `POST /api/products` - Create new product.
- `PUT /api/products/{id}` - Update product details.
- `DELETE /api/products/{id}` - Soft delete a product.

### Internal Endpoints (Service-to-Service)

#### Price & Product Lookup (Order Service)
- `POST /internal/catalog/products/info` 
  - Used by **Order Service** during order creation to fetch
    current product information.

## 4. Data Storage
- **Primary:** PostgreSQL (`catalog_db`)
- **Tables:** (`categories`,`products`)
- **Caching:** (Future) Redis for `GET /api/products/{id}`.