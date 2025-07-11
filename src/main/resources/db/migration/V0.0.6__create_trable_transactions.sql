
create table if not exists transactions (
    id uuid not null,
    wallet_id uuid not null,
    correlation_id uuid not null,
    transaction_type smallint not null,
    amount numeric(18,2) not null,
    created_at timestamp with time zone not null default now(),
    primary key (id),
    foreign key (wallet_id) references wallets(id)
);