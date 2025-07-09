package com.wallet.infra.database;

import com.wallet.domain.User;
import com.wallet.infra.database.mappers.UserMapper;
import com.wallet.infra.database.repositories.UserRepository;
import com.wallet.services.ports.UserFindPort;
import com.wallet.services.ports.UserSavePort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDao implements UserSavePort, UserFindPort {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDao(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        var entity = userMapper.toEntity(user);
        entity = userRepository.save(entity);
        return  userMapper.toDomain(entity);
    }

    @Override
    public Optional<User> findByDocument(String document) {
        return userRepository.findByDocument(document)
                .map(userMapper::toDomain);
    }

}
