package com.example.md_08_ungdungfivestore.models;

// Dùng cho API gửi OTP
public class EmailRequest {
    private String email;

    public EmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }
}