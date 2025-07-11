package com.wallet.entrypoints.web.dto.request;

import com.wallet.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestDTOMapper {

    @Mapping(target = "createdAt",
            expression = "java(java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC))")
    User toDomain(CreateUserRequestDTO createUserRequestDTO);

}
