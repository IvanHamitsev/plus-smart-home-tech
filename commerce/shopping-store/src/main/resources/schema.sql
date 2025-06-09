CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS shopping_store_product (
    id UUID PRIMARY KEY,
    product_name VARCHAR,
    description VARCHAR,
    image_src VARCHAR,
    quantity_state VARCHAR,
    product_state VARCHAR,
    product_category VARCHAR,
    price FLOAT
);
