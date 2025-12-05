package com.example.md_08_ungdungfivestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.md_08_ungdungfivestore.models.AuthResponse;
import com.example.md_08_ungdungfivestore.models.LoginRequest;
import com.example.md_08_ungdungfivestore.services.DangNhapApiClient;
import com.example.md_08_ungdungfivestore.services.ApiService;
import com.example.md_08_ungdungfivestore.utils.AuthManager; // ⭐ IMPORT AuthManager

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhap extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private TextView btnLogin, tvRegister;
    private TextView tvResetDangNhap;
    // private SharedPreferences sharedPreferences; // ❌ KHÔNG CẦN DÙNG TRỰC TIẾP NỮA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        // Gán biến với layout
        edtEmail = findViewById(R.id.edtEmailDangNhap);
        edtPassword = findViewById(R.id.matKhauDangNhapTextInputEditText);
        btnLogin = findViewById(R.id.nutDangnhapvSignInTextView);
        tvRegister = findViewById(R.id.tvRegisterDangNhap);
        tvResetDangNhap = findViewById(R.id.tvResetDangNhap);

        // sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE); // ❌ Bỏ qua dòng này

        // Xử lý sự kiện Đăng nhập
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(DangNhap.this, "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        // Xử lý sự kiện Đăng ký
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(DangNhap.this, ManDangKy.class));
        });

        // XỬ LÝ SỰ KIỆN ĐỔI MẬT KHẨU (QUÊN MẬT KHẨU)
        tvResetDangNhap.setOnClickListener(v -> {
            startActivity(new Intent(DangNhap.this, com.example.md_08_ungdungfivestore.RequestOtpActivity.class));
        });
    }

    private void loginUser(String email, String password){
        ApiService apiService = DangNhapApiClient.getClient().create(ApiService.class);
        LoginRequest request = new LoginRequest(email, password);

        Call<AuthResponse> call = apiService.login(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d("DangNhap", "Response code: " + response.code());
                if(response.isSuccessful() && response.body() != null){
                    AuthResponse auth = response.body();
                    Log.d("DangNhap", "Success: " + auth.isSuccess() + ", Message: " + auth.getMessage());
                    if(auth.isSuccess()){

                        // ⭐ SỬA LỖI LƯU TOKEN: Dùng AuthManager để đồng bộ với Retrofit Interceptor
                        AuthManager.saveToken(DangNhap.this, auth.getToken());

                        Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        // Chuyển sang MainActivity
                        Intent intent = new Intent(DangNhap.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(DangNhap.this, auth.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("DangNhap", "Response thất bại: " + response.errorBody());
                    Toast.makeText(DangNhap.this, "Đăng nhập thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("DangNhap", "Lỗi network: " + t.getMessage());
                Toast.makeText(DangNhap.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}