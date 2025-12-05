package com.example.md_08_ungdungfivestore.services;

import com.example.md_08_ungdungfivestore.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiUtils {
    public static List<Product> parseProductList(Map<String,Object> responseMap) {
        List<Product> productList = new ArrayList<>();
        if (responseMap == null) return productList;

        try {
            Object data = responseMap.get("wishlist"); // key API trả về
            Gson gson = new Gson();
            String json = gson.toJson(data);

            Type listType = new TypeToken<List<Product>>(){}.getType();
            productList = gson.fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productList;
    }
}
