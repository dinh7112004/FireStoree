package com.example.md_08_ungdungfivestore.fragments;

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

        // ⚠️ Đảm bảo ApiClientCart đang dùng cổng 5001
        if (getContext() != null) {
            cartService = ApiClientCart.getCartService(getContext());
        }

        cartAdapter = new CartAdapter(getContext(), cartItems, this);
        rcvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCart.setAdapter(cartAdapter);

        thanhToanBtn.setOnClickListener(v -> {
            // Logic Thanh toán
        });

        fetchCartItems();

        return view;
    }

    // --- LOGIC TẢI DỮ LIỆU TỪ API ---
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
                    cartAdapter.notifyDataSetChanged();
                    updateTotalPrice();
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

    // --- Cập nhật số lượng (Đã Tối ưu hóa xử lý lỗi) ---
    @Override
    public void onQuantityChange(CartItem item, int newQuantity) {
        if (cartService == null || newQuantity < 1) return;

        QuantityUpdate requestBody = new QuantityUpdate(newQuantity);

        cartService.updateQuantity(item.getId(), requestBody).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CartItem> updatedItems = response.body().getItems();

                    // ✅ LOGIC TỐI ƯU: Chỉ cập nhật nếu danh sách không bị NULL
                    if (updatedItems != null) {
                        cartItems.clear();
                        cartItems.addAll(updatedItems);
                        cartAdapter.notifyDataSetChanged();
                        updateTotalPrice();

                        if (response.body().getMessage() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Trường hợp lỗi Backend trả về 200 nhưng items là null
                        Toast.makeText(getContext(), "Lỗi dữ liệu. Đang đồng bộ lại...", Toast.LENGTH_SHORT).show();
                        fetchCartItems(); // Tải lại giỏ hàng
                    }
                } else {
                    // Nếu thất bại (hết hàng, 4xx, 5xx), TẢI LẠI để đồng bộ trạng thái đúng từ server
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

    // --- Xóa sản phẩm (Đã Tối ưu hóa xử lý lỗi) ---
    @Override
    public void onDelete(CartItem item) {
        if (cartService == null) return;

        cartService.deleteItem(item.getId()).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CartItem> updatedItems = response.body().getItems();

                    // ✅ LOGIC TỐI ƯU: Chỉ cập nhật nếu danh sách không bị NULL
                    if (updatedItems != null) {
                        cartItems.clear();
                        cartItems.addAll(updatedItems);
                        cartAdapter.notifyDataSetChanged();
                        updateTotalPrice();

                        if (response.body().getMessage() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Trường hợp lỗi Backend trả về 200 nhưng items là null
                        Toast.makeText(getContext(), "Lỗi dữ liệu. Đang đồng bộ lại...", Toast.LENGTH_SHORT).show();
                        fetchCartItems(); // Tải lại giỏ hàng
                    }
                } else {
                    Toast.makeText(getContext(), "Xóa sản phẩm thất bại. Tải lại.", Toast.LENGTH_SHORT).show();
                    fetchCartItems(); // Tải lại giỏ hàng
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng khi xóa sản phẩm.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}