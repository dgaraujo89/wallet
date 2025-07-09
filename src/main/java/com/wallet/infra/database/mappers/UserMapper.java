package com.wallet.infra.database.mappers;

import com.wallet.domain.User;
import com.wallet.infra.database.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserEntity toEntity(User user);

    User toDomain(UserEntity userEntity);

}
