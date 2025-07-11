package com.wallet.infra.database.mappers;

import com.wallet.domain.Wallet;
import com.wallet.infra.database.entities.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WalletMapper {

    WalletEntity from(Wallet wallet);

    Wallet from(WalletEntity entity);
}
