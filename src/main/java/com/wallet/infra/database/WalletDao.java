package com.wallet.infra.database;

import com.wallet.infra.database.repositories.WalletRepository;
import org.springframework.stereotype.Component;

@Component
public class WalletDao {

    private final WalletRepository walletRepository;

    public WalletDao(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

}
