DROP TABLE IF EXISTS products;

CREATE TABLE IF NOT EXISTS products (
        product_id uuid default gen_random_uuid() primary key,
            product_name VARCHAR(255) NOT NULL,
            description TEXT NOT NULL,
            image_src VARCHAR(500),
            quantity_state VARCHAR(50) NOT NULL,
            product_state VARCHAR(50) NOT NULL,
            product_category VARCHAR(50) NOT NULL,
            price NUMERIC(10, 2) NOT NULL
);