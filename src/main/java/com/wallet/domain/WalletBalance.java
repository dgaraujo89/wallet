package com.wallet.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record WalletBalance(BigDecimal balance, ZonedDateTime at) {

}
