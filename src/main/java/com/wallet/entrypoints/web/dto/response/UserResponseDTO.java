package com.wallet.entrypoints.web.dto.response;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public record UserResponseDTO(String name,
                              String email,
                              String document,
                              LocalDate dateOfBirth,
                              String address,
                              String complement,
                              String city,
                              String state,
                              String postalCode,
                              String country,
                              ZonedDateTime createdAt) {
}
