drop table if exists warehouse_product;

create table if not exists warehouse_product
(
    product_id uuid primary key,
    quantity   integer,
    fragile    boolean,
    width      double precision,
    height     double precision,
    depth      double precision,
    weight     double precision
);