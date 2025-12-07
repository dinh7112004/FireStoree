package com.example.md_08_ungdungfivestore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.models.OrderItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailItemAdapter extends RecyclerView.Adapter<OrderDetailItemAdapter.DetailItemViewHolder> {

    private final Context context;
    private final List<OrderItem> itemList;
    private final NumberFormat currencyFormat;

    public OrderDetailItemAdapter(Context context, List<OrderItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    }

    @NonNull
    @Override
    public DetailItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout item_detail_product.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail_product, parent, false);
        return new DetailItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailItemViewHolder holder, int position) {
        OrderItem item = itemList.get(position);

        // Tên sản phẩm
        holder.tvItemDetailProductName.setText(item.getProductName());

        // Biến thể (Size và Color)
        String variant = String.format("Size %s, Màu %s", item.getSize(), item.getColor());
        holder.tvItemDetailVariant.setText(variant);

        // Số lượng
        holder.tvItemDetailQuantity.setText(String.format("x %d", item.getQuantity()));

        // Tổng giá (Quantity * UnitPrice, hoặc dùng Subtotal nếu có)
        double itemTotal = item.getQuantity() * item.getUnitPrice();
        holder.tvItemDetailPrice.setText(currencyFormat.format(itemTotal) + " VNĐ");

        // Tải ảnh sản phẩm
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.avatar_img)
                .error(R.drawable.ic_error)
                .into(holder.imgItemDetailProduct);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Class ViewHolder
    public static class DetailItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemDetailProduct;
        TextView tvItemDetailProductName, tvItemDetailVariant, tvItemDetailQuantity, tvItemDetailPrice;

        public DetailItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các thành phần từ item_detail_product.xml
            imgItemDetailProduct = itemView.findViewById(R.id.imgItemDetailProduct);
            tvItemDetailProductName = itemView.findViewById(R.id.tvItemDetailProductName);
            tvItemDetailVariant = itemView.findViewById(R.id.tvItemDetailVariant);
            tvItemDetailQuantity = itemView.findViewById(R.id.tvItemDetailQuantity);
            tvItemDetailPrice = itemView.findViewById(R.id.tvItemDetailPrice);
        }
    }
}