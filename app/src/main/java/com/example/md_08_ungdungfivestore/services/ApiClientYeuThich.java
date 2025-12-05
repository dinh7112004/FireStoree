package com.example.md_08_ungdungfivestore.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClientYeuThich {

    private static final String BASE_URL = "http://10.0.2.2:5001/";
    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            SharedPreferences sp = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                            String token = sp.getString("token", "");

                            Log.d("ApiClientYeuThich", "Requesting URL: " + original.url());
                            Log.d("ApiClientYeuThich", "Auth Header: " + (token.isEmpty() ? "No Token" : "Bearer " + token.substring(0, Math.min(token.length(), 10)) + "..."));

                            Request.Builder builder = original.newBuilder();
                            if (!token.isEmpty()) {
                                builder.header("Authorization", "Bearer " + token);
                            }

                            Request request = builder.build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static YeuThichService getYeuThichService(Context context) {
        return getClient(context).create(YeuThichService.class);
    }
}
