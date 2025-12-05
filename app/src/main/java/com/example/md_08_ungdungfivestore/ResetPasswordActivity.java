package com.example.md_08_ungdungfivestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.md_08_ungdungfivestore.models.AuthResponse;
import com.example.md_08_ungdungfivestore.models.ResetPasswordRequest;
import com.example.md_08_ungdungfivestore.services.DangNhapApiClient;
import com.example.md_08_ungdungfivestore.services.AuthApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText edtNewPassword, edtConfirmPassword;
    private Button btnFinalReset;
    private AuthApiService apiService;
    private String email, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Nhận Email và OTP từ màn hình trước
        email = getIntent().getStringExtra("EMAIL");
        otp = getIntent().getStringExtra("OTP");

        if (email == null || otp == null) {
            Toast.makeText(this, "Lỗi dữ liệu. Vui lòng thử lại từ đầu.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtNewPassword = findViewById(R.id.edtNewPasswordReset);
        edtConfirmPassword = findViewById(R.id.edtConfirmPasswordReset);
        btnFinalReset = findViewById(R.id.btnFinalReset);

        apiService = DangNhapApiClient.getClient().create(AuthApiService.class);

        btnFinalReset.setOnClickListener(v -> attemptResetPassword());
    }

    private void attemptResetPassword() {
        String newPass = edtNewPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        resetPassword(email, otp, newPass);
    }

    private void resetPassword(String email, String otp, String newPass) {
        ResetPasswordRequest request = new ResetPasswordRequest(email, otp, newPass);

        Call<AuthResponse> call = apiService.resetPassword(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(ResetPasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_LONG).show();

                    // Chuyển về màn hình Đăng nhập
                    Intent intent = new Intent(ResetPasswordActivity.this, DangNhap.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    String message = response.body() != null ? response.body().getMessage() : "Mã OTP không đúng, đã hết hạn hoặc lỗi server.";
                    Log.e("ResetPassword", "Reset failed: " + message);
                    Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("ResetPassword", "Lỗi network: " + t.getMessage());
                Toast.makeText(ResetPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}