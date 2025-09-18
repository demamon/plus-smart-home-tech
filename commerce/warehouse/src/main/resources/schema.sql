drop table if exists warehouse_product;
drop table if exists bookings;
drop table if exists booking_products;

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

create table if not exists bookings
(
    id uuid primary key,
    delivery_weight  double precision not null,
    delivery_volume  double precision not null,
    fragile          boolean          not null,
    order_id uuid not null unique,
    delivery_id uuid
);

create table if not exists booking_products
(
    booking_id uuid references bookings (id) on delete cascade,
    product_id       uuid not null,
    quantity         integer,
    primary key (booking_id, product_id)
    );