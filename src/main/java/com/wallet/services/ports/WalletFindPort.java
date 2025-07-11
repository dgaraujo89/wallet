package com.wallet.services.ports;

import com.wallet.domain.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletFindPort {
    Optional<Wallet> findById(UUID id);
}
