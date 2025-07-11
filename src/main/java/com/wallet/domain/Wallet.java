package com.wallet.domain;

import com.wallet.domain.builders.WalletBuilder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Wallet {

    private final UUID id;
    private final User user;
    private BigDecimal balance;
    private final WalletStatus status;
    private final ZonedDateTime createdAt;
    private final ZonedDateTime updatedAt;
    private ZonedDateTime lastTransactionAt;

    public Wallet(UUID id, User user, BigDecimal balance, WalletStatus status,
                   ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime lastTransactionAt) {
        this.id = id;
        this.user = user;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastTransactionAt = lastTransactionAt;
    }

    public static WalletBuilder builder() {
        return new WalletBuilder();
    }

    public void calculate(final Transaction transaction) {
        switch (transaction.type()) {
            case CREDIT -> balance = balance.add(transaction.amount());
            case DEBIT -> balance = balance.subtract(transaction.amount());
        }

        lastTransactionAt = transaction.createdAt();
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTime getLastTransactionAt() {
        return lastTransactionAt;
    }
}
