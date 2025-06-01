CREATE TABLE IF NOT EXISTS product_in_warehouse (
    id VARCHAR PRIMARY KEY,
    fragile BOOLEAN,
    width FLOAT,
    height FLOAT,
    depth FLOAT,
    weight FLOAT,
    quantity INTEGER
);