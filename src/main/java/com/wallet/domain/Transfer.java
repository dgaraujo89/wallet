package com.wallet.domain;

import java.time.ZonedDateTime;

public record Transfer(Transaction debit, Transaction credit, ZonedDateTime createdAt) {

    public static TransferBuilder builder() {
        return new TransferBuilder();
    }

    public static class TransferBuilder {
        private Transaction debit;
        private Transaction credit;
        private ZonedDateTime createdAt;

        public TransferBuilder debit(Transaction debit) {
            this.debit = debit;
            return this;
        }

        public TransferBuilder credit(Transaction credit) {
            this.credit = credit;
            return this;
        }

        public TransferBuilder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Transfer build() {
            return new Transfer(debit, credit, createdAt);
        }
    }

}
