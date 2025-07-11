package com.wallet.infra.database;

import com.wallet.domain.Transaction;
import com.wallet.infra.database.mappers.TransactionMapper;
import com.wallet.infra.database.repositories.TransactionRepository;
import com.wallet.services.ports.TransactionSavePort;
import org.springframework.stereotype.Component;

@Component
public class TransactionDao implements TransactionSavePort {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionDao(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transactionMapper.from(transaction));
    }

}
