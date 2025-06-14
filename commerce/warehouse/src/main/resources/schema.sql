CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS product_in_warehouse (
    id UUID PRIMARY KEY,
    fragile BOOLEAN,
    width FLOAT,
    height FLOAT,
    depth FLOAT,
    weight FLOAT,
    quantity INTEGER
);