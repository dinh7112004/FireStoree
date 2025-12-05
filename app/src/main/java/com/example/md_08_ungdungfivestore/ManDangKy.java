package com.example.md_08_ungdungfivestore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.md_08_ungdungfivestore.models.RegisterRequest;
import com.example.md_08_ungdungfivestore.models.RegisterResponse;
import com.example.md_08_ungdungfivestore.services.DangNhapApiClient;
import com.example.md_08_ungdungfivestore.services.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManDangKy extends AppCompatActivity {

    EditText edtTen, edtEmail;
    TextInputEditText edtMatKhau, edtNhapLaiMatKhau;
    TextView nutDangKyTextView, dangNhapTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_dang_ky);

        // Khai báo đúng kiểu dữ liệu
        edtTen = findViewById(R.id.tenDangKyEditText);
        edtEmail = findViewById(R.id.edtEmailDangNhap);
        edtMatKhau = findViewById(R.id.matKhauTextInputEditText);
        edtNhapLaiMatKhau = findViewById(R.id.nhapLaiMatKhauTextInputEditText);

        nutDangKyTextView = findViewById(R.id.nutDangKyTextView);
        dangNhapTextView = findViewById(R.id.dangNhapTextView);

        // Chuyển sang màn đăng nhập
        dangNhapTextView.setOnClickListener(v -> {
            startActivity(new Intent(ManDangKy.this, DangNhap.class));
            finish();
        });

        // Nút đăng ký
        nutDangKyTextView.setOnClickListener(v -> {
            String ten = edtTen.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String matKhau = edtMatKhau.getText().toString().trim();
            String nhapLaiMatKhau = edtNhapLaiMatKhau.getText().toString().trim();

            if (ten.isEmpty() || email.isEmpty() || matKhau.isEmpty() || nhapLaiMatKhau.isEmpty()) {
                Toast.makeText(ManDangKy.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!matKhau.equals(nhapLaiMatKhau)) {
                Toast.makeText(ManDangKy.this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi API tạo OTP
            dangKyTaoOtp(ten, email, matKhau);
        });
    }

    private void dangKyTaoOtp(String ten, String email, String matKhau) {
        ApiService apiService = DangNhapApiClient.getClient().create(ApiService.class);

        RegisterRequest request = new RegisterRequest(ten, email, matKhau);

        Call<RegisterResponse> call = apiService.register(request);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ManDangKy.this, "OTP đã được gửi. Kiểm tra console để lấy mã", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManDangKy.this, OtpActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("full_name", ten);
                    intent.putExtra("password", matKhau);
                    startActivity(intent);
                } else {
                    Toast.makeText(ManDangKy.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(ManDangKy.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
