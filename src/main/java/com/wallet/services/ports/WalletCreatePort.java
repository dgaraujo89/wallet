package com.wallet.services.ports;

import com.wallet.domain.Wallet;

public interface WalletCreatePort {
    void create(Wallet wallet);
}
