// File: com.example.md_08_ungdungfivestore.services.OrderService.java

package com.example.md_08_ungdungfivestore.services;

import com.example.md_08_ungdungfivestore.models.OrderRequest;
import com.example.md_08_ungdungfivestore.models.OrderResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {

    // 1. TẠO ĐƠN HÀNG CHUNG (Để giải quyết lỗi trong OrderManager.java)
    @POST("api/orders")
    Call<OrderResponse> createOrder(@Body OrderRequest request);

    // 2. Đặt hàng COD (POST /api/orders/cash-order)
    @POST("api/orders/cash-order")
    Call<OrderResponse> createCashOrder(@Body OrderRequest request);

    // 3. Lấy danh sách đơn hàng của người dùng theo trạng thái
    // GET /api/orders/my-orders?status={status}
    @GET("api/orders/my-orders")
    Call<List<OrderResponse>> getMyOrdersByStatus(@Query("status") String status);

    // 4. Hủy đơn hàng (Sử dụng PUT /api/orders/{id}/cancel)
    @PUT("api/orders/{id}/cancel")
    Call<OrderResponse> cancelOrder(@Path("id") String orderId);

    // 5. Lấy chi tiết đơn hàng theo ID
    @GET("api/orders/{id}")
    Call<OrderResponse> getOrderById(@Path("id") String orderId);
}