package com.example.md_08_ungdungfivestore.fragments;

import android.content.Intent; // ĐÃ THÊM: Cần cho Intent
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

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// ĐÃ SỬA: Triển khai interface OnOrderActionListener
public class DaHuyFragment extends Fragment implements OrderAdapter.OnOrderActionListener {

    private RecyclerView rcvOrders;
    private LinearLayout tvEmpty;
    private TextView tvEmptyText;
    private OrderAdapter orderAdapter;
    private final List<OrderResponse> orderList = new ArrayList<>();
    private OrderService orderService;

    private static final String STATUS = "cancelled";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        rcvOrders = view.findViewById(R.id.rcvOrders);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        tvEmptyText = view.findViewById(R.id.tvEmptyText);

        orderService = ApiClientYeuThich.getClient(getContext()).create(OrderService.class);

        orderAdapter = new OrderAdapter(getContext(), orderList);
        orderAdapter.setOnOrderActionListener(this); // Gắn listener cho Adapter

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
        // Không xử lý Hủy đơn hàng trong Fragment Đã Hủy
    }

    @Override
    public void onRateClick(OrderResponse order) {
        // Không xử lý Đánh giá trong Fragment Đã Hủy
    }

    @Override
    public void onDetailClick(OrderResponse order) {
        // ⭐ XỬ LÝ XEM CHI TIẾT
        Intent intent = new Intent(getContext(), ManChiTietDonHang.class);

        // Truyền ID đơn hàng qua Intent
        intent.putExtra("ORDER_ID", order.getOrderId());

        startActivity(intent);
        Toast.makeText(getContext(), "Mở chi tiết đơn hàng đã hủy: " + order.getOrderId(), Toast.LENGTH_SHORT).show();
    }

    // --- KẾT THÚC TRIỂN KHAI INTERFACE ---


    public void fetchOrders(String status) {
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
                    Toast.makeText(getContext(), "Lỗi tải đơn hàng đã hủy: " + response.code(), Toast.LENGTH_SHORT).show();
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
            tvEmpty.setVisibility(View.VISIBLE);
            rcvOrders.setVisibility(View.GONE);
            tvEmptyText.setText("Không có đơn hàng nào đã bị hủy.");
        } else {
            tvEmpty.setVisibility(View.GONE);
            rcvOrders.setVisibility(View.VISIBLE);
        }
    }
}