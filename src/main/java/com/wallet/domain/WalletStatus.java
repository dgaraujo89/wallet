package com.wallet.domain;

import java.util.Arrays;
import java.util.Objects;

public enum WalletStatus {

    OPEN(1),
    CLOSED(2);

    private final Integer value;

    WalletStatus(Integer value) {
        this.value = value;
    }

    public static WalletStatus resolve(Integer value) {
        return Arrays.stream(WalletStatus.values())
                .filter(v -> Objects.equals(v.value, value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value."));
    }

    public Integer getValue() {
        return value;
    }

}
