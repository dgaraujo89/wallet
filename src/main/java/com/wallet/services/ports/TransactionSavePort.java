package com.wallet.services.ports;

import com.wallet.domain.Transaction;

public interface TransactionSavePort {
    void save(Transaction transaction);
}
