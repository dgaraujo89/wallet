package com.wallet.domain;

import com.github.f4b6a3.uuid.UuidCreator;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public record Transaction(UUID id, Wallet wallet, UUID correlationId,
                          TransactionType type, BigDecimal amount, ZonedDateTime createdAt) {

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public static class TransactionBuilder {
        private Wallet wallet;
        private UUID correlationId;
        private TransactionType type;
        private BigDecimal amount;
        private ZonedDateTime createdAt;

        public TransactionBuilder wallet(Wallet wallet) {
            this.wallet = wallet;
            return this;
        }

        public TransactionBuilder correlationId(UUID correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public TransactionBuilder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public TransactionBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Transaction build() {
            return new Transaction(
                    UuidCreator.getTimeOrderedEpoch(),
                    wallet,
                    correlationId,
                    type,
                    amount,
                    createdAt != null ? createdAt : ZonedDateTime.now(ZoneOffset.UTC)
            );
        }

    }

}
