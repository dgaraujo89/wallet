package com.wallet.entrypoints.web.dto.response;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record WalletBalanceResponseDTO(BigDecimal balance, ZonedDateTime at) {
}
