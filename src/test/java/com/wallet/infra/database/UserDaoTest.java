package com.wallet.infra.database;

import com.wallet.builders.domain.UserBuilder;
import com.wallet.infra.database.entities.UserEntity;
import com.wallet.infra.database.mappers.UserMapper;
import com.wallet.infra.database.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    @InjectMocks
    private UserDao userDao;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldSaveUserSuccessfully() {
        var user = UserBuilder.createUser();
        var userEntity = new UserEntity();

        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        var savedUser = userDao.save(user);

        assertNotNull(savedUser);
        assertEquals(user, savedUser);
        verify(userMapper).toEntity(user);
        verify(userRepository).save(userEntity);
        verify(userMapper).toDomain(userEntity);
    }

    @Test
    void shouldFindUserByDocumentSuccessfully() {
        var user = UserBuilder.createUser();
        var userEntity = new UserEntity();
        var document = "12345678900";

        when(userRepository.findByDocument(document)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        var foundUser = userDao.findByDocument(document);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository).findByDocument(document);
        verify(userMapper).toDomain(userEntity);
    }

}