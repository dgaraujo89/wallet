package com.wallet.infra.database.mappers;

import com.wallet.domain.Transaction;
import com.wallet.infra.database.entities.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    TransactionEntity from(Transaction transaction);

}
