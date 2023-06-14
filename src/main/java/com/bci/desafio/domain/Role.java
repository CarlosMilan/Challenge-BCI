package com.bci.desafio.domain;

public enum Role {
    ROLE_ADMIN("ADMIN"), ROLE_USER("USER");
    private final String value;
    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
