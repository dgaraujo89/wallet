package com.wallet.services.ports;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface TransactionFindPort {
    BigDecimal loadBalanceByWallet(UUID walletId, ZonedDateTime dateTime);
}
