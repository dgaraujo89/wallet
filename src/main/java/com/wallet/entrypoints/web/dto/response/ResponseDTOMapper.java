package com.wallet.entrypoints.web.dto.response;

import com.wallet.domain.Transaction;
import com.wallet.domain.Transfer;
import com.wallet.domain.User;
import com.wallet.domain.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResponseDTOMapper {

    UserResponseDTO fron(User user);

    WalletResponseDTO from(Wallet wallet);

    @Mapping(target = "wallet", source = "wallet.id")
    TransactionResponseDTO from(Transaction transaction);

}
