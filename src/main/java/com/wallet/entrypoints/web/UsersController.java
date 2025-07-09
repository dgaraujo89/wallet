package com.wallet.entrypoints.web;

import com.wallet.entrypoints.web.dto.request.CreateUserRequestDTO;
import com.wallet.entrypoints.web.dto.request.CreateUserRequestDTOMapper;
import com.wallet.entrypoints.web.dto.response.UserResponseDTO;
import com.wallet.entrypoints.web.dto.response.UserResponseDTOMapper;
import com.wallet.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UsersController {

    private final UserService userService;
    private final CreateUserRequestDTOMapper createUserRequestDTOMapper;
    private final UserResponseDTOMapper userResponseDTOMapper;

    public UsersController(UserService userService, CreateUserRequestDTOMapper createUserRequestDTOMapper, UserResponseDTOMapper userResponseDTOMapper) {
        this.userService = userService;
        this.createUserRequestDTOMapper = createUserRequestDTOMapper;
        this.userResponseDTOMapper = userResponseDTOMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@RequestBody @Validated CreateUserRequestDTO userDto) {
        final var user = createUserRequestDTOMapper.toDomain(userDto);
        return userResponseDTOMapper.from(userService.createUser(user));
    }

    @GetMapping("/{document}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO getUser(@PathVariable("document") String document) {
        final var user = userService.findByDocument(document);
        return userResponseDTOMapper.from(user);
    }

}
