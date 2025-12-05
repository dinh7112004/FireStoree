package com.example.md_08_ungdungfivestore.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Cart {
    @SerializedName("_id")
    private String id;
    @SerializedName("user_id")
    private String userId;
    private List<CartItem> items;
    // Có thể thêm createdAt, updatedAt nếu cần

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}