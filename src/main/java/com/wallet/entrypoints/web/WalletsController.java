package com.wallet.entrypoints.web;

import com.github.f4b6a3.uuid.UuidCreator;
import com.wallet.entrypoints.web.dto.request.TransactionRequestDTO;
import com.wallet.entrypoints.web.dto.request.TransferRequestDTO;
import com.wallet.entrypoints.web.dto.request.WalletRequestDTO;
import com.wallet.entrypoints.web.dto.response.ErrorDTO;
import com.wallet.entrypoints.web.dto.response.ResponseDTOMapper;
import com.wallet.entrypoints.web.dto.response.TransactionResponseDTO;
import com.wallet.entrypoints.web.dto.response.TransferResponseDTO;
import com.wallet.entrypoints.web.dto.response.WalletBalanceResponseDTO;
import com.wallet.entrypoints.web.dto.response.WalletResponseDTO;
import com.wallet.idempotency.Idempotent;
import com.wallet.idempotency.IdempotentArg;
import com.wallet.services.TransactionService;
import com.wallet.services.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

import static com.wallet.idempotency.IdempotencyConstants.DEFAULT_HEADER;

@RestController
@RequestMapping("/v1/wallets")
@Tag(name = "Wallets", description = "Endpoints for managing wallets and transactions")
public class WalletsController {

    private final WalletService walletService;
    private final TransactionService transactionService;
    private final ResponseDTOMapper responseDTOMapper;

    public WalletsController(WalletService walletService,
                             TransactionService transactionService,
                             ResponseDTOMapper responseDTOMapper) {
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.responseDTOMapper = responseDTOMapper;
    }

    @Operation(summary = "Create a new wallet", description = "Creates a new wallet with the specified document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wallet created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Idempotent
    public WalletResponseDTO create(
            @Parameter(description = "Correlation ID for request tracking") @RequestHeader(DEFAULT_HEADER) final String correlationId,
            @Parameter(description = "Wallet creation request") @IdempotentArg final WalletRequestDTO walletRequestDTO) {
        final var wallet = walletService.create(walletRequestDTO.document());
        return responseDTOMapper.from(wallet);
    }

    @Operation(summary = "Get wallet by ID", description = "Retrieves wallet information by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wallet found"),
            @ApiResponse(responseCode = "404", description = "Wallet not found", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
    })
    @GetMapping("/{id}")
    public WalletResponseDTO getById(@Parameter(description = "Wallet ID") @PathVariable final String id) {
        final var wallet = walletService.findById(UuidCreator.fromString(id));
        return responseDTOMapper.from(wallet);
    }

    @Operation(summary = "Deposit funds", description = "Deposits funds into the specified wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Deposit successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Wallet not found", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "409", description = "Transaction with provided correlation ID already exists", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
    })
    @PostMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    @Idempotent
    public TransactionResponseDTO deposit(
            @Parameter(description = "Wallet ID") @IdempotentArg @PathVariable final String id,
            @Parameter(description = "Correlation ID for request tracking") @RequestHeader(DEFAULT_HEADER) final String correlationId,
            @Parameter(description = "Transaction details") @IdempotentArg @RequestBody final TransactionRequestDTO transactionRequestDTO) {
        final var transaction = transactionService.deposit(UuidCreator.fromString(id),
                UuidCreator.fromString(correlationId),
                transactionRequestDTO.amount());

        return responseDTOMapper.from(transaction);
    }

    @Operation(summary = "Withdraw funds", description = "Withdraws funds from the specified wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Wallet not found", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "409", description = "Transaction with provided correlation ID already exists", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
    })
    @PostMapping("/{id}/withdraw")
    @ResponseStatus(HttpStatus.CREATED)
    @Idempotent
    public TransactionResponseDTO withdraw(
            @Parameter(description = "Wallet ID") @IdempotentArg @PathVariable final String id,
            @Parameter(description = "Correlation ID for request tracking") @RequestHeader(DEFAULT_HEADER) final String correlationId,
            @Parameter(description = "Transaction details") @IdempotentArg @RequestBody final TransactionRequestDTO transactionRequestDTO) {
        final var transaction = transactionService.withdraw(UuidCreator.fromString(id),
                UuidCreator.fromString(correlationId),
                transactionRequestDTO.amount());

        return responseDTOMapper.from(transaction);
    }

    @Operation(summary = "Transfer funds", description = "Transfers funds between wallets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Wallet not found", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "409", description = "Transaction with provided correlation ID already exists", content = @Content(
                    schema = @Schema(implementation = ErrorDTO.class))),
    })
    @PostMapping("/{id}/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    @Idempotent
    public TransferResponseDTO transfer(
            @Parameter(description = "Source wallet ID") @IdempotentArg @PathVariable final String id,
            @Parameter(description = "Correlation ID for request tracking") @RequestHeader(DEFAULT_HEADER) final String correlationId,
            @Parameter(description = "Transfer details") @IdempotentArg @RequestBody final TransferRequestDTO transferRequestDTO) {

        final var transfer = transactionService.transfer(UuidCreator.fromString(id),
                UuidCreator.fromString(transferRequestDTO.wallet()),
                UuidCreator.fromString(correlationId),
                transferRequestDTO.amount());

        return TransferResponseDTO.builder()
                .correlationId(transfer.debit().correlationId())
                .amount(transferRequestDTO.amount())
                .createdAt(transfer.createdAt())
                .debitTransaction(TransactionResponseDTO.builder()
                        .id(transfer.debit().id())
                        .wallet(UuidCreator.fromString(id))
                        .build())
                .creditTransaction(TransactionResponseDTO.builder()
                        .id(transfer.credit().id())
                        .wallet(UuidCreator.fromString(transferRequestDTO.wallet()))
                        .build())
                .build();
    }

    @Operation(summary = "Get wallet balance", description = "Retrieves wallet balance by its ID and optional dateTime")
    @ApiResponse(responseCode = "200", description = "Balance retrieved successfully")
    @GetMapping("/{id}/balance")
    public WalletBalanceResponseDTO getBalance(
            @Parameter(description = "Wallet ID") @PathVariable final String id,
            @Parameter(description = "Optional date time to get historical balance")
            @RequestParam(required = false) final ZonedDateTime dateTime) {
        return responseDTOMapper.from(walletService.balance(UuidCreator.fromString(id), dateTime));
    }

}
