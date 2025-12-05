package com.example.md_08_ungdungfivestore;

import android.content.Intent; // ⭐ IMPORT MỚI
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.md_08_ungdungfivestore.adapters.ImagePagerAdapter;
import com.example.md_08_ungdungfivestore.fragments.GioHangFragment;
import com.example.md_08_ungdungfivestore.fragments.SelectOptionsBottomSheetFragment;
import com.example.md_08_ungdungfivestore.models.Product;
import com.example.md_08_ungdungfivestore.services.ApiClientYeuThich;
import com.example.md_08_ungdungfivestore.services.YeuThichManager;

import java.util.ArrayList;
import java.util.List;

public class XemChiTiet extends AppCompatActivity {

    private ViewPager2 viewPagerImages;
    private ImageButton btnBack, btnFavorite;
    private TextView tvName, tvPrice, tvDesc, btnOrderNow, btnAddToCart;

    private Product product;
    private List<String> imageUrls = new ArrayList<>();

    private boolean isFavorite = false;
    private YeuThichManager yeuThichManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chi_tiet);

        anhXa();

        // Lấy Product từ Intent
        product = (Product) getIntent().getSerializableExtra("product");
        if (product == null) {
            Toast.makeText(this, "Sản phẩm không tồn tại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvName.setText(product.getName());
        tvPrice.setText(String.format("%,.0f VND", product.getPrice()));

        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            StringBuilder desc = new StringBuilder();
            for (Product.Description d : product.getDescription()) {
                desc.append(d.getField()).append(": ").append(d.getValue()).append("\n");
            }
            tvDesc.setText(desc.toString());
        } else {
            tvDesc.setText("Không có mô tả chi tiết.");
        }

        // Ảnh sản phẩm
        imageUrls.clear();
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            imageUrls.add(product.getImage());
        }
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            imageUrls.addAll(product.getImages());
        }

        setupViewPager();
        setupBackButton();

        yeuThichManager = new YeuThichManager(ApiClientYeuThich.getYeuThichService(this));

        if (product.getId() != null && !product.getId().isEmpty()) {
            checkFavoriteStatus(product.getId());
        } else {
            Log.e("XemChiTiet", "Lỗi: Product ID bị null hoặc rỗng, không thể kiểm tra trạng thái yêu thích.");
            btnFavorite.setImageResource(R.drawable.heart_empty);
            btnFavorite.setEnabled(false);
        }

        setupFavoriteButton();

        // ⭐ ĐÃ SỬA: Tách logic xử lý sự kiện cho 2 nút
        setupCartButtons();
    }

    private void anhXa() {
        viewPagerImages = findViewById(R.id.viewPagerImages);
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);

        tvName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvProductPrice);
        tvDesc = findViewById(R.id.tvProductDesc);

        btnOrderNow = findViewById(R.id.btnOrderNow);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }

    private void setupViewPager() {
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, imageUrls);
        viewPagerImages.setAdapter(adapter);
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void checkFavoriteStatus(String productId) {
        yeuThichManager.checkWishlist(productId, new YeuThichManager.CheckCallback() {
            @Override
            public void onCheckResult(boolean isFavoriteServer) {
                isFavorite = isFavoriteServer;
                btnFavorite.setImageResource(isFavorite ? R.drawable.heart_filled : R.drawable.heart_empty);
            }

            @Override
            public void onError(String error) {
                Log.e("XemChiTiet", "Lỗi kiểm tra yêu thích: " + error);
            }
        });
    }

    private void setupFavoriteButton() {
        if (product.getId() == null || product.getId().isEmpty()) return;

        btnFavorite.setOnClickListener(v -> {
            btnFavorite.animate()
                    .scaleX(1.3f).scaleY(1.3f).setDuration(120)
                    .withEndAction(() ->
                            btnFavorite.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
                    ).start();

            if (!isFavorite) {
                yeuThichManager.addToWishlist(product.getId(), new YeuThichManager.ToggleCallback() {
                    @Override
                    public void onSuccess(String message, boolean isAdded) {
                        isFavorite = true;
                        btnFavorite.setImageResource(R.drawable.heart_filled);
                        Toast.makeText(XemChiTiet.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(XemChiTiet.this, "Lỗi thêm yêu thích: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                yeuThichManager.removeFromWishlist(product.getId(), new YeuThichManager.ToggleCallback() {
                    @Override
                    public void onSuccess(String message, boolean isAdded) {
                        isFavorite = false;
                        btnFavorite.setImageResource(R.drawable.heart_empty);
                        Toast.makeText(XemChiTiet.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(XemChiTiet.this, "Lỗi bỏ yêu thích: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // ⭐ PHẦN MỚI: Tách biệt hành động cho 2 nút Giỏ hàng
    private void setupCartButtons() {
        // 1. Nút Thêm vào giỏ hàng: Chỉ thêm, không chuyển màn hình
        btnAddToCart.setOnClickListener(v -> openSelectOptionsBottomSheet(false));

        // 2. Nút Mua ngay: Thêm và chuyển sang màn hình Giỏ hàng
        btnOrderNow.setOnClickListener(v -> openSelectOptionsBottomSheet(true));
    }

    /**
     * Mở BottomSheet. Xử lý chuyển màn hình sau khi sản phẩm được thêm thành công.
     * @param navigateToCart True nếu cần chuyển đến CartActivity sau khi thêm thành công (cho nút Mua ngay)
     */
    private void openSelectOptionsBottomSheet(boolean navigateToCart) {
        if (product == null) return;

        SelectOptionsBottomSheetFragment bottomSheet = new SelectOptionsBottomSheetFragment(product, (size, color, quantity) -> {
            // HÀNH ĐỘNG SAU KHI SẢN PHẨM ĐÃ ĐƯỢC THÊM VÀO GIỎ HÀNG THÀNH CÔNG (từ BottomSheet)

            String successMsg = "Đã thêm vào giỏ hàng: Size " + size + ", Màu " + color + ", SL: " + quantity;
            Toast.makeText(XemChiTiet.this, successMsg, Toast.LENGTH_SHORT).show();

            if (navigateToCart) {
                // Nếu là nút "Mua ngay" (navigateToCart = true), chuyển sang màn hình giỏ hàng
                Intent intent = new Intent(XemChiTiet.this, GioHangFragment.class); // Thay CartActivity bằng tên Activity Giỏ hàng của bạn
                startActivity(intent);
                // Nếu bạn muốn người dùng quay lại màn hình chính thay vì màn hình chi tiết, có thể dùng finish()
            }
        });

        bottomSheet.show(getSupportFragmentManager(), "SelectOptionsBottomSheet");
    }
}