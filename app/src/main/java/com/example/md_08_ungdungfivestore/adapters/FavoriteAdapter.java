package com.example.md_08_ungdungfivestore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.models.Product;
import com.example.md_08_ungdungfivestore.services.YeuThichManager;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    // 🌟 BASE URL ĐÃ ĐƯỢC THÊM VÀO ĐỂ TẢI HÌNH ẢNH TỪ SERVER
    private static final String BASE_IMAGE_URL = "http://10.0.2.2:5001";

    private Context context;
    private List<Product> productList;
    private YeuThichManager yeuThichManager;
    private OnFavoriteClickListener onFavoriteClickListener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Product product);
    }

    public FavoriteAdapter(Context context, List<Product> productList, YeuThichManager yeuThichManager, OnFavoriteClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.yeuThichManager = yeuThichManager;
        this.onFavoriteClickListener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(String.format("%,d đ", (int) product.getPrice())); // Định dạng lại giá tiền cho dễ nhìn

        // 🛠️ ĐIỂM SỬA LỖI: Nối Base URL để tạo URL hoàn chỉnh
        String imagePath = product.getImage();

        if (imagePath != null && !imagePath.isEmpty()) {
            // Tạo URL hoàn chỉnh
            String fullImageUrl = BASE_IMAGE_URL + imagePath;

            Glide.with(context)
                    .load(fullImageUrl) // SỬ DỤNG URL HOÀN CHỈNH ĐỂ TẢI ẢNH
                    .placeholder(R.drawable.ic_kids1) // Bạn thay bằng ảnh placeholder phù hợp
                    .error(R.drawable.ic_launcher_background) // Ảnh hiển thị khi lỗi tải
                    .into(holder.ivProduct);
        } else {
            // Hiển thị ảnh mặc định nếu không có đường dẫn ảnh
            holder.ivProduct.setImageResource(R.drawable.ic_kids1);
        }

        holder.btnFavorite.setOnClickListener(v -> {
            if (onFavoriteClickListener != null) {
                onFavoriteClickListener.onFavoriteClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvPrice;
        ImageButton btnFavorite;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}