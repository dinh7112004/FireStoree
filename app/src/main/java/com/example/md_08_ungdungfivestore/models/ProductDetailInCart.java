package com.example.md_08_ungdungfivestore.models;

import com.google.gson.annotations.SerializedName;

// Đại diện cho Object phức tạp nằm trong trường "product_id"
public class ProductDetailInCart {
    @SerializedName("_id")
    private String id;

    private String name;
    private String image; // Đây là đường dẫn ảnh bạn cần
    private double price;
    // Có thể thêm List<Variation> variations nếu bạn cần thông tin này

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getImage() { return image; }
    public double getPrice() { return price; }

    // (Thêm setters nếu cần)
}