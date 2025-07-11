package com.wallet.entrypoints.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionResponseDTO(UUID id, UUID wallet, UUID correlationId,
                                     BigDecimal amount, ZonedDateTime createdAt) {

    public static Builder builder() {
        return new Builder();
    }

    private TransactionResponseDTO(Builder builder) {
        this(builder.id, builder.wallet, builder.correlationId, builder.amount, builder.createdAt);
    }

    public static class Builder {
        private UUID id;
        private UUID wallet;
        private UUID correlationId;
        private BigDecimal amount;
        private ZonedDateTime createdAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder wallet(UUID wallet) {
            this.wallet = wallet;
            return this;
        }

        public Builder correlationId(UUID correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TransactionResponseDTO build() {
            return new TransactionResponseDTO(this);
        }
    }

}
