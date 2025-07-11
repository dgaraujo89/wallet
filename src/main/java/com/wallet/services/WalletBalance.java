package com.wallet.services;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record WalletBalance(BigDecimal balance, ZonedDateTime at) {

}
