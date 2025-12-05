package com.example.md_08_ungdungfivestore.services;

import android.util.Log;
import com.example.md_08_ungdungfivestore.models.Product;
import com.example.md_08_ungdungfivestore.models.CheckResponse; // <-- Import CheckResponse
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YeuThichManager {

    private static final String TAG = "YeuThichManager";
    private final YeuThichService service;

    public YeuThichManager(YeuThichService service) {
        this.service = service;
    }

    public interface CallbackMap {
        void onSuccess(Map<String, Object> responseMap);
        void onError(String error);
    }

    public interface ToggleCallback {
        void onSuccess(String message, boolean isAdded);
        void onError(String error);
    }

    // THÊM: Interface mới để kiểm tra trạng thái yêu thích
    public interface CheckCallback {
        void onCheckResult(boolean isFavorite);
        void onError(String error);
    }

    public void getMyWishlist(CallbackMap callback) {
        Log.d(TAG, "Requesting URL: " + service.getMyWishlist().request().url().toString());

        service.getMyWishlist().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.d(TAG, "Response Code for getMyWishlist: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Full Response Map: " + response.body().toString());
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Lỗi tải danh sách yêu thích: Mã lỗi " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "getMyWishlist failure: " + t.getMessage(), t);
                callback.onError("Lỗi mạng hoặc kết nối: " + t.getMessage());
            }
        });
    }

    public void addToWishlist(String productId, ToggleCallback callback) {
        Log.d(TAG, "Requesting URL: " + service.addToWishlist(Map.of("productId", productId)).request().url().toString());

        service.addToWishlist(Map.of("productId", productId)).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().get("message").toString(), true);
                } else {
                    String errorBody = response.errorBody() != null ? response.errorBody().toString() : "Unknown Error";
                    String errorMsg = "Lỗi thêm yêu thích: Mã lỗi " + response.code() + ". Chi tiết: " + errorBody;
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "addToWishlist failure", t);
                callback.onError("Lỗi mạng hoặc kết nối");
            }
        });
    }

    public void removeFromWishlist(String productId, ToggleCallback callback) {
        Log.d(TAG, "Requesting URL: " + service.removeFromWishlist(productId).request().url().toString());

        service.removeFromWishlist(productId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().get("message").toString(), false);
                } else {
                    String errorBody = response.errorBody() != null ? response.errorBody().toString() : "Unknown Error";
                    String errorMsg = "Lỗi xoá yêu thích: Mã lỗi " + response.code() + ". Chi tiết: " + errorBody;
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "removeFromWishlist failure", t);
                callback.onError("Lỗi mạng hoặc kết nối");
            }
        });
    }

    // THÊM: Phương thức để kiểm tra trạng thái yêu thích
    public void checkWishlist(String productId, CheckCallback callback) {
        service.checkWishlist(productId).enqueue(new Callback<CheckResponse>() {
            @Override
            public void onResponse(Call<CheckResponse> call, Response<CheckResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onCheckResult(response.body().isInWishlist());
                } else {
                    String errorMsg = "Lỗi kiểm tra yêu thích: Mã lỗi " + response.code();
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<CheckResponse> call, Throwable t) {
                Log.e(TAG, "checkWishlist failure", t);
                callback.onError("Lỗi mạng hoặc kết nối: " + t.getMessage());
            }
        });
    }

    public static List<Product> parseProductList(Map<String, Object> responseMap) {
        Log.d(TAG, "Bắt đầu parse danh sách sản phẩm...");

        final String WISHLIST_KEY = "wishlist";
        final String PRODUCTS_KEY = "products";

        if (responseMap == null || !responseMap.containsKey(WISHLIST_KEY) || !(responseMap.get(WISHLIST_KEY) instanceof Map)) {
            Log.d(TAG, "Không tìm thấy key '" + WISHLIST_KEY + "' hoặc giá trị không phải là Object.");
            return new ArrayList<>();
        }

        Map<String, Object> wishlistMap = (Map<String, Object>) responseMap.get(WISHLIST_KEY);

        if (!wishlistMap.containsKey(PRODUCTS_KEY) || !(wishlistMap.get(PRODUCTS_KEY) instanceof List)) {
            Log.d(TAG, "Không tìm thấy danh sách sản phẩm hợp lệ trong key '" + PRODUCTS_KEY + "'.");
            return new ArrayList<>();
        }

        try {
            List<?> rawProductsList = (List<?>) wishlistMap.get(PRODUCTS_KEY);

            String jsonList = new Gson().toJson(rawProductsList);
            java.lang.reflect.Type listType = new TypeToken<ArrayList<Product>>(){}.getType();
            List<Product> products = new Gson().fromJson(jsonList, listType);

            Log.d(TAG, "Parse thành công " + products.size() + " sản phẩm.");
            return products;

        } catch (Exception e) {
            Log.e(TAG, "LỖI PARSE JSON SẢN PHẨM: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}