package com.example.md_08_ungdungfivestore;
// Đảm bảo bạn đang sử dụng đúng package gốc, nếu file của bạn nằm trong package con, hãy điều chỉnh lại.

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Cần Import cho Retrofit
import com.example.md_08_ungdungfivestore.models.AuthResponse;
import com.example.md_08_ungdungfivestore.models.EmailRequest;
import com.example.md_08_ungdungfivestore.services.DangNhapApiClient;
import com.example.md_08_ungdungfivestore.services.AuthApiService; // ⭐ Dùng AuthApiService ⭐

// Cần Import cho View
import com.google.android.material.textfield.TextInputEditText; // Giả sử đã sửa XML thành TextInputEditText

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestOtpActivity extends AppCompatActivity {

    // Khai báo đúng kiểu TextInputEditText (sau khi đã sửa XML)
    private TextInputEditText edtEmail;
    private TextView btnRequestOtp;

    // ⭐ Khai báo VerifyOtpActivity ở đây nếu nó nằm ở package gốc ⭐
    // public class VerifyOtpActivity extends AppCompatActivity { ... }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_otp);

        // Lỗi ClassCastException đã được sửa nhờ dùng findViewById với TextInputEditText
        edtEmail = findViewById(R.id.edtEmailRequestOtp);
        btnRequestOtp = findViewById(R.id.btnRequestOtp);

        btnRequestOtp.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
                return;
            }

            requestOtpForPasswordReset(email);
        });
    }

    private void requestOtpForPasswordReset(String email) {

        // ⭐ SỬA LỖI CANNOT FIND SYMBOL: Đổi từ ApiService sang AuthApiService ⭐
        AuthApiService apiService = DangNhapApiClient.getClient().create(AuthApiService.class);
        EmailRequest request = new EmailRequest(email);

        Call<AuthResponse> call = apiService.requestPasswordOtp(request);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                Log.e("API_OTP", "HTTP Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {

                    // Trường hợp thành công (ví dụ: code 200)
                    Toast.makeText(RequestOtpActivity.this,
                            "Thành công: " + response.body().getMessage(),
                            Toast.LENGTH_LONG).show();

                    // ⭐ ĐÃ THÊM LỆNH CHUYỂN MÀN HÌNH ⭐
                    // Cần đảm bảo VerifyOtpActivity nằm trong package gốc hoặc import đúng
                    Intent intent = new Intent(RequestOtpActivity.this, VerifyOtpActivity.class);
                    intent.putExtra("EMAIL", email);
                    startActivity(intent);

                } else {
                    // Xử lý lỗi từ server (400, 404, 500)
                    String errorMessage = "Đã xảy ra lỗi, vui lòng thử lại.";
                    try {
                        if (response.errorBody() != null) {
                            String errorJson = response.errorBody().string();
                            Log.e("API_OTP", "Error Body: " + errorJson);
                            errorMessage = "Email không tồn tại hoặc lỗi gửi OTP (Code: " + response.code() + ")";
                        }
                    } catch (IOException e) {
                        Log.e("API_OTP", "Lỗi đọc error body: " + e.getMessage());
                    }
                    Toast.makeText(RequestOtpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Lỗi kết nối mạng/Server không chạy
                Log.e("API_OTP", "Lỗi mạng/Kết nối: " + t.getMessage());
                Toast.makeText(RequestOtpActivity.this, "Lỗi kết nối Server: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}