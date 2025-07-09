package com.wallet.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public record User(Long id, String name, String email, String document, LocalDate dateOfBirth,
                   String address,  String complement, String city, String state, String postalCode,
                   String country, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
}
