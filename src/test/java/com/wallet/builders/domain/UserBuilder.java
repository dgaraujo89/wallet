package com.wallet.builders.domain;

import com.wallet.domain.User;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class UserBuilder {

    public static User createUser() {
        return new User(null, "Maria", "maria@gmail.com", "87077184030", LocalDate.now().minusYears(30),
                "Av. Paulista, 4532", null, "Sao Paulo", "SP", "08123456", "Brasil",
                ZonedDateTime.now(ZoneOffset.UTC), null);
    }

}
