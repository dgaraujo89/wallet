package com.wallet.entrypoints.web.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WalletRequestDTO(@NotEmpty @Size(min = 11, max = 14) String document) {
}
