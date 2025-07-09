package com.wallet.entrypoints.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDTO(int status, String error, String message, Set<FieldErrorDTO> fields, ZonedDateTime timestamp) {

    public static ErrorDTOBuilder builder() {
        return new ErrorDTOBuilder();
    }

    public static class ErrorDTOBuilder {
        private int status;
        private String error;
        private String message;
        private Set<FieldErrorDTO> fields;

        public ErrorDTOBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ErrorDTOBuilder error(String error) {
            this.error = error;
            return this;
        }

        public ErrorDTOBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorDTOBuilder addField(String name,  String value) {
            if(fields == null) {
                fields = new HashSet<>();
            }

            fields.add(new FieldErrorDTO(name, value));
            return this;
        }

        public ErrorDTO build() {
            return new ErrorDTO(status, error, message, fields, ZonedDateTime.now(ZoneOffset.UTC));
        }

    }

    public record FieldErrorDTO(String name, String message) {}

}
