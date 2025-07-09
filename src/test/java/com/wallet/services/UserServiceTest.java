package com.wallet.services;

import com.wallet.builders.domain.UserBuilder;
import com.wallet.domain.User;
import com.wallet.exceptions.NotFoundException;
import com.wallet.services.ports.UserFindPort;
import com.wallet.services.ports.UserSavePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserSavePort userSavePort;

    @Mock
    private UserFindPort userFindPort;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void shouldCreateUserSuccessfully() {
        var user = UserBuilder.createUser();

        when(userFindPort.findByDocument(user.document())).thenReturn(Optional.empty());
        when(userSavePort.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.createUser(user));

        verify(userFindPort).findByDocument(user.document());
        verify(userSavePort).save(userCaptor.capture());

        assertEquals(user, userCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionWhenTryingToCreateExistingUser() {
        var user = UserBuilder.createUser();

        when(userFindPort.findByDocument(user.document())).thenReturn(Optional.of(user));

        var exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        assertEquals("User already exists.", exception.getMessage());

        verify(userFindPort).findByDocument(user.document());
        verify(userSavePort, never()).save(any());
    }

    @Test
    void shouldFindUserSuccessfully() {
        var user = UserBuilder.createUser();

        when(userFindPort.findByDocument(user.document())).thenReturn(Optional.of(user));

        var userFound = assertDoesNotThrow(() -> userService.findByDocument(user.document()));

        assertEquals(user, userFound);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        var document = "12345678900";

        when(userFindPort.findByDocument(document)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class, () -> userService.findByDocument(document));
        assertEquals("User not found.", exception.getMessage());

        verify(userFindPort).findByDocument(document);
    }

}