package com.wallet.entrypoints.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserRequestDTO(@NotEmpty String name,
                                   @NotNull @Email String email,
                                   @NotEmpty @Size(min = 11, max = 14) String document,
                                   @NotNull LocalDate dateOfBirth,
                                   @NotEmpty String address,
                                   String complement,
                                   @NotEmpty String city,
                                   @NotEmpty @Size(max = 2) String state,
                                   @NotEmpty @Size(max = 8) String postalCode,
                                   @NotEmpty String country) {

}
