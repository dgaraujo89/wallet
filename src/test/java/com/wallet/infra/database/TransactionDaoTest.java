package com.wallet.infra.database;

import com.wallet.domain.Transaction;
import com.wallet.infra.database.entities.TransactionEntity;
import com.wallet.infra.database.mappers.TransactionMapper;
import com.wallet.infra.database.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionDaoTest {

    @InjectMocks
    private TransactionDao transactionDao;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    void shouldSaveTransactionSuccessfully() {
        var transaction = Transaction.builder().build();
        var transactionEntity = new TransactionEntity();

        when(transactionMapper.from(transaction)).thenReturn(transactionEntity);

        transactionDao.save(transaction);

        verify(transactionMapper).from(transaction);
        verify(transactionRepository).save(transactionEntity);
    }

    @Test
    void shouldLoadBalanceByWalletSuccessfully() {
        var walletId = UUID.randomUUID();
        var dateTime = ZonedDateTime.now();
        var expectedBalance = BigDecimal.TEN;

        when(transactionRepository.getBalanceByWalletIdAndDate(walletId, dateTime))
                .thenReturn(Optional.of(expectedBalance));

        var balance = transactionDao.loadBalanceByWallet(walletId, dateTime);

        assertEquals(expectedBalance, balance);
        verify(transactionRepository).getBalanceByWalletIdAndDate(walletId, dateTime);
    }

}