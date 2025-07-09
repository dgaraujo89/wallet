
create table if not exists users (
    id serial not null,
    name varchar(50) not null,
    email varchar(80) not null,
    document varchar(14) not null,
    date_of_birth date not null,
    address_line_1 varchar(50) not null,
    complement varchar(50),
    city varchar(50) not null,
    state char(2) not null,
    postal_code varchar(8) not null,
    country varchar(50) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone,
    primary key (id)
);