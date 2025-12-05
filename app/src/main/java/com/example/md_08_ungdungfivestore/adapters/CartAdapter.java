package com.example.md_08_ungdungfivestore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // ✅ Import Glide
import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final List<CartItem> cartItemList;
    private final CartItemActionListener actionListener;

    // BASE_URL Cần được định nghĩa để tải ảnh
    private static final String BASE_URL = "http://10.0.2.2:5001";

    public interface CartItemActionListener {
        void onQuantityChange(CartItem item, int newQuantity);
        void onDelete(CartItem item);
    }

    public CartAdapter(Context context, List<CartItem> cartItemList, CartItemActionListener actionListener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("%,.0f VNĐ", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvSize.setText(String.format("Size: %s | Màu: %s", item.getSize(), item.getColor()));

        // ✅ LOGIC TẢI ẢNH ĐÃ THÊM VÀO
        String imagePath = item.getImage();

        if (imagePath != null && !imagePath.isEmpty()) {
            String fullImageUrl = BASE_URL + imagePath;

            Glide.with(context)
                    .load(fullImageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imgProduct);
        } else {
            // Hiển thị ảnh mặc định nếu không có đường dẫn
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.btnIncrease.setOnClickListener(v ->
                actionListener.onQuantityChange(item, item.getQuantity() + 1)
        );

        holder.btnDecrease.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();
            if (currentQuantity > 1) {
                actionListener.onQuantityChange(item, currentQuantity - 1);
            } else {
                actionListener.onDelete(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvSize, tvQuantity;
        ImageButton btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }
}