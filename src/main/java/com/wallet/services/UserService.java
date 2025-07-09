package com.wallet.services;

import com.wallet.domain.User;
import com.wallet.exceptions.NotFoundException;
import com.wallet.services.ports.UserFindPort;
import com.wallet.services.ports.UserSavePort;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserSavePort userSavePort;
    private final UserFindPort userFindPort;

    public UserService(UserSavePort userSavePort, UserFindPort userFindPort) {
        this.userSavePort = userSavePort;
        this.userFindPort = userFindPort;
    }

    public User createUser(User user) {
        if(user == null) {
            throw new IllegalArgumentException("Invalid user.");
        }

        final var existingUser = userFindPort.findByDocument(user.document());
        if(existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists.");
        }

        return userSavePort.save(user);
    }

    public User findByDocument(String document) {
        return userFindPort.findByDocument(document)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

}
