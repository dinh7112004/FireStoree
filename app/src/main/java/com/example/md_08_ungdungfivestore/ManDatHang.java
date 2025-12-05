package com.example.md_08_ungdungfivestore;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ManDatHang extends AppCompatActivity {
    private TextView diaChiTxt, hoTenKhachHangTxt, soDienThoaiTxt, emailKhachHangTxt, tongSoTienTxt, nutThanhToanTxt;
    private ImageButton quayLaiBtn;
    private RadioButton thanhToanRadioBtn, thanhToanTruocRadioBtn;
    private RecyclerView danhSachMuaRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man_dat_hang);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhXa();

        quayLaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void anhXa() {
        diaChiTxt = findViewById(R.id.diaChiTxt);
        hoTenKhachHangTxt = findViewById(R.id.hoTenKhachHangTxt);

        nutThanhToanTxt = findViewById(R.id.nutThanhToanTxt);
        quayLaiBtn = findViewById(R.id.quayLaiBtn);
        thanhToanRadioBtn = findViewById(R.id.thanhToanRadioBtn);
        thanhToanTruocRadioBtn = findViewById(R.id.thanhToanTruocRadioBtn);
        danhSachMuaRecyclerView = findViewById(R.id.danhSachMuaRecyclerView);
    }
}