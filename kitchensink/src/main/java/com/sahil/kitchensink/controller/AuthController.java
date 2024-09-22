package com.sahil.kitchensink.controller;

import com.sahil.kitchensink.enums.Roles;
import com.sahil.kitchensink.filter.JwtTokenFilter;
import com.sahil.kitchensink.model.User;
import com.sahil.kitchensink.model.UserDTO;
import com.sahil.kitchensink.service.TokenService;
import com.sahil.kitchensink.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/auth/v1")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationProvider authenticationProvider;
    private final TokenService tokenService;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserDTO user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        authenticationProvider.authenticate(token);
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String jwtToken = tokenService.generateToken(userDetails);

        return ResponseEntity.ok().header(JwtTokenFilter.AUTHORIZATION_HEADER, jwtToken).build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid UserDTO user) {
        User userDetail = User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .authorityList(List.of(new SimpleGrantedAuthority(Roles.USER.getValue())))
                .build();

        userService.saveUser(userDetail);
    }

}
