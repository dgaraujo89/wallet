package com.wallet.domain;

public enum TransactionType {

    CREDIT(1),
    DEBIT(2);

    private final Integer value;

    TransactionType(Integer value) {
        this.value = value;
    }

    public static TransactionType resolve(Integer value) {
        return java.util.Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction type value: " + value));
    }

    public Integer getValue() {
        return value;
    }
}
