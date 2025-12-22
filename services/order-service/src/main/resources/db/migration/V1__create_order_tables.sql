CREATE TABLE orders (
  id UUID PRIMARY KEY,
  order_number VARCHAR(100) NOT NULL UNIQUE,
  user_id VARCHAR(100) NOT NULL,
  status VARCHAR(30) NOT NULL,
  total_amount NUMERIC(19,2) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE order_items (
  id UUID PRIMARY KEY,
  order_id UUID NOT NULL,
  product_id VARCHAR(100) NOT NULL,
  product_name VARCHAR(255) NOT NULL,
  price NUMERIC(19,2) NOT NULL,
  quantity INT NOT NULL,
  CONSTRAINT fk_order
    FOREIGN KEY (order_id) REFERENCES orders(id)
);
