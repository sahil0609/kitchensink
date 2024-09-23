package com.sahil.kitchensink;

import com.sahil.kitchensink.enums.Roles;
import com.sahil.kitchensink.model.User;
import com.sahil.kitchensink.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminCreater {

    private final PasswordEncoder encoder;
    private final UserService userService;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;


    @EventListener(ApplicationReadyEvent.class)
    public void createAdmin() {

        User user = User.builder()
                .email(adminEmail)
                .password(encoder.encode(adminPassword))
                .authorityList(List.of(new SimpleGrantedAuthority(Roles.ADMIN.getValue())))
                .build();

        log.info("Creating admin user with email: {}", adminEmail);
        userService.saveUser(user);
    }
}
