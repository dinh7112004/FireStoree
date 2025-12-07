package com.example.md_08_ungdungfivestore.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.md_08_ungdungfivestore.CheckoutActivity;
import com.example.md_08_ungdungfivestore.MainActivity; // ⭐ IMPORT MainActivity
import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.adapters.CartAdapter;
import com.example.md_08_ungdungfivestore.models.CartItem;
import com.example.md_08_ungdungfivestore.models.CartResponse;
import com.example.md_08_ungdungfivestore.models.QuantityUpdate;
import com.example.md_08_ungdungfivestore.services.CartService;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import com.example.md_08_ungdungfivestore.services.ApiClientCart;

public class GioHangFragment extends Fragment implements CartAdapter.CartItemActionListener {

    private RecyclerView rcvCart;
    private TextView tvTotal;
    private MaterialButton thanhToanBtn;
    private TextView tvEmptyCart;

    private final List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;
    private CartService cartService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gio_hang, container, false);

        rcvCart = view.findViewById(R.id.rcvCart);
        tvTotal = view.findViewById(R.id.tvTotal);
        thanhToanBtn = view.findViewById(R.id.thanhToanBtn);
        // Cần ánh xạ TextView báo giỏ hàng trống trong layout fragment_gio_hang.xml nếu có
        // tvEmptyCart = view.findViewById(R.id.tvEmptyCart);

        if (getContext() != null) {
            cartService = ApiClientCart.getCartService(getContext());
        }

        cartAdapter = new CartAdapter(getContext(), cartItems, this);
        rcvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCart.setAdapter(cartAdapter);

        thanhToanBtn.setOnClickListener(v -> navigateToCheckout());

        // Tải giỏ hàng lần đầu khi Fragment được tạo
        fetchCartItems();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // BỎ fetchCartItems() ở đây, vì chúng ta sẽ dùng ActivityResultLauncher
        // để chỉ tải lại khi Thanh toán thành công.
    }

    // ⭐ LOGIC CHUYỂN MÀN HÌNH THANH TOÁN (SỬ DỤNG LAUNCHER TỪ MAINACTIVITY)
    private void navigateToCheckout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(getContext(), "Giỏ hàng của bạn đang trống. Vui lòng thêm sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getActivity() instanceof MainActivity) {
            Intent intent = new Intent(getContext(), CheckoutActivity.class);
            ((MainActivity) getActivity()).getCheckoutResultLauncher().launch(intent);
        } else if (getContext() != null) {
            Intent intent = new Intent(getContext(), CheckoutActivity.class);
            startActivity(intent);
        }
    }

    // --- LOGIC TẢI DỮ LIỆU TỪ API (LÀM PUBLIC ĐỂ MAINACTIVITY GỌI) ---
    public void fetchCartItems() {
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
                    cartAdapter.notifyDataSetChanged();
                    updateTotalPrice();
                    // updateEmptyView();
                } else {
                    Toast.makeText(getContext(), "Lỗi tải giỏ hàng: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }

        String formattedTotal = String.format("%,.0f VNĐ", total);
        tvTotal.setText(formattedTotal);
    }

    // ... (onQuantityChange và onDelete giữ nguyên logic tối ưu hóa)

    @Override
    public void onQuantityChange(CartItem item, int newQuantity) {
        if (cartService == null || newQuantity < 1) return;

        QuantityUpdate requestBody = new QuantityUpdate(newQuantity);

        cartService.updateQuantity(item.getId(), requestBody).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CartItem> updatedItems = response.body().getItems();

                    if (updatedItems != null) {
                        cartItems.clear();
                        cartItems.addAll(updatedItems);
                        cartAdapter.notifyDataSetChanged();
                        updateTotalPrice();
                    } else {
                        Toast.makeText(getContext(), "Lỗi dữ liệu. Đang đồng bộ lại...", Toast.LENGTH_SHORT).show();
                        fetchCartItems();
                    }
                } else {
                    Toast.makeText(getContext(), "Cập nhật thất bại. Tải lại.", Toast.LENGTH_SHORT).show();
                    fetchCartItems();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng khi cập nhật số lượng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDelete(CartItem item) {
        if (cartService == null) return;

        cartService.deleteItem(item.getId()).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CartItem> updatedItems = response.body().getItems();

                    if (updatedItems != null) {
                        cartItems.clear();
                        cartItems.addAll(updatedItems);
                        cartAdapter.notifyDataSetChanged();
                        updateTotalPrice();

                        if (response.body().getMessage() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Lỗi dữ liệu. Đang đồng bộ lại...", Toast.LENGTH_SHORT).show();
                        fetchCartItems();
                    }
                } else {
                    Toast.makeText(getContext(), "Xóa sản phẩm thất bại. Tải lại.", Toast.LENGTH_SHORT).show();
                    fetchCartItems();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng khi xóa sản phẩm.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}