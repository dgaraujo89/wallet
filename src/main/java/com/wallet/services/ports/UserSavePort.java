package com.wallet.services.ports;

import com.wallet.domain.User;

public interface UserSavePort {
    User save(User user);
}
