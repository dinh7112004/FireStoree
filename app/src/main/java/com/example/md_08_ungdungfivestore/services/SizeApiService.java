package com.example.md_08_ungdungfivestore.services;

import com.example.md_08_ungdungfivestore.models.Size;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SizeApiService {
    // Lấy tất cả kích cỡ
    @GET("api/sizes")
    Call<List<Size>> getAllSizes();

    // Tạo thực thể kích cỡ mới
    @POST("api/sizes")
    Call<Size> createSize(@Body Size size);

    // Cập nhật thực thể kích cỡ
    @PUT("api/sizes/{id}")
    Call<Size> updateSize(@Body Size size);

    // Xóa thực thể kích cỡ
    @DELETE("api/sizes/{id}")
    Call<Void> deleteSize(@Path("id") String id);
}