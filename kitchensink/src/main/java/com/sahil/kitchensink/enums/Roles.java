package com.sahil.kitchensink.enums;

public enum Roles {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getValue() {
        return role;
    }
}
