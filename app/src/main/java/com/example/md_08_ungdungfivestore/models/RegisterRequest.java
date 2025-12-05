package com.example.md_08_ungdungfivestore.models;

public class RegisterRequest {
    private String full_name;
    private String email;
    private String password;

    public RegisterRequest(String full_name, String email, String password) {
        this.full_name = full_name;
        this.email = email;
        this.password = password;
    }

    // getters
    public String getFull_name() { return full_name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
