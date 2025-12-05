package com.example.md_08_ungdungfivestore.models;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("_id")
    private String id; // ID của subdocument (item) trong giỏ hàng

    // SỬA LỖI PARSING: Khai báo product_id là Object ProductDetailInCart
    @SerializedName("product_id")
    private ProductDetailInCart productDetail;

    // Các trường sau nằm trực tiếp trong CartItem object (tương tự như productDetail)
    // Các trường này có thể được sử dụng thay vì lấy từ productDetail
    private String name;
    private double price;
    private int quantity;
    private String size;
    private String color;

    // Getters and Setters
    public String getId() { return id; }

    // Dùng getQuantity/setQuantity cho logic tăng giảm số lượng
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getSize() { return size; }
    public String getColor() { return color; }

    // Getter cho Object chi tiết sản phẩm
    public ProductDetailInCart getProductDetail() { return productDetail; }

    /**
     * Phương thức tiện ích để lấy đường dẫn ảnh từ Object productDetail.
     */
    public String getImage() {
        if (productDetail != null) {
            return productDetail.getImage();
        }
        return null;
    }
}