package com.example.md_08_ungdungfivestore.services;

import com.example.md_08_ungdungfivestore.models.CheckResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface YeuThichService {

    @GET("api/wishlists/me")
    Call<Map<String, Object>> getMyWishlist();

    @POST("api/wishlists/")
    Call<Map<String, Object>> addToWishlist(@Body Map<String, String> body);

    @DELETE("api/wishlists/{productId}")
    Call<Map<String, Object>> removeFromWishlist(@Path("productId") String productId);

    @GET("api/wishlists/check/{productId}")
    Call<CheckResponse> checkWishlist(@Path("productId") String productId);
}
