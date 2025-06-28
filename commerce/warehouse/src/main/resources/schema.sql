CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS product_in_warehouse (
    id UUID PRIMARY KEY NOT NULL,
    fragile BOOLEAN,
    width FLOAT,
    height FLOAT,
    depth FLOAT,
    weight FLOAT,
    quantity INTEGER
);

CREATE TABLE IF NOT EXISTS address (
    id UUID PRIMARY KEY NOT NULL,
    country VARCHAR,
    city VARCHAR,
    street VARCHAR,
    house VARCHAR,
    flat VARCHAR
);

CREATE TABLE IF NOT EXISTS delivery (
    id UUID PRIMARY KEY NOT NULL,
    state VARCHAR,
    order_id VARCHAR,
    from_address UUID REFERENCES address(id),
    to_address UUID REFERENCES address(id)
);

CREATE TABLE IF NOT EXISTS order_booking (
    id UUID PRIMARY KEY NOT NULL,
    delivery_id UUID REFERENCES delivery(id)
);

CREATE TABLE IF NOT EXISTS booking_products (
    booking_id UUID REFERENCES order_booking(id),
    product_id UUID REFERENCES shopping_cart(id),
    PRIMARY KEY(booking_id, product_id)
);