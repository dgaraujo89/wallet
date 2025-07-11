package com.wallet.services;

import com.github.f4b6a3.uuid.UuidCreator;
import com.wallet.domain.Wallet;
import com.wallet.domain.WalletBalance;
import com.wallet.domain.WalletStatus;
import com.wallet.exceptions.NotFoundException;
import com.wallet.services.ports.TransactionFindPort;
import com.wallet.services.ports.UserFindPort;
import com.wallet.services.ports.WalletCreatePort;
import com.wallet.services.ports.WalletFindPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final UserFindPort userFindPort;
    private final WalletCreatePort walletCreatePort;
    private final WalletFindPort walletFindPort;
    private final TransactionFindPort transactionFindPort;

    public WalletService(UserFindPort userFindPort, WalletCreatePort walletCreatePort,
                         WalletFindPort walletFindPort, TransactionFindPort transactionFindPort) {
        this.userFindPort = userFindPort;
        this.walletCreatePort = walletCreatePort;
        this.walletFindPort = walletFindPort;
        this.transactionFindPort = transactionFindPort;
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

    public WalletBalance balance(final UUID id, ZonedDateTime dateTime) {
        if (dateTime == null) {
            dateTime = ZonedDateTime.now(ZoneOffset.UTC);
        }

        final var balance = transactionFindPort.loadBalanceByWallet(id, dateTime);

        return new WalletBalance(balance, dateTime);
    }

}
