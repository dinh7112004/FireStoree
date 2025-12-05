package com.example.md_08_ungdungfivestore.services;

import com.example.md_08_ungdungfivestore.models.AuthResponse;
import com.example.md_08_ungdungfivestore.models.RegisterRequest;
import com.example.md_08_ungdungfivestore.models.RegisterResponse;
import com.example.md_08_ungdungfivestore.models.OtpRequest;
import com.example.md_08_ungdungfivestore.models.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    // Register trả về RegisterResponse
    @POST("register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    // Verify OTP trả về AuthResponse
    @POST("verify-otp")
    Call<AuthResponse> verifyOtp(@Body OtpRequest request);

    // Login trả về AuthResponse
    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest request);
}
