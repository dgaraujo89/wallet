package com.wallet.entrypoints.web.dto.response;

import com.wallet.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserResponseDTOMapper {

    UserResponseDTO from(User user);

}
