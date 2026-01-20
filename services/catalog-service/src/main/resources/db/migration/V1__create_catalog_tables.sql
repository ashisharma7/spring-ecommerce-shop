CREATE TABLE categories
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    is_active   BOOLEAN      NOT NULL,
    parent_id   UUID REFERENCES categories (id),
    created_at  TIMESTAMP    NOT NULL
);

CREATE TABLE products
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT           NOT NULL,
    price       NUMERIC(19, 2) NOT NULL,
    image       TEXT,
    available   BOOLEAN        NOT NULL,
    category_id UUID           NOT NULL REFERENCES categories (id),
    created_at  TIMESTAMP      NOT NULL
);