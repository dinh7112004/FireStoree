package com.example.md_08_ungdungfivestore.models;

import com.google.gson.annotations.SerializedName;

public class OrderItem {

    private String productId;
    private String productName;

    // ⭐ ĐÃ SỬA LỖI ẢNH: Cho phép đọc "imageUrl" (từ Server đã sửa), "image" (từ Server cũ) và "image_url"
    @SerializedName(value = "imageUrl", alternate = {"image", "image_url"})
    private String imageUrl; // Ảnh sản phẩm

    private String size;
    private String color;

    private int quantity;

    // ⭐ ĐÃ SỬA LỖI GIÁ ĐƠN VỊ: Server Node.js trả về trường "price".
    @SerializedName(value = "unitPrice", alternate = {"price"})
    private double unitPrice; // Giá bán lẻ tại thời điểm đặt hàng (Server trả về là 'price')

    private double subtotal; // Thành tiền (quantity * unitPrice)

    // Constructor rỗng (cần thiết cho Retrofit/Gson)
    public OrderItem() {
    }

    // Constructor đầy đủ
    public OrderItem(String productId, String productName, String imageUrl, String size, String color, int quantity, double unitPrice, double subtotal) {
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    // --- GETTERS ---

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }

    // --- SETTERS --- (Cần thiết cho Deserialization)

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}