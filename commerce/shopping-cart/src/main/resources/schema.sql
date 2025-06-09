CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS shopping_cart (
    id UUID PRIMARY KEY,
    owner VARCHAR,
    is_activate BOOLEAN,
    UNIQUE(owner)
);

create TABLE IF NOT EXISTS product_quantity (
    id UUID PRIMARY KEY,
    quantity INTEGER
);

CREATE TABLE IF NOT EXISTS cart_products (
    cart_id UUID REFERENCES shopping_cart(id),
    product_id UUID REFERENCES product_quantity(id)
);
