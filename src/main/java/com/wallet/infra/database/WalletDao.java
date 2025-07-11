package com.wallet.infra.database;

import com.github.f4b6a3.uuid.UuidCreator;
import com.wallet.domain.Wallet;
import com.wallet.infra.database.mappers.WalletMapper;
import com.wallet.infra.database.repositories.WalletRepository;
import com.wallet.services.ports.WalletCreatePort;
import com.wallet.services.ports.WalletFindPort;
import com.wallet.services.ports.WalletUpdatePort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class WalletDao implements WalletCreatePort, WalletFindPort, WalletUpdatePort {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    public WalletDao(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    @Override
    public void create(Wallet wallet) {
        walletRepository.save(walletMapper.from(wallet));
    }

    @Override
    public Optional<Wallet> findById(UUID id) {
        final var entity = walletRepository.findById(id);
        return entity.map(walletMapper::from);
    }

    @Override
    public void update(Wallet wallet) {
        walletRepository.save(walletMapper.from(wallet));
    }

}
