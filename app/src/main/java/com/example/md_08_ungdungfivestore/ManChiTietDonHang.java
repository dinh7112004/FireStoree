package com.example.md_08_ungdungfivestore;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog; // Import AlertDialog của AppCompat
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.md_08_ungdungfivestore.adapters.OrderDetailItemAdapter;
import com.example.md_08_ungdungfivestore.models.Address;
import com.example.md_08_ungdungfivestore.models.OrderItem;
import com.example.md_08_ungdungfivestore.models.OrderResponse;
import com.example.md_08_ungdungfivestore.services.ApiClientYeuThich;
import com.example.md_08_ungdungfivestore.services.OrderService;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date; // Cần dùng java.util.Date

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManChiTietDonHang extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvDetailStatus, tvDetailOrderId, tvDetailOrderDate, tvDetailAddress,
            tvDetailPaymentMethod, tvSummarySubtotal, tvSummaryShippingFee, tvSummaryTotal;
    private RecyclerView rcvOrderItems;
    private Button btnDetailAction;

    private OrderService orderService;
    private String orderId;
    private OrderDetailItemAdapter itemAdapter;
    private OrderResponse currentOrder;

    // Định dạng tiền tệ
    private final NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    // Định dạng ngày giờ
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_chi_tiet_don_hang);

        anhXa();
        setupListeners();

        // Lấy Order ID từ Intent
        orderId = getIntent().getStringExtra("ORDER_ID");

        if (orderId != null) {
            orderService = ApiClientYeuThich.getClient(this).create(OrderService.class);
            fetchOrderDetail(orderId);
        } else {
            Toast.makeText(this, "Không tìm thấy mã đơn hàng.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void anhXa() {
        btnBack = findViewById(R.id.btnBack);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        tvDetailOrderId = findViewById(R.id.tvDetailOrderId);
        tvDetailOrderDate = findViewById(R.id.tvDetailOrderDate);
        tvDetailAddress = findViewById(R.id.tvDetailAddress);
        tvDetailPaymentMethod = findViewById(R.id.tvDetailPaymentMethod);
        tvSummarySubtotal = findViewById(R.id.tvSummarySubtotal);
        tvSummaryShippingFee = findViewById(R.id.tvSummaryShippingFee);
        tvSummaryTotal = findViewById(R.id.tvSummaryTotal);
        rcvOrderItems = findViewById(R.id.rcvOrderItems);
        btnDetailAction = findViewById(R.id.btnDetailAction);

        rcvOrderItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    // GỌI API LẤY CHI TIẾT ĐƠN HÀNG
    private void fetchOrderDetail(String id) {
        orderService.getOrderById(id).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentOrder = response.body();
                    displayOrderDetails(currentOrder);
                } else {
                    Toast.makeText(ManChiTietDonHang.this, "Không thể tải chi tiết đơn hàng: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                Toast.makeText(ManChiTietDonHang.this, "Lỗi mạng khi tải chi tiết đơn hàng.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // HIỂN THỊ DỮ LIỆU LÊN UI
    private void displayOrderDetails(OrderResponse order) {
        // 1. Thông tin chung và Trạng thái
        tvDetailOrderId.setText(order.getOrderId());
        tvDetailStatus.setText(mapStatusToVietnamese(order.getStatus()));

        Date orderDate = order.getOrderDate();
        if (orderDate != null) {
            tvDetailOrderDate.setText(dateFormat.format(orderDate));
        } else {
            tvDetailOrderDate.setText("N/A");
        }

        // 2. Thông tin Địa chỉ
        Address address = order.getShippingAddress();
        if (address != null) {
            String fullAddress = String.format("%s | %s\n%s, %s, %s, %s",
                    address.getFullName(),
                    address.getPhoneNumber(),
                    address.getStreet(),
                    address.getWard(),
                    address.getDistrict(),
                    address.getProvince());
            tvDetailAddress.setText(fullAddress);
        } else {
            tvDetailAddress.setText("Địa chỉ không có sẵn.");
        }

        // ⭐ SỬA LỖI NullPointerException: Kiểm tra null cho paymentMethod
        String paymentMethod = order.getPaymentMethod();
        if (paymentMethod != null) {
            tvDetailPaymentMethod.setText(paymentMethod.equalsIgnoreCase("cash") ? "Thanh toán khi nhận hàng (COD)" : paymentMethod.toUpperCase());
        } else {
            tvDetailPaymentMethod.setText("Phương thức thanh toán không xác định");
        }


        // 3. Danh sách Sản phẩm
        if (order.getItems() != null) {
            itemAdapter = new OrderDetailItemAdapter(this, order.getItems());
            rcvOrderItems.setAdapter(itemAdapter);
        }

        // 4. Tổng quan Thanh toán
        double subtotal = calculateSubtotal(order);
        // Lấy shippingFee từ model, nếu không có, tính toán dựa trên tổng tiền
        double shippingFee = (order.getShippingFee() != null) ? order.getShippingFee() : (order.getTotalAmount() - subtotal);
        if (shippingFee < 0) shippingFee = 0;

        double total = order.getTotalAmount();

        tvSummarySubtotal.setText(formatCurrency(subtotal));
        tvSummaryShippingFee.setText(formatCurrency(shippingFee));
        tvSummaryTotal.setText(formatCurrency(total));

        // 5. Xử lý nút Hành động dưới cùng
        setupActionButton(order);
    }

    private double calculateSubtotal(OrderResponse order) {
        double subtotal = 0;
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                subtotal += (item.getQuantity() * item.getUnitPrice());
            }
        }
        return subtotal;
    }

    private String formatCurrency(double amount) {
        return currencyFormat.format(amount) + " VNĐ";
    }

    private String mapStatusToVietnamese(String status) {
        if (status == null) return "Không xác định";
        switch (status.toLowerCase()) {
            case "pending": return "Chờ xác nhận";
            case "confirmed":
            case "processing": return "Đang xử lý";
            case "shipping": return "Đang giao";
            case "delivered": return "Đã giao";
            case "cancelled": return "Đã hủy";
            default: return status;
        }
    }

    // Cấu hình Nút Hành động dựa trên trạng thái
    private void setupActionButton(OrderResponse order) {
        btnDetailAction.setVisibility(View.GONE);
        String status = order.getStatus();

        if ("pending".equalsIgnoreCase(status)) {
            btnDetailAction.setVisibility(View.VISIBLE);
            btnDetailAction.setText("HỦY ĐƠN HÀNG");
            btnDetailAction.setBackgroundColor(getResources().getColor(R.color.red));
            btnDetailAction.setOnClickListener(v -> cancelOrder(order.getOrderId()));
        } else if ("delivered".equalsIgnoreCase(status)) {
            btnDetailAction.setVisibility(View.VISIBLE);
            btnDetailAction.setText("ĐÁNH GIÁ SẢN PHẨM");
            // btnDetailAction.setBackgroundColor(getResources().getColor(R.color.primary)); // Sử dụng màu chính nếu cần
            btnDetailAction.setOnClickListener(v -> {
                // CHUYỂN SANG MÀN HÌNH ĐÁNH GIÁ
                Intent intent = new Intent(ManChiTietDonHang.this, ManDanhGiaSanPham.class);
                intent.putExtra("ORDER_ID_FOR_RATING", order.getOrderId());
                startActivity(intent);
            });
        }
    }

    // XỬ LÝ HỦY ĐƠN HÀNG
    private void cancelOrder(String id) {
        // Thêm Dialog xác nhận
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận Hủy Đơn Hàng")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không?")
                .setPositiveButton("Hủy", (dialog, which) -> {
                    callCancelApi(id);
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void callCancelApi(String id) {
        orderService.cancelOrder(id).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManChiTietDonHang.this, "Đơn hàng đã hủy thành công!", Toast.LENGTH_SHORT).show();

                    // Cập nhật trạng thái và ẩn nút Hành động
                    tvDetailStatus.setText(mapStatusToVietnamese("cancelled"));
                    btnDetailAction.setVisibility(View.GONE);

                    // Gửi tín hiệu RESULT_OK về Activity cha (ManDonHang) để làm mới danh sách
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ManChiTietDonHang.this, "Hủy đơn hàng thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                Toast.makeText(ManChiTietDonHang.this, "Lỗi mạng khi hủy đơn hàng.", Toast.LENGTH_LONG).show();
            }
        });
    }
}