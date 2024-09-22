package com.sahil.kitchensink.service;

import com.sahil.kitchensink.model.User;
import com.sahil.kitchensink.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRespository userRespository;

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRespository.findById(email).orElseThrow(() -> new UsernameNotFoundException("email or password is invalid"));

    }

    public User saveUser(User user) {
        return userRespository.save(user);
    }
}
