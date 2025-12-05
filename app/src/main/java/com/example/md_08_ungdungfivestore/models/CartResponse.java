package com.example.md_08_ungdungfivestore.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CartResponse {
    @SerializedName("_id")
    private String id;

    @SerializedName("user_id")
    private String userId;

    // items: [ { product_id, name, image, ... } ]
    private List<CartItem> items;

    @SerializedName("message")
    private String message;

    // Getters
    public List<CartItem> getItems() {
        return items;
    }
    public String getMessage() {
        return message;
    }
}