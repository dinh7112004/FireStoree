package com.example.md_08_ungdungfivestore.services;
// HOẶC: package com.example.md_08_ungdungfivestore.api;

import android.content.Context;
import com.example.md_08_ungdungfivestore.utils.AuthManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientCart {

    // Đã sửa cổng từ 5000 thành 5001
    private static final String BASE_URL = "http://10.0.2.2:5001/";
    private static Retrofit retrofit = null;

    public static CartService getCartService(Context context) {
        if (retrofit == null) {

            // Interceptor để thêm Authorization Header (Bearer Token)
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        String token = AuthManager.getToken(context); // Lấy token từ AuthManager đã sửa

                        Request.Builder requestBuilder = original.newBuilder();

                        // Chỉ thêm Header nếu token tồn tại
                        if (token != null) {
                            requestBuilder.header("Authorization", "Bearer " + token);
                        }

                        Request request = requestBuilder
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(CartService.class);
    }
}