CREATE TABLE IF NOT EXISTS shopping_cart (
    id VARCHAR PRIMARY KEY,
    owner VARCHAR,
    is_activate BOOLEAN,
    UNIQUE(owner)
);

create TABLE IF NOT EXISTS product_quantity (
    id VARCHAR PRIMARY KEY,
    quantity INTEGER
);

CREATE TABLE IF NOT EXISTS cart_products (
    cart_id VARCHAR REFERENCES shopping_cart(id),
    product_id VARCHAR REFERENCES product_quantity(id)
);
