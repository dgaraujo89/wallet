package com.wallet.services;

import com.github.f4b6a3.uuid.UuidCreator;
import com.wallet.domain.User;
import com.wallet.domain.Wallet;
import com.wallet.exceptions.NotFoundException;
import com.wallet.services.ports.TransactionFindPort;
import com.wallet.services.ports.UserFindPort;
import com.wallet.services.ports.WalletCreatePort;
import com.wallet.services.ports.WalletFindPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private UserFindPort userFindPort;

    @Mock
    private WalletCreatePort walletCreatePort;

    @Mock
    private WalletFindPort walletFindPort;

    @Mock
    private TransactionFindPort transactionFindPort;

    @Captor
    private ArgumentCaptor<Wallet> walletCaptor;

    @Test
    void shouldCreateWalletSuccessfully() {
        var document = "12345678900";
        var wallet = Wallet.builder()
                .user(User.builder()
                        .document(document)
                        .build())
                .balance(BigDecimal.ZERO)
                .build();

        when(userFindPort.findByDocument(document)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> walletService.create(document));

        verify(userFindPort).findByDocument(document);
        verify(walletCreatePort).create(walletCaptor.capture());
        assertEquals(document, walletCaptor.getValue().getUser().document());
        assertEquals(BigDecimal.ZERO, walletCaptor.getValue().getBalance());
    }

    @Test
    void shouldThrowExceptionWhenTryingToCreateExistingWallet() {
        var wallet = Wallet.builder()
                .user(User.builder()
                        .document("12345678900")
                        .build())
                .balance(BigDecimal.ZERO)
                .build();

        when(userFindPort.findByDocument(wallet.getUser().document())).thenReturn(Optional.of(User.builder().build()));

        var exception = assertThrows(IllegalArgumentException.class,
                () -> walletService.create(wallet.getUser().document()));
        assertEquals("Wallet already exists for this document.", exception.getMessage());

        verify(userFindPort).findByDocument(wallet.getUser().document());
        verify(walletCreatePort, never()).create(any());
    }

    @Test
    void shouldFindWalletByIdSuccessfully() {
        var walletId = UUID.randomUUID();
        var user = User.builder()
                .document("12345678900")
                .build();
        var wallet = Wallet.builder()
                .id(walletId)
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();

        when(walletFindPort.findById(walletId)).thenReturn(Optional.of(wallet));

        var foundWallet = assertDoesNotThrow(() -> walletService.findById(walletId));

        assertEquals(wallet, foundWallet);
        verify(walletFindPort).findById(walletId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenWalletIdDoesNotExist() {
        var walletId = UUID.randomUUID();

        when(walletFindPort.findById(walletId)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class,
                () -> walletService.findById(walletId));
        assertEquals("Wallet not found.", exception.getMessage());

        verify(walletFindPort).findById(walletId);
    }

    @Test
    void shouldFindWalletByDocumentSuccessfully() {
        var document = "12345678900";
        var user = User.builder()
                .document(document)
                .build();
        var wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();

        when(userFindPort.findByDocument(document)).thenReturn(Optional.of(user));

        var foundWallet = assertDoesNotThrow(() -> walletService.findById(UuidCreator.fromString(document)));

        assertEquals(wallet, foundWallet);
        verify(walletFindPort, times(1)).findById(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDocumentDoesNotExist() {
        var document = "12345678900";

        when(userFindPort.findByDocument(document)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class,
                () -> walletService.findById(UuidCreator.fromString(document)));
        assertEquals("Wallet not found.", exception.getMessage());

        verify(walletFindPort, times(1)).findById(any());
    }

    @Test
    void shouldGetWalletBalanceSuccessfully() {
        var walletId = UuidCreator.getTimeOrderedEpoch();
        var balance = BigDecimal.TEN;
        var user = User.builder()
                .document("12345678900")
                .build();
        var wallet = Wallet.builder()
                .id(walletId)
                .user(user)
                .balance(balance)
                .build();

        when(walletFindPort.findById(walletId)).thenReturn(Optional.of(wallet));

        var walletBalance = assertDoesNotThrow(() -> walletService.balance(walletId, null));

        assertEquals(balance, walletBalance.balance());
        verify(walletFindPort).findById(walletId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGettingBalanceForNonExistentWallet() {
        var walletId = UuidCreator.getTimeOrderedEpoch();

        when(walletFindPort.findById(walletId)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class,
                () -> walletService.balance(walletId, null));
        assertEquals("Wallet not found.", exception.getMessage());

        verify(walletFindPort).findById(walletId);
    }

}