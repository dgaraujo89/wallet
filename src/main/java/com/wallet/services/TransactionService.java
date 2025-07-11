package com.wallet.services;

import com.wallet.domain.Transaction;
import com.wallet.domain.TransactionType;
import com.wallet.domain.Transfer;
import com.wallet.domain.Wallet;
import com.wallet.exceptions.NotFoundException;
import com.wallet.services.ports.TransactionFindPort;
import com.wallet.services.ports.TransactionSavePort;
import com.wallet.services.ports.WalletFindPort;
import com.wallet.services.ports.WalletUpdatePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    private final WalletFindPort walletFindPort;
    private final WalletUpdatePort walletUpdatePort;
    private final TransactionSavePort transactionSavePort;

    public TransactionService(WalletFindPort walletFindPort,
                              WalletUpdatePort walletUpdatePort,
                              TransactionSavePort transactionSavePort) {
        this.walletFindPort = walletFindPort;
        this.walletUpdatePort = walletUpdatePort;
        this.transactionSavePort = transactionSavePort;
    }

    @Transactional
    public Transaction deposit(final UUID walletId, final UUID correlationId, final BigDecimal amount) {
        final var transaction = Transaction.builder()
                .wallet(Wallet.builder()
                        .id(walletId)
                        .build())
                .correlationId(correlationId)
                .type(TransactionType.CREDIT)
                .amount(amount)
                .build();

        return registerTransaction(transaction, walletId);
    }

    @Transactional
    public Transaction withdraw(final UUID walletId, final UUID correlationId, final BigDecimal amount) {
        final var transaction = Transaction.builder()
                .wallet(Wallet.builder()
                        .id(walletId)
                        .build())
                .correlationId(correlationId)
                .type(TransactionType.DEBIT)
                .amount(amount)
                .build();

        return registerTransaction(transaction, walletId);
    }

    @Transactional
    public Transfer transfer(final UUID fromWalletId, final UUID toWalletId,
                             final UUID correlationId, final BigDecimal amount) {
        if (fromWalletId.equals(toWalletId)) {
            throw new IllegalArgumentException("Cannot transfer to the same wallet.");
        }

        final var createdAt = ZonedDateTime.now(ZoneOffset.UTC);

        final var fromTransaction = Transaction.builder()
                .wallet(Wallet.builder()
                        .id(fromWalletId)
                        .build())
                .correlationId(correlationId)
                .type(TransactionType.DEBIT)
                .amount(amount)
                .createdAt(createdAt)
                .build();

        final var toTransaction = Transaction.builder()
                .wallet(Wallet.builder()
                        .id(toWalletId)
                        .build())
                .correlationId(correlationId)
                .type(TransactionType.CREDIT)
                .amount(amount)
                .createdAt(createdAt)
                .build();

        registerTransaction(fromTransaction, fromWalletId);
        registerTransaction(toTransaction, toWalletId);

        return Transfer.builder()
                .debit(fromTransaction)
                .credit(toTransaction)
                .createdAt(createdAt)
                .build();
    }

    private Transaction registerTransaction(final Transaction transaction, final UUID walletId) {
        final var wallet = walletFindPort.findById(walletId)
                .orElseThrow(() -> new NotFoundException("Wallet ("+ walletId +") not found."));

        wallet.calculate(transaction);

        if(wallet.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        transactionSavePort.save(transaction);
        walletUpdatePort.update(wallet);

        return transaction;
    }

}
