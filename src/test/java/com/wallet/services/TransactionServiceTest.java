package com.wallet.services;

import com.wallet.domain.Transaction;
import com.wallet.domain.TransactionType;
import com.wallet.domain.Wallet;
import com.wallet.exceptions.NotFoundException;
import com.wallet.services.ports.TransactionSavePort;
import com.wallet.services.ports.WalletFindPort;
import com.wallet.services.ports.WalletUpdatePort;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private WalletFindPort walletFindPort;

    @Mock
    private WalletUpdatePort walletUpdatePort;

    @Mock
    private TransactionSavePort transactionSavePort;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @Captor
    private ArgumentCaptor<Wallet> walletCaptor;

    @Test
    void shouldDepositSuccessfully() {
        var walletId = UUID.randomUUID();
        var correlationId = UUID.randomUUID();
        var amount = BigDecimal.TEN;
        var wallet = Wallet.builder()
                .id(walletId)
                .balance(BigDecimal.ZERO)
                .build();

        when(walletFindPort.findById(walletId)).thenReturn(Optional.of(wallet));

        assertDoesNotThrow(() -> transactionService.deposit(walletId, correlationId, amount));

        verify(walletFindPort).findById(walletId);
        verify(transactionSavePort).save(transactionCaptor.capture());
        verify(walletUpdatePort).update(walletCaptor.capture());

        assertEquals(TransactionType.CREDIT, transactionCaptor.getValue().type());
        assertEquals(amount, transactionCaptor.getValue().amount());
        assertEquals(amount, walletCaptor.getValue().getBalance());
    }

    @Test
    void shouldWithdrawSuccessfully() {
        var walletId = UUID.randomUUID();
        var correlationId = UUID.randomUUID();
        var amount = BigDecimal.TEN;
        var wallet = Wallet.builder()
                .id(walletId)
                .balance(BigDecimal.valueOf(20))
                .build();

        when(walletFindPort.findById(walletId)).thenReturn(Optional.of(wallet));

        assertDoesNotThrow(() -> transactionService.withdraw(walletId, correlationId, amount));

        verify(walletFindPort).findById(walletId);
        verify(transactionSavePort).save(transactionCaptor.capture());
        verify(walletUpdatePort).update(walletCaptor.capture());

        assertEquals(TransactionType.DEBIT, transactionCaptor.getValue().type());
        assertEquals(amount, transactionCaptor.getValue().amount());
        assertEquals(BigDecimal.TEN, walletCaptor.getValue().getBalance());
    }

    @Test
    void shouldTransferSuccessfully() {
        var fromWalletId = UUID.randomUUID();
        var toWalletId = UUID.randomUUID();
        var correlationId = UUID.randomUUID();
        var amount = BigDecimal.TEN;
        var fromWallet = Wallet.builder()
                .id(fromWalletId)
                .balance(BigDecimal.valueOf(20))
                .build();
        var toWallet = Wallet.builder()
                .id(toWalletId)
                .balance(BigDecimal.ZERO)
                .build();

        when(walletFindPort.findById(fromWalletId)).thenReturn(Optional.of(fromWallet));
        when(walletFindPort.findById(toWalletId)).thenReturn(Optional.of(toWallet));

        var transfer = assertDoesNotThrow(() ->
                transactionService.transfer(fromWalletId, toWalletId, correlationId, amount));

        verify(walletFindPort).findById(fromWalletId);
        verify(walletFindPort).findById(toWalletId);
        verify(transactionSavePort, times(2)).save(any());
        verify(walletUpdatePort, times(2)).update(any());

        assertEquals(TransactionType.DEBIT, transfer.debit().type());
        assertEquals(TransactionType.CREDIT, transfer.credit().type());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenWalletDoesNotExist() {
        var walletId = UUID.randomUUID();
        var correlationId = UUID.randomUUID();
        var amount = BigDecimal.TEN;

        when(walletFindPort.findById(walletId)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class,
                () -> transactionService.deposit(walletId, correlationId, amount));
        assertEquals("Wallet (" + walletId + ") not found.", exception.getMessage());

        verify(walletFindPort).findById(walletId);
        verify(transactionSavePort, never()).save(any());
        verify(walletUpdatePort, never()).update(any());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        var walletId = UUID.randomUUID();
        var correlationId = UUID.randomUUID();
        var amount = BigDecimal.valueOf(100);
        var wallet = Wallet.builder()
                .id(walletId)
                .balance(BigDecimal.TEN)
                .build();

        when(walletFindPort.findById(walletId)).thenReturn(Optional.of(wallet));

        var exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.withdraw(walletId, correlationId, amount));
        assertEquals("Insufficient funds.", exception.getMessage());

        verify(walletFindPort).findById(walletId);
        verify(transactionSavePort, never()).save(any());
        verify(walletUpdatePort, never()).update(any());
    }

    @Test
    void shouldThrowExceptionWhenTransferringToSameWallet() {
        var walletId = UUID.randomUUID();
        var correlationId = UUID.randomUUID();
        var amount = BigDecimal.TEN;

        var exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transfer(walletId, walletId, correlationId, amount));
        assertEquals("Cannot transfer to the same wallet.", exception.getMessage());

        verify(walletFindPort, never()).findById(any());
        verify(transactionSavePort, never()).save(any());
        verify(walletUpdatePort, never()).update(any());
    }

}