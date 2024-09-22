package com.sahil.kitchensink.service;


import com.sahil.kitchensink.model.User;
import com.sahil.kitchensink.repository.UserRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(UserService.class)
class UserServiceTest {

    @MockBean
    private UserRespository userRespository;

    @Autowired
    private UserService userService;

    @Test
    void loadUserByUsername_ReturnsUser_WhenEmailExists() {
        String email = "test@example.com";
        User user = User.builder().email(email).build();

        when(userRespository.findById(email)).thenReturn(Optional.of(user));

        User result = userService.loadUserByUsername(email);

        assertEquals(user, result);
    }

    @Test
    void loadUserByUsername_ThrowsException_WhenEmailDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userRespository.findById(email)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }

    @Test
    void saveUser_ReturnsSavedUser() {
        User user = User.builder().email("test@exmaple.com").build();
        when(userRespository.save(user)).thenReturn(user);
        User result = userService.saveUser(user);
        assertEquals(user, result);
    }

}
