package com.example.md_08_ungdungfivestore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GuiMaXacNhan extends AppCompatActivity {


    private TextView nutXacNhanTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gui_ma_xac_nhan);


        nutXacNhanTextView=findViewById(R.id.nutXacNhanTextView);

        nutXacNhanTextView.setOnClickListener(v -> {
            Intent intent = new Intent(GuiMaXacNhan.this, DangNhap.class);
            startActivity(intent);
            finish(); // đóng màn xác nhận mã
        });




    }
}