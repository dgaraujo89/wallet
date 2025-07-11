package com.wallet.entrypoints.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.f4b6a3.uuid.UuidCreator;
import com.wallet.domain.Transaction;
import com.wallet.domain.Transfer;
import com.wallet.domain.User;
import com.wallet.domain.Wallet;
import com.wallet.domain.WalletBalance;
import com.wallet.domain.WalletStatus;
import com.wallet.entrypoints.web.dto.request.TransactionRequestDTO;
import com.wallet.entrypoints.web.dto.request.TransferRequestDTO;
import com.wallet.entrypoints.web.dto.request.WalletRequestDTO;
import com.wallet.entrypoints.web.dto.response.ResponseDTOMapper;
import com.wallet.entrypoints.web.dto.response.ResponseDTOMapperImpl;
import com.wallet.exceptions.NotFoundException;
import com.wallet.idempotency.IdempotencyConstants;
import com.wallet.services.TransactionService;
import com.wallet.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WalletsControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private WalletService walletService;

    @Mock
    private TransactionService transactionService;

    private final ResponseDTOMapper responseDTOMapper = new ResponseDTOMapperImpl();

    private TransactionRequestDTO transactionRequestDTO;

    private TransferRequestDTO transferRequestDTO;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        var controller = new WalletsController(walletService, transactionService, responseDTOMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        transactionRequestDTO = new TransactionRequestDTO(BigDecimal.valueOf(100.00));
        transferRequestDTO = new TransferRequestDTO(BigDecimal.valueOf(50.00), UuidCreator.getTimeOrderedEpoch().toString());

        wallet = Wallet.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .user(User.builder()
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
                        .build())
                .balance(BigDecimal.TEN)
                .status(WalletStatus.OPEN)
                .createdAt(ZonedDateTime.now(ZoneOffset.UTC).minusDays(30))
                .build();
    }

    @Test
    void shouldCreateWalletSuccessfully() throws Exception {
        var request = new WalletRequestDTO("12345678900");
        when(walletService.create(anyString())).thenReturn(wallet);

        mockMvc.perform(post("/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(IdempotencyConstants.DEFAULT_HEADER, UuidCreator.getTimeOrderedEpoch()))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetWalletByIdSuccessfully() throws Exception {
        when(walletService.findById(any())).thenReturn(wallet);

        mockMvc.perform(get("/v1/wallets/{id}", wallet.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenWalletIdDoesNotExist() throws Exception {
        when(walletService.findById(any())).thenThrow(new NotFoundException("Wallet not found"));

        mockMvc.perform(get("/v1/wallets/{id}", UuidCreator.getTimeOrderedEpoch()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Wallet not found"));
    }

    @Test
    void shouldDepositSuccessfully() throws Exception {
        mockMvc.perform(post("/v1/wallets/{id}/deposit", UuidCreator.getTimeOrderedEpoch())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequestDTO))
                        .header(IdempotencyConstants.DEFAULT_HEADER, UuidCreator.getTimeOrderedEpoch()))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldWithdrawSuccessfully() throws Exception {
        mockMvc.perform(post("/v1/wallets/{id}/withdraw", UuidCreator.getTimeOrderedEpoch())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequestDTO))
                        .header(IdempotencyConstants.DEFAULT_HEADER, UuidCreator.getTimeOrderedEpoch()))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetBalanceSuccessfully() throws Exception {
        var walletBalance = new WalletBalance(BigDecimal.valueOf(100.00), ZonedDateTime.now(ZoneOffset.UTC));
        when(walletService.balance(any(), any())).thenReturn(walletBalance);

        mockMvc.perform(get("/v1/wallets/{id}/balance", UuidCreator.getTimeOrderedEpoch()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100.00));
    }

    @Test
    void shouldTransferSuccessfully() throws Exception {
        var correlationId = UuidCreator.getTimeOrderedEpoch();
        var transfer = Transfer.builder()
                .debit(Transaction.builder()
                        .correlationId(correlationId)
                        .wallet(Wallet.builder()
                                .id(UuidCreator.getTimeOrderedEpoch())
                                .build())
                        .build())
                .credit(Transaction.builder()
                        .correlationId(correlationId)
                        .wallet(Wallet.builder()
                                .id(UuidCreator.getTimeOrderedEpoch())
                                .build())
                        .build())
                .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();

        when(transactionService.transfer(any(), any(), any(), any())).thenReturn(transfer);

        mockMvc.perform(post("/v1/wallets/{id}/transfer", UuidCreator.getTimeOrderedEpoch())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequestDTO))
                        .header(IdempotencyConstants.DEFAULT_HEADER, UuidCreator.getTimeOrderedEpoch()))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.correlationId").value(correlationId.toString()))
                .andExpect(jsonPath("$.amount").value(50.00));
    }

}