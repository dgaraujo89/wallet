package com.wallet.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public record User(Long id, String name, String email, String document, LocalDate dateOfBirth,
                   String address,  String complement, String city, String state, String postalCode,
                   String country, ZonedDateTime createdAt, ZonedDateTime updatedAt) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
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
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder document(String document) {
            this.document = document;
            return this;
        }

        public Builder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder complement(String complement) {
            this.complement = complement;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(ZonedDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public User build() {
            return new User(id, name, email, document, dateOfBirth, address, complement,
                    city, state, postalCode, country, createdAt, updatedAt);
        }
    }

}
