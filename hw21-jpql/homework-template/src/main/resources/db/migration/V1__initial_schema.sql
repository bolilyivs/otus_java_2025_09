-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence address_SEQ start with 1 increment by 1;

create table address
(
    id     bigint not null primary key,
    street varchar(50)
);

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id         bigint not null primary key,
    name       varchar(50),
    address_id bigint references address
);

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence phone_SEQ start with 1 increment by 1;

create table phone
(
    id        bigint not null primary key,
    number    varchar(50),
    client_id bigint references client
);
