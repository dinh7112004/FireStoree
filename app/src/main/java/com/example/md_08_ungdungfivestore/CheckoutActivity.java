package com.example.md_08_ungdungfivestore;

import android.app.Activity; // ⭐ Cần import Activity
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.md_08_ungdungfivestore.models.Address;
import com.example.md_08_ungdungfivestore.models.CartItem;
import com.example.md_08_ungdungfivestore.models.CartResponse;
import com.example.md_08_ungdungfivestore.models.OrderRequest;
import com.example.md_08_ungdungfivestore.models.OrderResponse;
import com.example.md_08_ungdungfivestore.services.ApiClientCart;
import com.example.md_08_ungdungfivestore.services.ApiClientYeuThich;
import com.example.md_08_ungdungfivestore.services.CartService;
import com.example.md_08_ungdungfivestore.utils.OrderManager;
import com.example.md_08_ungdungfivestore.services.OrderService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = "CheckoutActivity";

    private EditText edtName, edtPhone, edtStreet, edtProvince, edtDistrict, edtWard;
    private Button btnPlaceOrder;
    private TextView tvSubtotal, tvShippingFee, tvTotalAmount;

    private OrderManager orderManager;
    private CartService cartService;
    private List<CartItem> cartItems = new ArrayList<>();
    private final double SHIPPING_FEE = 30000;

    private double currentTotalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        anhXa();

        OrderService orderService = ApiClientYeuThich.getClient(this).create(OrderService.class);
        orderManager = new OrderManager(orderService);
        cartService = ApiClientCart.getCartService(this);

        btnPlaceOrder.setEnabled(false);
        fetchCartItems();

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void anhXa() {
        edtName = findViewById(R.id.edtReceiverName);
        edtPhone = findViewById(R.id.edtPhone);
        edtStreet = findViewById(R.id.edtStreet);
        edtProvince = findViewById(R.id.edtProvince);
        edtDistrict = findViewById(R.id.edtDistrict);
        edtWard = findViewById(R.id.edtWard);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
    }

    private void fetchCartItems() {
        if (cartService == null) return;

        cartService.getCartItems().enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CartItem> newItems = response.body().getItems();

                    cartItems.clear();
                    if (newItems != null) {
                        cartItems.addAll(newItems);
                    }

                    if (cartItems.isEmpty()) {
                        Toast.makeText(CheckoutActivity.this, "Giỏ hàng trống. Đang quay lại...", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }

                    updateSummary();
                    btnPlaceOrder.setEnabled(true);

                } else {
                    Toast.makeText(CheckoutActivity.this, "Lỗi tải dữ liệu giỏ hàng: " + response.code(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Lỗi kết nối mạng khi tải giỏ hàng.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void updateSummary() {
        if (cartItems.isEmpty()) {
            return;
        }

        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        currentTotalAmount = subtotal + SHIPPING_FEE;

        tvSubtotal.setText(String.format("%,.0f VNĐ", subtotal));
        tvShippingFee.setText(String.format("%,.0f VNĐ", SHIPPING_FEE));
        tvTotalAmount.setText(String.format("%,.0f VNĐ", currentTotalAmount));
    }

    private void placeOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống. Không thể đặt hàng.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentTotalAmount <= 0) {
            Toast.makeText(this, "Không thể xác định tổng tiền. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String street = edtStreet.getText().toString().trim();
        String province = edtProvince.getText().toString().trim();
        String district = edtDistrict.getText().toString().trim();
        String ward = edtWard.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || street.isEmpty() || province.isEmpty() || district.isEmpty() || ward.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đủ thông tin địa chỉ.", Toast.LENGTH_SHORT).show();
            return;
        }

        String paymentMethod = "cash";
        String note = "Giao hàng giờ hành chính";

        Address shippingAddress = new Address(name, phone, street, ward, district, province);

        OrderRequest request = new OrderRequest(
                shippingAddress,
                paymentMethod,
                note,
                cartItems,
                SHIPPING_FEE,
                currentTotalAmount
        );

        btnPlaceOrder.setEnabled(false);
        Toast.makeText(this, "Đang xử lý đơn hàng...", Toast.LENGTH_LONG).show();

        orderManager.createOrder(request, new OrderManager.OrderCallback() {
            @Override
            public void onSuccess(OrderResponse orderResponse) {
                btnPlaceOrder.setEnabled(true);
                Log.d(TAG, "Order placed successfully. ID: " + orderResponse.getOrderId());

                // ⭐ BƯỚC QUAN TRỌNG: BÁO HIỆU THÀNH CÔNG CHO MainActivity
                setResult(Activity.RESULT_OK);

                // Chuyển sang màn hình thành công
                Intent intent = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
                intent.putExtra("orderId", orderResponse.getOrderId());
                startActivity(intent);

                // Kết thúc màn hình hiện tại
                finish();
            }

            @Override
            public void onError(String error) {
                btnPlaceOrder.setEnabled(true);
                Log.e(TAG, "Order placement failed: " + error);

                Toast.makeText(CheckoutActivity.this, "Đặt hàng thất bại: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}