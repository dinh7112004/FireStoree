package com.example.md_08_ungdungfivestore.services;

import com.example.md_08_ungdungfivestore.models.AuthResponse;
import com.example.md_08_ungdungfivestore.models.EmailRequest;
import com.example.md_08_ungdungfivestore.models.ResetPasswordRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {

    // ... (các phương thức login, register, v.v. cũ)

    // ---------------------------------------------------------------------------------------
    // ⭐ CÁC PHƯƠNG THỨC CHO LUỒNG QUÊN MẬT KHẨU (ĐÃ SỬA LỖI 404 và TRÙNG LẶP) ⭐

    /**
     * Bước 1: Yêu cầu server gửi OTP đến email.
     * Endpoint Server: POST /api/auth/forgot-password/request-otp
     * Endpoint Retrofit chỉ cần: forgot-password/request-otp (vì /api/auth đã có ở BASE_URL hoặc Server Routing)
     */
    @POST("forgot-password/request-otp")
    Call<AuthResponse> requestPasswordOtp(@Body EmailRequest request);

    /**
     * Bước 3: Xác thực OTP và Đặt mật khẩu mới.
     * Endpoint Server: POST /api/auth/forgot-password/reset
     */
    @POST("forgot-password/reset")
    Call<AuthResponse> resetPassword(@Body ResetPasswordRequest request);

    // Đã xóa phương thức requestOtp bị trùng lặp.

}