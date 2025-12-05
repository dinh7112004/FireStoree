package com.example.md_08_ungdungfivestore.services;

import com.example.md_08_ungdungfivestore.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApi {

    /**
     * Endpoint: /api/products/search
     * Truyền từ khóa tìm kiếm qua tham số 'q'
     */
    @GET("api/products/search")
    Call<List<Product>> searchProducts(
            @Query("q") String keyword
    );
}