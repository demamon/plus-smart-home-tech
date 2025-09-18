DROP TABLE IF EXISTS payments;

CREATE TABLE IF NOT EXISTS payments (
    payment_id uuid default gen_random_uuid() primary key,
    order_id uuid NOT NULL,
    total_payment NUMERIC(10, 2) NOT NULL,
    delivery_total NUMERIC(10, 2) NOT NULL,
    fee_total NUMERIC(10, 2) NOT NULL,
    product_total NUMERIC(10, 2) NOT NULL,
    payment_state VARCHAR(10) NOT NULL
);