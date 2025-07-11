package com.wallet.domain.builders;

import com.wallet.domain.User;
import com.wallet.domain.Wallet;
import com.wallet.domain.WalletStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class WalletBuilder {

    private UUID id;
    private User user;
    private BigDecimal balance;
    private WalletStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime lastTransactionAt;

    public WalletBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public WalletBuilder user(User user) {
        this.user = user;
        return this;
    }

    public WalletBuilder balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public WalletBuilder status(WalletStatus status) {
        this.status = status;
        return this;
    }

    public WalletBuilder createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public WalletBuilder updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public WalletBuilder lastTransactionAt(ZonedDateTime lastTransactionAt) {
        this.lastTransactionAt = lastTransactionAt;
        return this;
    }

    public Wallet build() {
        return new Wallet(id, user, balance, status, createdAt, updatedAt, lastTransactionAt);
    }
    
}
