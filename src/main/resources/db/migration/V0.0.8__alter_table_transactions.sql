
drop index idx_transaction_correlation_id;

create unique index idx_transaction_correlation_id
    on transactions (correlation_id, transaction_type);;
