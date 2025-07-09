
create table if not exists wallets (
    id uuid not null,
    user_id bigint not null,
    balance numeric(18,2) not null default 0.0,
    status smallint not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone,
    last_transaction_at timestamp with time zone not null,
    primary key (id),
    constraint user_wallet foreign key (user_id) REFERENCES users(id)
);