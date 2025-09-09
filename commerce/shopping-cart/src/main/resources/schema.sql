DROP TABLE IF EXISTS shopping_cart CASCADE;
DROP TABLE IF EXISTS shopping_cart_items;


CREATE TABLE IF NOT EXISTS shopping_cart (
    shopping_cart_id uuid default gen_random_uuid() primary key,
    username VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS shopping_cart_items (
    product_id uuid NOT NULL,
    quantity integer NOT NULL,
    cart_id uuid NOT NULL references shopping_cart (shopping_cart_id) on delete cascade,
    PRIMARY KEY (cart_id, product_id)
);

