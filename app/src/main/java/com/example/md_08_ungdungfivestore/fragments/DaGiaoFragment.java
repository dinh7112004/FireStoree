package com.example.md_08_ungdungfivestore.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.ManChiTietDonHang; // ĐÃ THÊM: Activity đích
import com.example.md_08_ungdungfivestore.adapters.OrderAdapter;
import com.example.md_08_ungdungfivestore.models.OrderResponse;
import com.example.md_08_ungdungfivestore.services.ApiClientYeuThich;
import com.example.md_08_ungdungfivestore.services.OrderService;
import com.example.md_08_ungdungfivestore.ManDanhGiaSanPham;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaGiaoFragment extends Fragment implements OrderAdapter.OnOrderActionListener {

    private RecyclerView rcvOrders;
    // ĐÃ SỬA: Thay đổi kiểu dữ liệu từ TextView thành LinearLayout
    private LinearLayout tvEmpty;
    // ĐÃ THÊM: Biến này để điều khiển TextView con trong LinearLayout (dùng cho setText)
    private TextView tvEmptyText;
    private OrderAdapter orderAdapter;
    private final List<OrderResponse> orderList = new ArrayList<>();
    private OrderService orderService;

    private static final String STATUS = "delivered";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        rcvOrders = view.findViewById(R.id.rcvOrders);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        tvEmptyText = view.findViewById(R.id.tvEmptyText);

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
        // Không xử lý Hủy đơn hàng ở đây
    }

    @Override
    public void onRateClick(OrderResponse order) {
        // XỬ LÝ CHUYỂN MÀN HÌNH ĐÁNH GIÁ
        Intent intent = new Intent(getContext(), ManDanhGiaSanPham.class);
        intent.putExtra("ORDER_ID_FOR_RATING", order.getOrderId());
        Toast.makeText(getContext(), "Mở màn hình đánh giá cho đơn hàng: " + order.getOrderId(), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onDetailClick(OrderResponse order) {
        // ⭐ XỬ LÝ XEM CHI TIẾT ĐƠN HÀNG
        Intent intent = new Intent(getContext(), ManChiTietDonHang.class);

        // Truyền ID đơn hàng
        intent.putExtra("ORDER_ID", order.getOrderId());

        startActivity(intent);
        Toast.makeText(getContext(), "Mở chi tiết đơn hàng đã giao: " + order.getOrderId(), Toast.LENGTH_SHORT).show();
    }

    // --- KẾT THÚC TRIỂN KHAI INTERFACE ---


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
                    Toast.makeText(getContext(), "Lỗi tải đơn hàng đã giao: " + response.code(), Toast.LENGTH_SHORT).show();
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
            // SỬA: Dùng LinearLayout (tvEmpty) để ẩn/hiện toàn bộ khối rỗng
            tvEmpty.setVisibility(View.VISIBLE);
            rcvOrders.setVisibility(View.GONE);
            // SỬA: Dùng TextView con (tvEmptyText) để đặt văn bản
            tvEmptyText.setText("Không có đơn hàng nào đã được giao.");
        } else {
            tvEmpty.setVisibility(View.GONE);
            rcvOrders.setVisibility(View.VISIBLE);
        }
    }
}