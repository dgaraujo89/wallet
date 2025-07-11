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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
        var user = User.builder()
                .document(document)
                .build();

        when(userFindPort.findByDocument(document)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> walletService.create(document));

        verify(userFindPort, times(1)).findByDocument(document);
        verify(walletCreatePort).create(walletCaptor.capture());
        assertEquals(document, walletCaptor.getValue().getUser().document());
        assertEquals(BigDecimal.ZERO, walletCaptor.getValue().getBalance());
        assertNotNull(walletCaptor.getValue().getId());
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
        var user = User.builder()
                .document("12345678900")
                .build();
        var wallet = Wallet.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();

        when(walletFindPort.findById(any())).thenReturn(Optional.of(wallet));

        var foundWallet = assertDoesNotThrow(() -> walletService.findById(wallet.getId()));

        assertEquals(wallet, foundWallet);
        verify(walletFindPort, times(1)).findById(any());
        verifyNoInteractions(walletCreatePort);
        verifyNoInteractions(transactionFindPort);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDocumentDoesNotExist() {
        when(walletFindPort.findById(any())).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class,
                () -> walletService.findById(UuidCreator.getTimeOrderedEpoch()));
        assertEquals("Wallet not found.", exception.getMessage());

        verify(walletFindPort, times(1)).findById(any());
        verifyNoInteractions(userFindPort);
        verifyNoInteractions(walletCreatePort);
        verifyNoInteractions(transactionFindPort);
    }

    @Test
    void shouldGetWalletBalanceSuccessfully() {
        var walletId = UuidCreator.getTimeOrderedEpoch();

        when(transactionFindPort.loadBalanceByWallet(any(), any())).thenReturn(BigDecimal.TEN);

        var walletBalance = assertDoesNotThrow(() -> walletService.balance(walletId, null));

        assertEquals(BigDecimal.TEN, walletBalance.balance());

        verify(transactionFindPort, times(1)).loadBalanceByWallet(any(), any());
        verifyNoInteractions(userFindPort);
        verifyNoInteractions(walletFindPort);
        verifyNoInteractions(walletCreatePort);

    }

    @Test
    void shouldGetZeroWhenGettingBalanceForNonExistentWallet() {
        when(transactionFindPort.loadBalanceByWallet(any(), any())).thenReturn(BigDecimal.ZERO);

        var walletBalance = assertDoesNotThrow(() -> walletService.balance(UuidCreator.getTimeOrderedEpoch(), null));
        assertEquals(BigDecimal.ZERO, walletBalance.balance());

        verify(transactionFindPort, times(1)).loadBalanceByWallet(any(), any());
        verifyNoInteractions(userFindPort);
        verifyNoInteractions(walletFindPort);
        verifyNoInteractions(walletCreatePort);
    }

}