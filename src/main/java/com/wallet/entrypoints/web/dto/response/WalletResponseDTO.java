package com.wallet.entrypoints.web.dto.response;

import com.wallet.domain.WalletStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;


public record WalletResponseDTO(UUID id, UserResponseDTO user, BigDecimal balance, WalletStatus status,
                                ZonedDateTime createdAt, ZonedDateTime lastTransactionAt) {
}
