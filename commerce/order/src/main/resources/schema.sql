DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS order_items;


CREATE TABLE IF NOT EXISTS orders (
    order_id uuid default gen_random_uuid() primary key,
    shopping_cart_id uuid NOT NULL,
    payment_id uuid NOT NULL,
    delivery_id uuid NOT NULL,
    state VARCHAR(20) NOT NULL,
    delivery_weight NUMERIC(10, 2) NOT NULL,
    delivery_volume NUMERIC(10, 2) NOT NULL,
    fragile BOOLEAN NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL,
    delivery_price NUMERIC(10, 2) NOT NULL,
    product_price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    product_id uuid NOT NULL,
    quantity integer NOT NULL,
    order_id uuid NOT NULL references orders (order_id) on delete cascade,
    PRIMARY KEY (order_id, product_id)
);