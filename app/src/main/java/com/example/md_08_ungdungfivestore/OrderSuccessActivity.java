// File: com.example.md_08_ungdungfivestore.OrderSuccessActivity.java

package com.example.md_08_ungdungfivestore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderSuccessActivity extends AppCompatActivity {

    private TextView tvOrderId;
    private Button btnContinueShopping, btnViewOrderHistory;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        // Ánh xạ View
        tvOrderId = findViewById(R.id.tvOrderId);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);
        btnViewOrderHistory = findViewById(R.id.btnViewOrderHistory);

        // Lấy Order ID từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            orderId = intent.getStringExtra("orderId");
            if (orderId != null) {
                tvOrderId.setText("Mã Đơn hàng của bạn: #" + orderId);
            } else {
                tvOrderId.setText("Mã Đơn hàng không tìm thấy.");
            }
        }

        // --- Xử lý sự kiện ---

        // 1. Tiếp tục mua sắm: Quay về màn hình chính (hoặc Home Fragment)
        btnContinueShopping.setOnClickListener(v -> {
            Intent mainIntent = new Intent(OrderSuccessActivity.this, MainActivity.class);
            // Xóa hết các Activity trên Stack và bắt đầu mới (tùy chọn)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        });

        // 2. Xem lịch sử đơn hàng: Chuyển đến màn hình/Fragment Lịch sử đơn hàng
        btnViewOrderHistory.setOnClickListener(v -> {
            // Thay thế OrderHistoryActivity bằng tên Activity hoặc Fragment chứa lịch sử đơn hàng của bạn
            Intent historyIntent = new Intent(OrderSuccessActivity.this, MainActivity.class);
            historyIntent.putExtra("NAVIGATE_TO", "ORDERS"); // Dùng Extra để chuyển đến Orders Fragment
            startActivity(historyIntent);
            finish();
        });
    }

    // Ngăn người dùng quay lại màn hình Checkout bằng nút Back
    @Override
    public void onBackPressed() {
        // Chuyển thẳng về màn hình chính
        Intent mainIntent = new Intent(OrderSuccessActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        super.onBackPressed();
    }
}