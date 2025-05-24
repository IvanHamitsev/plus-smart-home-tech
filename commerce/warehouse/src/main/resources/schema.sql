CREATE TABLE IF NOT EXISTS product_in_warehouse (
    id VARCHAR PRIMARY KEY,
    fragile BOOLEAN,
    width DOUBLE,
    height DOUBLE,
    depth DOUBLE,
    weight DOUBLE,
    quantity INTEGER
);