create table shop_unit
(
    id uuid not null
        constraint shop_unit_pkey
        primary key,
    date timestamp not null,
    name varchar(255) not null,
    price bigint,
    type integer not null,
    parent_id uuid
        constraint fkl2nx2b5fsamdqyir6lif6n7as
        references shop_unit
);