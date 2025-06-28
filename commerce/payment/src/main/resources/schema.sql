CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS payment (
    id UUID PRIMARY KEY NOT NULL,
    order_id UUID,
    product_cost NUMERIC,
    delivery_cost NUMERIC,
    total_cost NUMERIC,
    status VARCHAR
);