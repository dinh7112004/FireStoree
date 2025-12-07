// File: com.example.md_08_ungdungfivestore.services.OrderManager.java

package com.example.md_08_ungdungfivestore.utils;

import android.util.Log;

import com.example.md_08_ungdungfivestore.models.OrderRequest;
import com.example.md_08_ungdungfivestore.models.OrderResponse;
import com.example.md_08_ungdungfivestore.services.OrderService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderManager {

    private static final String TAG = "OrderManager";
    private final OrderService orderService;

    public OrderManager(OrderService orderService) {
        this.orderService = orderService;
    }

    public interface OrderCallback {
        void onSuccess(OrderResponse orderResponse);
        void onError(String error);
    }

    public void createOrder(OrderRequest request, OrderCallback callback) {
        orderService.createOrder(request).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                Log.d(TAG, "Response Code for createOrder: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg;
                    if (response.code() == 401) {
                        errorMsg = "Lỗi 401: Vui lòng đăng nhập lại (Token hết hạn).";
                    } else {
                        // Cố gắng lấy thông báo lỗi từ body nếu có
                        String errorBody = response.errorBody() != null ? response.errorBody().toString() : "Lỗi không xác định";
                        errorMsg = "Lỗi tạo đơn hàng: Mã lỗi " + response.code() + ". Chi tiết: " + errorBody;
                    }
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.e(TAG, "createOrder failure: " + t.getMessage(), t);
                callback.onError("Lỗi mạng hoặc kết nối: " + t.getMessage());
            }
        });
    }
}