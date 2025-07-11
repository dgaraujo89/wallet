package com.wallet.entrypoints.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequestDTO(@NotNull @Positive BigDecimal amount) {
}
