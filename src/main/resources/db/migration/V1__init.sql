create table shop_unit
(
    id        uuid         not null
        constraint shop_unit_pkey
            primary key,
    date      timestamp    not null,
    name      varchar(255) not null,
    price     bigint,
    type      integer      not null,
    parent_id uuid
        constraint puuid
            references shop_unit
);
CREATE TABLE revision_info
(
    revision_id   serial PRIMARY KEY,
    rev_timestamp BIGINT NOT NULL
);
create table shop_unit_audit
(
    revision_id    INTEGER  NOT NULL,
    revision_type SMALLINT NOT NULL,
    id            uuid     NOT NULL,
    date          timestamp,
    date_mod      boolean,
    name          varchar(255),
    name_mod      boolean,
    price         bigint,
    price_mod     boolean,
    type          integer,
    type_mod      boolean,
    parent_id     uuid,
    parent_mod    boolean,
    PRIMARY KEY (revision_id, id),
    CONSTRAINT idfk_shop_unit_revinfo_rev_id
        FOREIGN KEY (revision_id) REFERENCES revision_info (revision_id)
);

create sequence hibernate_sequence;