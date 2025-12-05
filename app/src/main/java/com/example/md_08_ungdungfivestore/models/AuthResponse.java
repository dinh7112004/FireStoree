package com.example.md_08_ungdungfivestore.models;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private User user;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public User getUser() { return user; }
}
