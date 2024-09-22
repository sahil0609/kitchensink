package com.sahil.kitchensink.validations;

import com.sahil.kitchensink.model.User;
import com.sahil.kitchensink.model.UserDTO;
import com.sahil.kitchensink.repository.UserRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UniqueEmailValidatorTest {

    @Mock
    private UserRespository userRespository;

    @InjectMocks
    private UniqueEmailValidator uniqueEmailValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isValid_ReturnsTrue_WhenEmailIsUnique() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("unique@example.com");

        when(userRespository.findById(userDTO.getEmail())).thenReturn(Optional.empty());

        boolean result = uniqueEmailValidator.isValid(userDTO, null);

        assertTrue(result);
    }

    @Test
    void isValid_ReturnsFalse_WhenEmailIsNotUnique() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("existing@example.com");
        User user = User.builder().email("existing@example.com").build();

        when(userRespository.findById(userDTO.getEmail())).thenReturn(Optional.of(user));

        boolean result = uniqueEmailValidator.isValid(userDTO, null);

        assertFalse(result);
    }

}