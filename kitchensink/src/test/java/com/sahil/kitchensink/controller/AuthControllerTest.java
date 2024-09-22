package com.sahil.kitchensink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahil.kitchensink.enums.Roles;
import com.sahil.kitchensink.model.User;
import com.sahil.kitchensink.model.UserDTO;
import com.sahil.kitchensink.repository.UserRespository;
import com.sahil.kitchensink.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRespository userRespository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRespository.deleteAll();
    }

    @Test
    void login_ReturnsOkResponse_WhenCredentialsAreValid() throws Exception {
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode("password12A"))
                .authorityList(List.of(new SimpleGrantedAuthority(Roles.USER.getValue())))
                .build();

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password12A");

        when(userRespository.findById(email)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/auth/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @Test
    void login_ReturnsUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("wrongPassword");

        mockMvc.perform(post("/auth/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_CreatesUser_WhenDataIsValid() throws Exception {
        String email = "test@example.com";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("Password1A");

        mockMvc.perform(post("/auth/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());

        ArgumentCaptor<User> userCapture = ArgumentCaptor.forClass(User.class);
        verify(userRespository).save(userCapture.capture());
        assertTrue(passwordEncoder.matches(userDTO.getPassword(), userCapture.getValue().getPassword()));
        assertTrue(userCapture.getValue().getUsername().matches(email));
        assertEquals(userCapture.getValue().getAuthorities().size(), 1);
    }

    @ParameterizedTest
    @MethodSource("invalidDataSource")
    void register_ReturnsBadRequest_WhenDataIsInvalid(String email, String password) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setPassword(password);

        mockMvc.perform(post("/auth/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidDataSource() {
        return Stream.of(
                Arguments.of("invalidEmail", "password"),
                Arguments.of("abcd@gmail.com", "pass1"),
                Arguments.of("abcd@gmail.com,", "password1fdafda")
        );
    }
}