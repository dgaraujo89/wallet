package com.wallet.entrypoints.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransferResponseDTO(UUID correlationId,
                                  BigDecimal amount,
                                  TransactionResponseDTO debitTransaction,
                                  TransactionResponseDTO creditTransaction,
                                  ZonedDateTime createdAt) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID correlationId;
        private BigDecimal amount;
        private TransactionResponseDTO debitTransaction;
        private TransactionResponseDTO creditTransaction;
        private ZonedDateTime createdAt;

        public Builder correlationId(UUID correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder debitTransaction(TransactionResponseDTO debitTransaction) {
            this.debitTransaction = debitTransaction;
            return this;
        }

        public Builder creditTransaction(TransactionResponseDTO creditTransaction) {
            this.creditTransaction = creditTransaction;
            return this;
        }

        public Builder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TransferResponseDTO build() {
            return new TransferResponseDTO(correlationId, amount, debitTransaction, creditTransaction, createdAt);
        }
    }

}
