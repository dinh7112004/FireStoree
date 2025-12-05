package com.example.md_08_ungdungfivestore.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchApiClient {

    private static SearchApiClient instance;
    private static Retrofit retrofit;

    private static final String BASE_URL = "http://10.0.2.2:5001/";

    private SearchApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized SearchApiClient getInstance() {
        if (instance == null) {
            instance = new SearchApiClient();
        }
        return instance;
    }

    public SearchApi getSearchApi() {
        return retrofit.create(SearchApi.class);
    }
}