package com.wallet.entrypoints.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.f4b6a3.uuid.UuidCreator;
import com.wallet.builders.dto.CreateUserRequestDTOBuilder;
import com.wallet.entrypoints.web.dto.request.CreateUserRequestDTO;
import com.wallet.entrypoints.web.dto.request.RequestDTOMapper;
import com.wallet.entrypoints.web.dto.request.RequestDTOMapperImpl;
import com.wallet.entrypoints.web.dto.response.ResponseDTOMapper;
import com.wallet.entrypoints.web.dto.response.ResponseDTOMapperImpl;
import com.wallet.exceptions.NotFoundException;
import com.wallet.idempotency.IdempotencyConstants;
import com.wallet.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    private final RequestDTOMapper requestDTOMapper = new RequestDTOMapperImpl();

    private final ResponseDTOMapper responseDTOMapper = new ResponseDTOMapperImpl();

    private CreateUserRequestDTO createUserRequestDTO;

    @BeforeEach
    void setUp() {
        var controller = new UsersController(userService, requestDTOMapper, responseDTOMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        createUserRequestDTO = CreateUserRequestDTOBuilder.builder()
                .name("John Doe")
                .email("john@gmail.com")
                .document("12345678900")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .complement("Apt 4B")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .country("USA")
                .build();
    }

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        when(userService.createUser(any())).thenReturn(requestDTOMapper.toDomain(createUserRequestDTO));

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequestDTO))
                        .header(IdempotencyConstants.DEFAULT_HEADER, UuidCreator.getTimeOrderedEpoch()))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenCreateUserWithInvalidData() throws Exception {
        createUserRequestDTO = CreateUserRequestDTOBuilder.builder().build();

        mockMvc.perform(post("/v1/users")
                        .header(IdempotencyConstants.DEFAULT_HEADER, UuidCreator.getTimeOrderedEpoch())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetUserSuccessfully() throws Exception {
        var user = requestDTOMapper.toDomain(createUserRequestDTO);

        when(userService.findByDocument(any())).thenReturn(user);

        mockMvc.perform(get("/v1/users/{document}", "12345678900"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.findByDocument(any())).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/v1/users/{document}", "12345678900"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

}