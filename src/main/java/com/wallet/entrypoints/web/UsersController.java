package com.wallet.entrypoints.web;

import com.wallet.entrypoints.web.dto.request.CreateUserRequestDTO;
import com.wallet.entrypoints.web.dto.request.RequestDTOMapper;
import com.wallet.entrypoints.web.dto.response.ErrorDTO;
import com.wallet.entrypoints.web.dto.response.ResponseDTOMapper;
import com.wallet.entrypoints.web.dto.response.UserResponseDTO;
import com.wallet.idempotency.Idempotent;
import com.wallet.idempotency.IdempotentArg;
import com.wallet.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.wallet.idempotency.IdempotencyConstants.DEFAULT_HEADER;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "Users", description = "Users management API")
public class UsersController {

    private final UserService userService;
    private final RequestDTOMapper requestDTOMapper;
    private final ResponseDTOMapper responseDTOMapper;

    public UsersController(UserService userService, RequestDTOMapper requestDTOMapper, ResponseDTOMapper responseDTOMapper) {
        this.userService = userService;
        this.requestDTOMapper = requestDTOMapper;
        this.responseDTOMapper = responseDTOMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Idempotent
    public UserResponseDTO createUser(
            @Parameter(description = "Correlation ID for request tracking") @RequestHeader(DEFAULT_HEADER) final String correlationId,
            @IdempotentArg @RequestBody @Validated CreateUserRequestDTO userDto) {
        final var user = requestDTOMapper.toDomain(userDto);
        return responseDTOMapper.fron(userService.createUser(user));
    }

    @GetMapping("/{document}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user by document", description = "Retrieves a user by their document number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
    })
    public UserResponseDTO getUser(@PathVariable("document") String document) {
        final var user = userService.findByDocument(document);
        return responseDTOMapper.fron(user);
    }

}
