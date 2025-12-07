package com.example.md_08_ungdungfivestore.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent; // ĐÃ THÊM: Cần cho Intent
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

import com.example.md_08_ungdungfivestore.OnOrderUpdateListener;
import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.ManChiTietDonHang; // ĐÃ THÊM: Activity đích
import com.example.md_08_ungdungfivestore.adapters.OrderAdapter;
import com.example.md_08_ungdungfivestore.models.OrderResponse;
import com.example.md_08_ungdungfivestore.services.ApiClientYeuThich;
import com.example.md_08_ungdungfivestore.services.OrderService;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoXacNhanFragment extends Fragment implements OrderAdapter.OnOrderActionListener {

    private RecyclerView rcvOrders;

    // ⭐ SỬA LỖI CASTING: Ánh xạ tới LinearLayout chứa thông báo
    private View emptyViewContainer;
    // ⭐ Biến cho TextView hiển thị nội dung bên trong emptyViewContainer
    private TextView tvEmptyText;

    private OrderAdapter orderAdapter;
    private final List<OrderResponse> orderList = new ArrayList<>();
    private OrderService orderService;

    private OnOrderUpdateListener orderUpdateListener;

    private static final String STATUS = "pending";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Kiểm tra Activity chủ có implement interface độc lập không
        if (context instanceof OnOrderUpdateListener) {
            orderUpdateListener = (OnOrderUpdateListener) context;
        } else {
            // throw new RuntimeException(context.toString() + " must implement OnOrderUpdateListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Sử dụng layout fragment_order_list
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        rcvOrders = view.findViewById(R.id.rcvOrders);

        // ⭐ SỬA LỖI CASTING: Ánh xạ LinearLayout container (ID: tvEmpty)
        emptyViewContainer = view.findViewById(R.id.tvEmpty);
        // ⭐ Ánh xạ TextView bên trong (ID: tvEmptyText)
        tvEmptyText = emptyViewContainer.findViewById(R.id.tvEmptyText);

        orderService = ApiClientYeuThich.getClient(getContext()).create(OrderService.class);

        orderAdapter = new OrderAdapter(getContext(), orderList);
        orderAdapter.setOnOrderActionListener(this);

        rcvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvOrders.setAdapter(orderAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchOrders(STATUS);
    }

    // --- TRIỂN KHAI INTERFACE ORDERADAPTER.ONORDERACTIONLISTENER ---

    @Override
    public void onCancelClick(OrderResponse order, int position) {
        // Hiển thị Dialog xác nhận trước khi gọi API
        new AlertDialog.Builder(getContext())
                .setTitle("Hủy đơn hàng")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng #" + order.getOrderId() + " này không?")
                .setPositiveButton("Hủy", (dialog, which) -> callCancelOrderApi(order.getOrderId(), position))
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public void onRateClick(OrderResponse order) {
        // Không xử lý Đánh giá trong fragment này
    }

    @Override
    public void onDetailClick(OrderResponse order) {
        // ⭐ XỬ LÝ XEM CHI TIẾT ĐƠN HÀNG
        Intent intent = new Intent(getContext(), ManChiTietDonHang.class);

        // Truyền ID đơn hàng
        intent.putExtra("ORDER_ID", order.getOrderId());

        startActivity(intent);
        Toast.makeText(getContext(), "Mở chi tiết đơn hàng: " + order.getOrderId(), Toast.LENGTH_SHORT).show();
    }

    // --- KẾT THÚC TRIỂN KHAI INTERFACE ---


    private void callCancelOrderApi(String orderId, int position) {
        orderService.cancelOrder(orderId).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    orderAdapter.removeItem(position);
                    updateEmptyView();

                    // THÔNG BÁO CHO ACTIVITY CHỦ
                    if (orderUpdateListener != null) {
                        orderUpdateListener.onOrderCancelled(orderId);
                    }
                    Toast.makeText(getContext(), "Đã hủy đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Lỗi hủy đơn hàng: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: Không thể hủy đơn hàng.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchOrders(String status) {
        if (orderService == null) return;

        orderService.getMyOrdersByStatus(status).enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<OrderResponse>> call, @NonNull Response<List<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    orderAdapter.notifyDataSetChanged();
                    updateEmptyView();
                } else {
                    Toast.makeText(getContext(), "Lỗi tải đơn hàng: " + response.code(), Toast.LENGTH_SHORT).show();
                    updateEmptyView();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OrderResponse>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                updateEmptyView();
            }
        });
    }

    private void updateEmptyView() {
        if (orderList.isEmpty()) {
            // ⭐ SỬA: Hiển thị LinearLayout container
            emptyViewContainer.setVisibility(View.VISIBLE);
            rcvOrders.setVisibility(View.GONE);
            // ⭐ SỬA: Đặt text vào TextView bên trong container
            tvEmptyText.setText("Không có đơn hàng nào đang chờ xác nhận.");
        } else {
            emptyViewContainer.setVisibility(View.GONE);
            rcvOrders.setVisibility(View.VISIBLE);
        }
    }
}