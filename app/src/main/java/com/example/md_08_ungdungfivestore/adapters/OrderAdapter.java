// File: com.example.md_08_ungdungfivestore.adapters.OrderAdapter.java

package com.example.md_08_ungdungfivestore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.models.OrderResponse;
import com.example.md_08_ungdungfivestore.models.OrderItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    // ⭐ SỬA LỖI 1: KHẮC PHỤC TẢI ẢNH (404 Not Found)
    // --------------------------------------------------------------------------------------
    // CẦN THAY THẾ "http://192.168.1.10:5001" BẰNG ĐỊA CHỈ IP VÀ PORT THỰC TẾ CỦA MÁY TÍNH BẠN
    // VÍ DỤ: "http://192.168.1.5:5001"
    private static final String SERVER_BASE_URL = "http://10.0.2.2:5001";
    // --------------------------------------------------------------------------------------

    private final Context context;
    private final List<OrderResponse> orderList;
    private OnOrderActionListener actionListener;

    // Interface để giao tiếp ngược lại với Fragment
    public interface OnOrderActionListener {
        void onCancelClick(OrderResponse order, int position);
        void onRateClick(OrderResponse order);
        // ⭐ ĐÃ THÊM: Phương thức xử lý sự kiện Xem Chi Tiết
        void onDetailClick(OrderResponse order);
    }

    public OrderAdapter(Context context, List<OrderResponse> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void setOnOrderActionListener(OnOrderActionListener listener) {
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderResponse order = orderList.get(position);

        if (order == null) return;

        // --- KHẮC PHỤC LỖI HIỂN THỊ DỮ LIỆU ---

        // 1. Hiển thị Tổng tiền (Đã sửa: dùng getTotalAmount())
        holder.tvTotalPrice.setText(formatCurrency(order.getTotalAmount()));

        // 2. Hiển thị Trạng thái
        holder.tvStatus.setText(getStatusVietnamese(order.getStatus()));

        // 3. Hiển thị Số lượng sản phẩm (Đã sửa: dùng getItems())
        int totalQuantity = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && order.getItems() != null) {
            totalQuantity = order.getItems().stream()
                    .mapToInt(OrderItem::getQuantity)
                    .sum();
        } else if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                totalQuantity += item.getQuantity();
            }
        }
        holder.tvProductCount.setText("x " + totalQuantity + " sản phẩm");

        // 4. Load Ảnh sản phẩm và Tên sản phẩm
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            OrderItem firstItem = order.getItems().get(0);

            // Hiển thị tên sản phẩm (đã fix ở Server)
            holder.tvProductName.setText(firstItem.getProductName());

            // ⭐ SỬA LỖI 2: GHÉP IP CỤC BỘ VÀO URL ẢNH
            String rawImageUrl = firstItem.getImageUrl(); // Ví dụ: /uploads/ten_file.jpg
            String fullImageUrl = SERVER_BASE_URL + rawImageUrl; // Ví dụ: http://192.168.1.10:5001/uploads/ten_file.jpg

            // Dùng Glide để tải ảnh từ URL tuyệt đối
            Glide.with(context)
                    .load(fullImageUrl) // Dùng URL đã sửa
                    .placeholder(R.drawable.avatar_img)
                    .error(R.drawable.avatar_img)
                    .into(holder.imgProduct);
        } else {
            // Trường hợp không có sản phẩm nào
            holder.imgProduct.setImageResource(R.drawable.avatar_img);
            holder.tvProductName.setText("Không có sản phẩm");
        }

        // --- Logic Hiển thị Nút hành động theo trạng thái ---
        setupActionButtons(holder, order, position);

        // ⭐ BỔ SUNG: Xử lý click trên toàn bộ item card để xem chi tiết
        holder.itemView.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDetailClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // Hàm tiện ích format tiền tệ
    private String formatCurrency(double amount) {
        // ⭐ SỬA LỖI 3: Tránh hiển thị "0.000 VND"
        if (amount <= 0) return "0 VND";
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount).replace("₫", "VND").trim();
    }

    // Hàm tiện ích chuyển trạng thái tiếng Anh sang tiếng Việt
    private String getStatusVietnamese(String status) {
        switch (status) {
            case "pending":
                return "Chờ xác nhận";
            case "confirmed": // Trạng thái này có thể được Server gửi về (nếu bạn có)
                return "Đã xác nhận";
            case "processing":
                return "Đang xử lý";
            case "shipping":
                return "Đang giao hàng";
            case "delivered": // Sửa "completed" thành "delivered" để khớp với Server
                return "Đã giao";
            case "cancelled":
                return "Đã hủy";
            default:
                return "Trạng thái không xác định";
        }
    }

    // Thiết lập nút hành động
    private void setupActionButtons(OrderViewHolder holder, OrderResponse order, int position) {
        // Tên button "XEM CHI TIẾT" trong ảnh nằm ở góc phải (có lẽ là một TextView hoặc Button khác),
        // nhưng tôi sẽ sử dụng btnAction1 vì đây là button duy nhất có ID trong ViewHolder
        holder.btnAction1.setVisibility(View.GONE);

        // Giả sử có một TextView/Button Xem Chi Tiết độc lập (nếu có trong item_order.xml)
        // Ví dụ: holder.tvDetailButton.setVisibility(View.GONE);

        // Mặc định, View (XEM CHI TIẾT) ở góc phải không phải là btnAction1,
        // nhưng vì không có item_order.xml, tôi sẽ giả định có một TextView/Button phụ
        // hoặc gán logic Xem Chi Tiết cho click toàn bộ item (đã làm ở trên).

        switch (order.getStatus()) {
            case "pending":
                holder.btnAction1.setText("HỦY ĐƠN");
                holder.btnAction1.setVisibility(View.VISIBLE);
                holder.btnAction1.setOnClickListener(v -> {
                    if (actionListener != null) {
                        actionListener.onCancelClick(order, position);
                    }
                });
                break;
            case "delivered": // Dùng "delivered" thay vì "completed"
                holder.btnAction1.setText("ĐÁNH GIÁ");
                holder.btnAction1.setVisibility(View.VISIBLE);
                holder.btnAction1.setOnClickListener(v -> {
                    if (actionListener != null) {
                        actionListener.onRateClick(order);
                    }
                });
                break;
            case "cancelled":
                // Khi đơn hàng đã hủy, không cần nút HỦY hoặc ĐÁNH GIÁ.
                // Nút "XEM CHI TIẾT" trong ảnh của bạn dường như nằm ở vị trí khác
                // và không phải là btnAction1.
                //
                // Tôi sẽ không gán lại logic Xem Chi Tiết cho btnAction1 ở đây,
                // mà sẽ dựa vào sự kiện click trên toàn bộ item (itemView.setOnClickListener)
                // đã được thêm ở trên để xử lý Xem Chi Tiết.

                // Nếu bạn muốn hiển thị nút XEM CHI TIẾT dưới dạng btnAction1:
                /*
                holder.btnAction1.setText("XEM CHI TIẾT");
                holder.btnAction1.setVisibility(View.VISIBLE);
                holder.btnAction1.setOnClickListener(v -> {
                    if (actionListener != null) {
                        actionListener.onDetailClick(order);
                    }
                });
                */
                break;
            // Các trạng thái khác (confirmed, processing, shipping) cũng nên có nút "XEM CHI TIẾT"
            default:
                // Nếu các trạng thái khác không có nút hành động đặc biệt,
                // chúng vẫn sẽ sử dụng click trên toàn bộ item để xem chi tiết.
                break;
        }
    }

    // Hàm hỗ trợ xóa đơn hàng (sử dụng trong Fragment)
    public void removeItem(int position) {
        orderList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, orderList.size());
    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductCount;
        TextView tvStatus;
        TextView tvTotalPrice;
        TextView tvProductName;
        Button btnAction1;
        // ⭐ BỔ SUNG: NẾU BẠN CÓ MỘT TextView/Button riêng cho "XEM CHI TIẾT" ở góc phải
        // TextView tvDetailButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductCount = itemView.findViewById(R.id.tvProductCount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            btnAction1 = itemView.findViewById(R.id.btnAction1);

            tvProductName = itemView.findViewById(R.id.tvProductName);
            // ⭐ BỔ SUNG: (ví dụ nếu có)
            // tvDetailButton = itemView.findViewById(R.id.tvDetailButton);
        }
    }
}