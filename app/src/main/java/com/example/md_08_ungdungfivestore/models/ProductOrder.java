// File: com.example.md_08_ungdungfivestore.models.ProductOrder.java

package com.example.md_08_ungdungfivestore.models;

// Có thể cần các thư viện này nếu bạn sử dụng Serializable/Parcelable
// import java.io.Serializable; 

public class ProductOrder { // implements Serializable { 

    // Thuộc tính chính
    private String productId;
    private String name;
    private int quantity;
    private double price; // Giá bán tại thời điểm đặt hàng
    private String imageUrl; // URL ảnh của sản phẩm

    // Thuộc tính Chi tiết Phân loại (nếu có)
    private String size;
    private String color;

    // Constructor mặc định (cần thiết cho Firebase/Retrofit/Gson)
    public ProductOrder() {
    }

    // Constructor đầy đủ
    public ProductOrder(String productId, String name, int quantity, double price, String imageUrl, String size, String color) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.size = size;
        this.color = color;
    }

    // --- Getters và Setters ---

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}