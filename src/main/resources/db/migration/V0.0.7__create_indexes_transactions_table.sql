create unique index idx_transaction_correlation_id
    on transactions using btree (correlation_id)
    where correlation_id is not null;

create index idx_transaction_wallet_transactions
    on transactions using btree (wallet_id, created_at);