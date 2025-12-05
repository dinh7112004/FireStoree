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

import com.bumptech.glide.Glide;
import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public ProductAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product); // Click vào ảnh hoặc cả item
        void onAddClick(Product product);  // Click vào nút thêm
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(String.format("%.0f VND", p.getPrice()));

        // Load ảnh với Glide, check URL đầy đủ
        if (p.getImage() != null && !p.getImage().isEmpty()) {
            String imageUrl = p.getImage();
            if (!imageUrl.startsWith("http")) {
                if (!imageUrl.startsWith("/")) imageUrl = "/" + imageUrl;
                imageUrl = "http://10.0.2.2:5001" + imageUrl;
            }
            Glide.with(context).load(imageUrl).error(R.drawable.ic_kids1).into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_kids1);
        }

        // Click cả item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(p);
        });

        // Click trực tiếp vào ảnh cũng mở chi tiết
        holder.imgProduct.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(p);
        });

        // Click nút thêm vào giỏ hàng
        holder.btnAdd.setOnClickListener(v -> {
            if (listener != null) listener.onAddClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProducts(List<Product> newProducts) {
        this.productList = newProducts;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice;
        ImageButton btnAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
