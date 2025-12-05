package com.example.md_08_ungdungfivestore.models;

import com.google.gson.annotations.SerializedName;

public class AddToCartRequest {
    @SerializedName("product_id")
    private String productId;
    private String name;
    private String image;
    private String size;
    private String color;
    private int quantity;
    private double price;

    public AddToCartRequest(String productId, String name, String image, String size, String color, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.image = image;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.price = price;
    }

    // Không cần Getters/Setters nếu chỉ dùng để gửi request
}