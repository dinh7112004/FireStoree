package com.example.md_08_ungdungfivestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DanhGia extends AppCompatActivity {
    private ImageView anhSanPhamImg;
    private TextView tenSanPhamTextView, giaTienTextView, tenKhachHangTextView, diaChiTextView;
    private RatingBar danhGiaRatingBar;
    private EditText nhanXetEditText;
    private Button guiDanhGiaButton;
    private ImageButton quayLaiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_danh_gia);
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

        guiDanhGiaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void anhXa() {
        anhSanPhamImg = findViewById(R.id.anhSanPhamImg);
        tenSanPhamTextView = findViewById(R.id.tenSanPhamTextView);
        giaTienTextView = findViewById(R.id.giaTienTextView);
        tenKhachHangTextView = findViewById(R.id.tenKhachHangTextView);
        diaChiTextView = findViewById(R.id.diaChiTextView);
        danhGiaRatingBar = findViewById(R.id.danhGiaRatingBar);
        nhanXetEditText = findViewById(R.id.nhanXetEditText);
        guiDanhGiaButton = findViewById(R.id.guiDanhGiaButton);
        quayLaiBtn = findViewById(R.id.quayLaiBtn);
    }
}