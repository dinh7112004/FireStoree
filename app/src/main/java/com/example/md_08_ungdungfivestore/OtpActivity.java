package com.example.md_08_ungdungfivestore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.md_08_ungdungfivestore.models.AuthResponse;
import com.example.md_08_ungdungfivestore.models.OtpRequest;
import com.example.md_08_ungdungfivestore.services.DangNhapApiClient;
import com.example.md_08_ungdungfivestore.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    EditText edtOtp;
    TextView btnVerify;

    String email, fullName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        email = getIntent().getStringExtra("email");
        fullName = getIntent().getStringExtra("full_name");
        password = getIntent().getStringExtra("password");

        edtOtp = findViewById(R.id.edtOtp);
        btnVerify = findViewById(R.id.btnVerify);

        btnVerify.setOnClickListener(v -> {
            String otp = edtOtp.getText().toString().trim();
            if(otp.isEmpty()){
                Toast.makeText(this, "Nhập mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyOtp(email, otp, fullName, password);
        });
    }

    private void verifyOtp(String email, String otp, String fullName, String password){
        ApiService apiService = DangNhapApiClient.getClient().create(ApiService.class);
        OtpRequest request = new OtpRequest(email, otp, fullName, password);

        Call<AuthResponse> call = apiService.verifyOtp(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(OtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if(response.body().isSuccess()){
                        startActivity(new Intent(OtpActivity.this, DangNhap.class));
                        finish();
                    }
                } else {
                    Toast.makeText(OtpActivity.this, "Xác thực OTP thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(OtpActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
