// File: com.example.md_08_ungdungfivestore.ManDonHang.java (ĐÃ SỬA)

package com.example.md_08_ungdungfivestore;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.md_08_ungdungfivestore.adapters.TabLayoutAdapter;
import com.google.android.material.tabs.TabLayout;

// ⭐ KHÔNG CẦN DEFINITION NỘI BỘ NỮA. NÓ ĐÃ LÀ FILE RIÊNG.
public class ManDonHang extends AppCompatActivity implements OnOrderUpdateListener {

    // ĐÃ XÓA định nghĩa interface ở đây.

    private TabLayout datHangTabLayout;
    private ViewPager datHangViewPager;
    private ImageButton quayLaiBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_don_hang);

        anhXa();
        setupTabs();
        setupListeners();
        checkIntentAndSelectTab();
    }

    private void anhXa() {
        datHangTabLayout = findViewById(R.id.datHangTabLayout);
        datHangViewPager = findViewById(R.id.datHangViewPager);
        quayLaiBtn = findViewById(R.id.quayLaiBtn);
    }

    private void setupTabs() {
        // Setup Tabs (4 tab: 0, 1, 2, 3)
        datHangTabLayout.addTab(datHangTabLayout.newTab().setText("Chờ xác nhận")); // Vị trí 0
        datHangTabLayout.addTab(datHangTabLayout.newTab().setText("Đang giao"));    // Vị trí 1
        datHangTabLayout.addTab(datHangTabLayout.newTab().setText("Đã giao"));      // Vị trí 2
        datHangTabLayout.addTab(datHangTabLayout.newTab().setText("Đã hủy"));       // Vị trí 3
        datHangTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Setup Adapter
        TabLayoutAdapter adapter = new TabLayoutAdapter(this, getSupportFragmentManager(),
                datHangTabLayout.getTabCount());
        datHangViewPager.setAdapter(adapter);

        // Sync Tab and ViewPager
        datHangViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(datHangTabLayout));
        datHangTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                datHangViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupListeners() {
        quayLaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void checkIntentAndSelectTab() {
        if (getIntent() != null) {
            int targetTab = getIntent().getIntExtra("targetTab", 0);
            if (targetTab >= 0 && targetTab < datHangTabLayout.getTabCount()) {
                datHangViewPager.setCurrentItem(targetTab, true);
                datHangTabLayout.getTabAt(targetTab).select();

                String newOrderId = getIntent().getStringExtra("orderId");
                if (newOrderId != null) {
                    Log.d(TAG, "Mở màn hình đơn hàng với đơn mới: " + newOrderId);
                }
            }
        }
    }

    // ⭐ TRIỂN KHAI PHƯƠNG THỨC CỦA INTERFACE ĐÃ TÁCH RA FILE RIÊNG
    @Override
    public void onOrderCancelled(String orderId) {
        // Thông báo cho người dùng
        Toast.makeText(this, "Đơn hàng #" + orderId + " đã được hủy thành công và chuyển sang tab Đã hủy.", Toast.LENGTH_LONG).show();

        // Chuyển sang tab "Đã Hủy" (Vị trí 3)
        datHangViewPager.setCurrentItem(3, true);
    }
}