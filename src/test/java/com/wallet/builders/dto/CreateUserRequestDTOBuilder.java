package com.wallet.builders.dto;

import com.wallet.entrypoints.web.dto.request.CreateUserRequestDTO;

import java.time.LocalDate;

public class CreateUserRequestDTOBuilder {

    private String name;
    private String email;
    private String document;
    private LocalDate dateOfBirth;
    private String address;
    private String complement;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private CreateUserRequestDTOBuilder() {
    }

    public static CreateUserRequestDTOBuilder builder() {
        return new CreateUserRequestDTOBuilder();
    }

    public CreateUserRequestDTOBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CreateUserRequestDTOBuilder email(String email) {
        this.email = email;
        return this;
    }

    public CreateUserRequestDTOBuilder document(String document) {
        this.document = document;
        return this;
    }

    public CreateUserRequestDTOBuilder dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public CreateUserRequestDTOBuilder address(String address) {
        this.address = address;
        return this;
    }

    public CreateUserRequestDTOBuilder complement(String complement) {
        this.complement = complement;
        return this;
    }

    public CreateUserRequestDTOBuilder city(String city) {
        this.city = city;
        return this;
    }

    public CreateUserRequestDTOBuilder state(String state) {
        this.state = state;
        return this;
    }

    public CreateUserRequestDTOBuilder postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public CreateUserRequestDTOBuilder country(String country) {
        this.country = country;
        return this;
    }

    public CreateUserRequestDTO build() {
        return new CreateUserRequestDTO(name, email, document, dateOfBirth,
                address, complement, city, state, postalCode, country);
    }
    
}
