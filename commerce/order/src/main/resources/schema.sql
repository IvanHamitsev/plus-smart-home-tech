CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS shop_order (
    id UUID PRIMARY KEY NOT NULL,
    username VARCHAR,
    state VARCHAR,
    shopping_cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    fragile BOOLEAN,
    delivery_weight FLOAT,
    delivery_volume FLOAT,
    delivery_price NUMERIC,
    product_price NUMERIC,
    total_price NUMERIC
);

create TABLE IF NOT EXISTS order_product (
    id UUID PRIMARY KEY NOT NULL,
    quantity INTEGER
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id UUID REFERENCES shop_order(id),
    product_id UUID REFERENCES order_product(id)
);