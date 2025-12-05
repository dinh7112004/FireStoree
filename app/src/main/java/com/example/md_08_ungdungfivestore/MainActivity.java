package com.example.md_08_ungdungfivestore;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.md_08_ungdungfivestore.fragments.GioHangFragment;
import com.example.md_08_ungdungfivestore.fragments.TrangChuFragment;
//import com.example.md_08_ungdungfivestore.fragments.YeuThichFragment;
import com.example.md_08_ungdungfivestore.fragments.YeuThichFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    FrameLayout layout;
    BottomNavigationView menu;
    Toolbar toolbar;
    TextView tieuDe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhXa();

        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            taiFragment(new TrangChuFragment());
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        menu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navTrangChu) {
                taiFragment(new TrangChuFragment());
                tieuDe.setText("Trang Chủ");
            }
            if (item.getItemId() == R.id.navYeuThich) {
                taiFragment(new YeuThichFragment());
                tieuDe.setText("Yêu thích");
            }
            if (item.getItemId() == R.id.navGioHang) {
                taiFragment(new GioHangFragment());
                tieuDe.setText("Giỏ hàng");
            }
            if (item.getItemId() == R.id.navNguoiDung) {
                taiFragment(new YeuThichFragment());
                tieuDe.setText("Người dùng");
            }
            return true;
        });
    }

    public void anhXa() {
        layout = findViewById(R.id.trangChuFrameLayout);
        menu = findViewById(R.id.menuTrangChuBottom);
        toolbar = findViewById(R.id.toolBarTrangChu);
        tieuDe = findViewById(R.id.tieuDeTextView);
    }

    public void taiFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.trangChuFrameLayout, fragment).commit();
    }
}
