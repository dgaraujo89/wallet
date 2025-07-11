package com.wallet.infra.database.converters;

import com.wallet.domain.TransactionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransactionTypeConverter implements AttributeConverter<TransactionType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TransactionType type) {
        return type != null ? type.getValue() : null;
    }

    @Override
    public TransactionType convertToEntityAttribute(Integer value) {
        return value != null ? TransactionType.resolve(value) : null;
    }

}