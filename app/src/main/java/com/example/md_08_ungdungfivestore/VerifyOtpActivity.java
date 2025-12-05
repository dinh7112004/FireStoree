package com.example.md_08_ungdungfivestore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.services.AuthApiService;

// Activity này chỉ dùng để nhập OTP và chuyển tiếp dữ liệu
public class VerifyOtpActivity extends AppCompatActivity {
    private EditText edtOtp;
    private Button btnVerifyOtp;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        email = getIntent().getStringExtra("EMAIL");
        if (email == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy Email.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtOtp = findViewById(R.id.edtOtpCode);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = edtOtp.getText().toString().trim();
            if (otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển sang màn hình Đặt mật khẩu mới (ResetPasswordActivity)
            Intent intent = new Intent(VerifyOtpActivity.this, ResetPasswordActivity.class);
            intent.putExtra("EMAIL", email); // Chuyển Email
            intent.putExtra("OTP", otp);     // Chuyển OTP
            startActivity(intent);
            finish();
        });

        // LƯU Ý: Nếu server của bạn yêu cầu xác thực OTP riêng biệt,
        // bạn sẽ cần gọi API xác thực tại đây và chỉ chuyển sang màn hình 3 khi thành công.
        // Hiện tại, chúng ta gộp xác thực vào bước 3 (resetPassword).
    }
}