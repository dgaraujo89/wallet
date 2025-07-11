package com.wallet.infra.database;

import com.wallet.domain.Wallet;
import com.wallet.infra.database.entities.WalletEntity;
import com.wallet.infra.database.mappers.WalletMapper;
import com.wallet.infra.database.repositories.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletDaoTest {

    @InjectMocks
    private WalletDao walletDao;

    @Mock
    private WalletMapper walletMapper;

    @Mock
    private WalletRepository walletRepository;

    @Test
    void shouldCreateWalletSuccessfully() {
        var wallet = Wallet.builder().build();
        var walletEntity = new WalletEntity();

        when(walletMapper.from(wallet)).thenReturn(walletEntity);

        walletDao.create(wallet);

        verify(walletMapper).from(wallet);
        verify(walletRepository).save(walletEntity);
    }

    @Test
    void shouldFindWalletByIdSuccessfully() {
        var id = UUID.randomUUID();
        var wallet = Wallet.builder().build();
        var walletEntity = new WalletEntity();

        when(walletRepository.findById(id)).thenReturn(Optional.of(walletEntity));
        when(walletMapper.from(walletEntity)).thenReturn(wallet);

        var foundWallet = walletDao.findById(id);

        assertTrue(foundWallet.isPresent());
        assertEquals(wallet, foundWallet.get());
        verify(walletRepository).findById(id);
        verify(walletMapper).from(walletEntity);
    }

    @Test
    void shouldUpdateWalletSuccessfully() {
        var wallet = Wallet.builder().build();
        var walletEntity = new WalletEntity();

        when(walletMapper.from(wallet)).thenReturn(walletEntity);

        walletDao.update(wallet);

        verify(walletMapper).from(wallet);
        verify(walletRepository).save(walletEntity);
    }

}