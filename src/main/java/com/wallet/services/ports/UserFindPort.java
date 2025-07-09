package com.wallet.services.ports;

import com.wallet.domain.User;

import java.util.Optional;

public interface UserFindPort {
    Optional<User> findByDocument(String document);
}
