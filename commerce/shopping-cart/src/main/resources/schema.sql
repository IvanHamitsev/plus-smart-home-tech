CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS shopping_cart (
    id UUID PRIMARY KEY NOT NULL,
    owner VARCHAR NOT NULL,
    is_activate BOOLEAN NOT NULL,
    UNIQUE(owner)
);

create TABLE IF NOT EXISTS product_quantity (
    id UUID PRIMARY KEY NOT NULL,
    quantity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_products (
    cart_id UUID REFERENCES shopping_cart(id),
    product_id UUID REFERENCES product_quantity(id)
);
