package com.example.md_08_ungdungfivestore.models;

import com.google.gson.annotations.SerializedName;

public class CartRequest {

    @SerializedName("product_id")
    private String productId;

    // ⭐ TRƯỜNG MỚI BỊ THIẾU ⭐
    private String name;

    private String size;

    private String color;

    private int quantity;

    // ⭐ TRƯỜNG MỚI BỊ THIẾU ⭐
    private double price;

    // Constructor MỚI (Thêm name và price)
    public CartRequest(String productId, String name, String size, String color, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.price = price;
    }

    // (Bạn có thể thêm Getters nếu cần, nhưng không bắt buộc cho request model)
}