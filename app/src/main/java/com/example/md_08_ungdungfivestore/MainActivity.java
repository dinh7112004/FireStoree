package com.example.md_08_ungdungfivestore;

import android.app.Activity; // ⭐ Cần import
import android.content.Intent; // Cần import
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher; // ⭐ Cần import
import androidx.activity.result.contract.ActivityResultContracts; // ⭐ Cần import
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.md_08_ungdungfivestore.fragments.GioHangFragment;
import com.example.md_08_ungdungfivestore.fragments.TrangCaNhanFragment;
import com.example.md_08_ungdungfivestore.fragments.TrangChuFragment;
import com.example.md_08_ungdungfivestore.fragments.YeuThichFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.widget.Toast; // Cần import

public class MainActivity extends AppCompatActivity {
    FrameLayout layout;
    BottomNavigationView menu;
    Toolbar toolbar;
    TextView tieuDe;

    // ⭐ Khai báo các Fragment là biến thành viên
    private final GioHangFragment gioHangFragment = new GioHangFragment();
    private final TrangChuFragment trangChuFragment = new TrangChuFragment();
    private final YeuThichFragment yeuThichFragment = new YeuThichFragment();
    private final TrangCaNhanFragment trangCaNhanFragment = new TrangCaNhanFragment();

    // ⭐ Khai báo và khởi tạo Activity Result Launcher
    private final ActivityResultLauncher<Intent> checkoutResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Kiểm tra mã kết quả trả về từ CheckoutActivity
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Lấy Fragment hiện tại
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.trangChuFrameLayout);

                    // Nếu Fragment hiện tại là GioHangFragment (Đang hiển thị Giỏ hàng)
                    if (currentFragment instanceof GioHangFragment) {
                        // ⭐ GỌI HÀM TẢI LẠI GIỎ HÀNG để làm trống giao diện
                        ((GioHangFragment) currentFragment).fetchCartItems();
                        Toast.makeText(this, "Giỏ hàng đã được làm mới sau khi thanh toán.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

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
            taiFragment(trangChuFragment); // Dùng biến đã khai báo
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        menu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navTrangChu) {
                taiFragment(trangChuFragment);
                tieuDe.setText("Trang Chủ");
            }
            if (item.getItemId() == R.id.navYeuThich) {
                taiFragment(yeuThichFragment);
                tieuDe.setText("Yêu thích");
            }
            if (item.getItemId() == R.id.navGioHang) {
                taiFragment(gioHangFragment); // Dùng biến đã khai báo
                tieuDe.setText("Giỏ hàng");
            }
            if (item.getItemId() == R.id.navNguoiDung) {
                taiFragment(trangCaNhanFragment);
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

    // ⭐ PHƯƠNG THỨC GETTER CHO LAUNCHER
    public ActivityResultLauncher<Intent> getCheckoutResultLauncher() {
        return checkoutResultLauncher;
    }
}