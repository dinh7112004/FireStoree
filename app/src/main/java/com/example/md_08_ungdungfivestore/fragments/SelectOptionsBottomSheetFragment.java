package com.example.md_08_ungdungfivestore.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.models.Product;
// ⭐ IMPORTS CẦN THIẾT ⭐
import com.example.md_08_ungdungfivestore.models.CartRequest;
import com.example.md_08_ungdungfivestore.models.CartResponse;
import com.example.md_08_ungdungfivestore.services.ApiClientCart;
import com.example.md_08_ungdungfivestore.services.CartService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectOptionsBottomSheetFragment extends BottomSheetDialogFragment {

    private ImageView ivProductImage;
    private TextView tvPriceNew, tvStock, tvQuantity, btnClose, btnBuyNow;
    private LinearLayout layoutColors, layoutSizes;
    private ImageButton btnDecrease, btnIncrease;

    private Product product;
    private int quantity = 1;
    private String selectedColor = null;
    private String selectedSize = null;

    private OnOptionSelectedListener listener;

    public interface OnOptionSelectedListener {
        void onOptionSelected(String size, String color, int quantity);
    }

    public SelectOptionsBottomSheetFragment(Product product, OnOptionSelectedListener listener) {
        this.product = product;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_options, container, false);

        anhXa(view);

        btnClose.setOnClickListener(v -> dismiss());

        if (product != null) {
            loadProductImage(product.getImage());
            tvPriceNew.setText(formatPrice(product.getPrice()));
            tvPriceNew.setTextColor(0xFFD17842);
            tvStock.setText("Kho: " + product.getTotalQuantity());
            tvStock.setTextColor(0xFF000000);

            setupColors();
            setupSizes();
            setupQuantity();
            setupBuyNow();
        }

        return view;
    }

    private void anhXa(View view) {
        ivProductImage = view.findViewById(R.id.ivProductImage);
        tvPriceNew = view.findViewById(R.id.tvProductPriceNew);
        tvStock = view.findViewById(R.id.tvProductStock);
        layoutColors = view.findViewById(R.id.layoutColors);
        layoutSizes = view.findViewById(R.id.layoutSizes);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        btnDecrease = view.findViewById(R.id.btnDecrease);
        btnIncrease = view.findViewById(R.id.btnIncrease);
        btnBuyNow = view.findViewById(R.id.btnBuyNow);
        btnClose = view.findViewById(R.id.btnClose);
    }

    private String formatPrice(double price) {
        return String.format("%,.0fđ", price).replace(',', '.');
    }

    private void loadProductImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            ivProductImage.setImageResource(R.drawable.ic_launcher_background);
            return;
        }

        String fullUrl;
        final String BASE_URL = "http://10.0.2.2:5001";

        if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
            fullUrl = imagePath;
        } else if (imagePath.startsWith("/uploads/")) {
            fullUrl = BASE_URL + imagePath;
        } else {
            int resId = getResources().getIdentifier(imagePath.replace(".jpg","").replace(".png",""), "drawable", getContext().getPackageName());
            if(resId != 0){
                ivProductImage.setImageResource(resId);
                return;
            } else {
                ivProductImage.setImageResource(R.drawable.ic_launcher_background);
                return;
            }
        }

        Glide.with(this)
                .load(fullUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(ivProductImage);
    }

    private void setupColors() {
        layoutColors.removeAllViews();
        List<String> colors = product.getAvailableColors();
        if (colors != null) {
            int margin = 8;
            for (String color : colors) {
                TextView colorView = new TextView(getContext());
                colorView.setText(color);
                colorView.setTextColor(0xFF000000);
                colorView.setTextSize(16f);
                colorView.setPadding(24,12,24,12);
                colorView.setBackgroundColor(0xFFFFFFFF);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(margin,0,0,0);
                colorView.setLayoutParams(params);

                colorView.setOnClickListener(v -> {
                    selectedColor = color;
                    highlightSelected(layoutColors, colorView);
                });
                layoutColors.addView(colorView);
            }
        }
    }

    private void setupSizes() {
        layoutSizes.removeAllViews();
        List<String> sizes = product.getAvailableSizes();
        if (sizes != null) {
            int margin = 8;
            for (String size : sizes) {
                TextView sizeView = new TextView(getContext());
                sizeView.setText(size);
                sizeView.setTextColor(0xFF000000);
                sizeView.setTextSize(16f);
                sizeView.setPadding(24,12,24,12);
                sizeView.setBackgroundColor(0xFFFFFFFF);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(margin,0,0,0);
                sizeView.setLayoutParams(params);

                sizeView.setOnClickListener(v -> {
                    selectedSize = size;
                    highlightSelected(layoutSizes, sizeView);
                });
                layoutSizes.addView(sizeView);
            }
        }
    }

    private void highlightSelected(LinearLayout parent, View selectedView) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            child.setBackgroundColor(0xFFFFFFFF);
            if (child instanceof TextView) ((TextView) child).setTextColor(0xFF000000);
        }
        selectedView.setBackgroundColor(0xFFD17842);
        if (selectedView instanceof TextView) ((TextView) selectedView).setTextColor(0xFFFFFFFF);
    }

    private void setupQuantity() {
        tvQuantity.setText(String.valueOf(quantity));
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) quantity--;
            tvQuantity.setText(String.valueOf(quantity));
        });
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });
    }

    /**
     * Xử lý khi nhấn nút Mua Ngay/Thêm vào Giỏ hàng.
     * Đã cập nhật CartRequest để truyền đủ 6 trường dữ liệu.
     */
    private void setupBuyNow() {
        btnBuyNow.setOnClickListener(v -> {
            if (selectedColor == null || selectedSize == null) {
                Toast.makeText(getContext(), "Vui lòng chọn size và màu", Toast.LENGTH_SHORT).show();
                return;
            }
            if (product == null || product.getId() == null) {
                Toast.makeText(getContext(), "Lỗi sản phẩm: Không có ID", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. CHUẨN BỊ VÀ GỌI API THÊM VÀO GIỎ HÀNG (Truyền đủ 6 tham số)
            CartRequest cartRequest = new CartRequest(
                    product.getId(),
                    product.getName(),
                    selectedSize,
                    selectedColor,
                    quantity,
                    product.getPrice()
            );

            CartService cartService = ApiClientCart.getCartService(getContext());
            cartService.addToCart(cartRequest).enqueue(new Callback<CartResponse>() {
                @Override
                public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                    if (response.isSuccessful()) {
                        // 2. Xử lý thành công
                        Toast.makeText(getContext(),
                                "Đã thêm " + quantity + " " + product.getName() + " vào giỏ hàng thành công!",
                                Toast.LENGTH_SHORT).show();

                        if (listener != null) {
                            listener.onOptionSelected(selectedSize, selectedColor, quantity);
                        }
                        dismiss();

                    } else {
                        // 3. XỬ LÝ LỖI API (In chi tiết lỗi và thông báo)
                        String errorMsg = "Lỗi thêm giỏ hàng (" + response.code() + "): ";

                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("CartAPI_DETAIL", "Server Error Body: " + errorBody);

                            if (response.code() == 400) {
                                // Lỗi 400 thường là lỗi thiếu trường dữ liệu hoặc lỗi logic như "hết hàng"
                                errorMsg += "Dữ liệu không hợp lệ. (Chi tiết trong Logcat)";
                                // Cố gắng hiển thị thông báo lỗi chi tiết từ server
                                if (errorBody.contains("message")) {
                                    // (Logic phức tạp hơn để parse JSON, nhưng ta dùng Logcat là chính)
                                }
                            } else if (response.code() == 401) {
                                errorMsg += "Vui lòng đăng nhập lại.";
                            } else {
                                errorMsg += "Server gặp lỗi.";
                            }
                        } catch (Exception e) {
                            errorMsg += "Không đọc được lỗi chi tiết.";
                            Log.e("CartAPI_DETAIL", "Error parsing error body: " + e.getMessage());
                        }

                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<CartResponse> call, Throwable t) {
                    // 4. XỬ LÝ LỖI MẠNG
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("CartAPI", "Lỗi Network: " + t.getMessage());
                }
            });
        });
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}