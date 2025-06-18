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