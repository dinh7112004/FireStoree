package com.example.md_08_ungdungfivestore.services;

import com.example.md_08_ungdungfivestore.models.CartRequest;
import com.example.md_08_ungdungfivestore.models.CartResponse;
import com.example.md_08_ungdungfivestore.models.QuantityUpdate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartService {

    // GET /api/cart
    @GET("api/cart")
    Call<CartResponse> getCartItems();

    // PUT /api/cart/:itemId (Body: { "quantity": X })
    @PUT("api/cart/{itemId}")
    Call<CartResponse> updateQuantity(
            @Path("itemId") String itemId,
            @Body QuantityUpdate body
    );
    @POST("api/cart")
    Call<CartResponse> addToCart(@Body CartRequest request);
    // DELETE /api/cart/:itemId
    @DELETE("api/cart/{itemId}")
    Call<CartResponse> deleteItem(@Path("itemId") String itemId);
}