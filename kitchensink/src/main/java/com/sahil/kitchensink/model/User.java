package com.sahil.kitchensink.model;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Builder
public class User implements UserDetails {

    @Id
    @Email
    private String email;
    private String password;

    private List<GrantedAuthority> authorityList;

    public User(String email, String password, List<GrantedAuthority> authorityList) {
        this.email = email;
        this.password = password;
        this.authorityList = authorityList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList == null ? List.of() : authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
