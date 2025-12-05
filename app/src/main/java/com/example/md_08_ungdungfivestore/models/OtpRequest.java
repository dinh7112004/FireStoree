package com.example.md_08_ungdungfivestore.models;

public class OtpRequest {
    private String email;
    private String otp;
    private String full_name;
    private String password;

    public OtpRequest(String email, String otp, String full_name, String password) {
        this.email = email;
        this.otp = otp;
        this.full_name = full_name;
        this.password = password;
    }

    // getters
    public String getEmail() { return email; }
    public String getOtp() { return otp; }
    public String getFull_name() { return full_name; }
    public String getPassword() { return password; }
}
