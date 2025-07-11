package com.wallet.services;

import com.github.f4b6a3.uuid.UuidCreator;
import com.wallet.domain.Transaction;
import com.wallet.domain.TransactionType;
import com.wallet.domain.Wallet;
import com.wallet.domain.WalletStatus;
import com.wallet.exceptions.NotFoundException;
import com.wallet.services.ports.TransactionSavePort;
import com.wallet.services.ports.UserFindPort;
import com.wallet.services.ports.WalletCreatePort;
import com.wallet.services.ports.WalletFindPort;
import com.wallet.services.ports.WalletUpdatePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final UserFindPort userFindPort;
    private final WalletCreatePort walletCreatePort;
    private final WalletFindPort walletFindPort;

    public WalletService(UserFindPort userFindPort, WalletCreatePort walletCreatePort, WalletFindPort walletFindPort) {
        this.userFindPort = userFindPort;
        this.walletCreatePort = walletCreatePort;
        this.walletFindPort = walletFindPort;
    }

    public Wallet create(final String document) {
        final var user = userFindPort.findByDocument(document)
                .orElseThrow(() -> new NotFoundException("User not found."));

        final var wallet = Wallet.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .user(user)
                .balance(BigDecimal.ZERO)
                .status(WalletStatus.OPEN)
                .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();

        walletCreatePort.create(wallet);
        return wallet;
    }

    public Wallet findById(UUID id) {
        return walletFindPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Wallet not found."));
    }

}
