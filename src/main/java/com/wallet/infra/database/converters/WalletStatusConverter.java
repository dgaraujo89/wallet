package com.wallet.infra.database.converters;

import com.wallet.domain.WalletStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WalletStatusConverter implements AttributeConverter<WalletStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(WalletStatus status) {
        if(status == null) {
            throw new IllegalArgumentException("Wallet status can't be null");
        }

        return status.getValue();
    }

    @Override
    public WalletStatus convertToEntityAttribute(Integer value) {
        return WalletStatus.resolve(value);
    }

}
