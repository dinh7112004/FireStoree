package com.example.md_08_ungdungfivestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManThongTinDonHang extends AppCompatActivity {
    private TextView thoiGianDuKienTextView, tenNguoiNhanTextView, diaChiTextView;
    private TextView tenSanPhamTextView, tongTienSanPhamTextView, huyDonHangButton, lienHeShopButton;
    private ImageView anhDonHangImageView;
    private ImageButton quayLaiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man_thong_tin_don_hang);
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

        lienHeShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lienHeIntent = new Intent(ManThongTinDonHang.this, ManLienHe.class);
                startActivity(lienHeIntent);
            }
        });
    }

    private void anhXa() {
        thoiGianDuKienTextView = findViewById(R.id.thoiGianDuKienTextView);
        tenNguoiNhanTextView = findViewById(R.id.tenNguoiNhanTextView);
        diaChiTextView = findViewById(R.id.diaChiTextView);
        tenSanPhamTextView = findViewById(R.id.tenSanPhamTextView);
        tongTienSanPhamTextView = findViewById(R.id.tongTienSanPhamTextView);
        huyDonHangButton = findViewById(R.id.huyDonHangButton);
        lienHeShopButton = findViewById(R.id.lienHeShopButton);
        anhDonHangImageView = findViewById(R.id.anhDonHangImageView);
        quayLaiBtn = findViewById(R.id.quayLaiBtn);
    }
}