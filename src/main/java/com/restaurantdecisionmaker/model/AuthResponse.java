package com.restaurantdecisionmaker.model;

import lombok.Getter;

@Getter
public class AuthResponse {

    private String username;
    private String token;

    public AuthResponse(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
