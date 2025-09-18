DROP TABLE IF EXISTS addresses CASCADE;
DROP TABLE IF EXISTS deliveries;

create table if not exists addresses
(
    address_id uuid default gen_random_uuid() primary key,
    country    varchar(100),
    city       varchar(100),
    street     varchar(200),
    house      varchar(30),
    flat       varchar(20)
);

create table if not exists deliveries
(
    delivery_id      uuid default gen_random_uuid() primary key,
    from_address_id  uuid references addresses (address_id),
    to_address_id    uuid references addresses (address_id),
    order_id         uuid,
    delivery_state   varchar(20)
);