package com.example.md_08_ungdungfivestore.models;

import com.google.gson.annotations.SerializedName; // <--- SỬA 1: THÊM IMPORT
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Product implements Serializable {

    @SerializedName("_id") // <--- SỬA 2: Ánh xạ JSON field "_id" vào Java field "id"
    private String id;

    private String name;
    private String image;
    private List<String> images;
    private List<Description> description;
    private double importPrice;
    private double price;
    private int quantity = 0; // tổng số lượng sản phẩm (tạm thời)
    private int sold = 0;
    private String category;
    private String brand;
    private List<Variation> variations;
    private String status = "Đang bán";
    private boolean isFeatured = false;
    private String createdAt;
    private String updatedAt;

    public Product() { }

    // Getter/Setter các trường hiện có

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public List<Description> getDescription() { return description; }
    public void setDescription(List<Description> description) { this.description = description; }

    public double getImportPrice() { return importPrice; }
    public void setImportPrice(double importPrice) { this.importPrice = importPrice; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getSold() { return sold; }
    public void setSold(int sold) { this.sold = sold; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public List<Variation> getVariations() { return variations; }
    public void setVariations(List<Variation> variations) { this.variations = variations; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    // -------------------------------
    // Phương thức tiện ích cho SelectOptionsActivity

    /** Trả về danh sách size có sẵn không trùng lặp */
    public List<String> getAvailableSizes() {
        HashSet<String> sizes = new HashSet<>();
        if (variations != null) {
            for (Variation v : variations) {
                if (v.getSize() != null) {
                    sizes.add(v.getSize());
                }
            }
        }
        return new ArrayList<>(sizes);
    }

    /** Trả về danh sách màu có sẵn không trùng lặp */
    public List<String> getAvailableColors() {
        HashSet<String> colors = new HashSet<>();
        if (variations != null) {
            for (Variation v : variations) {
                if (v.getColor() != null) {
                    colors.add(v.getColor());
                }
            }
        }
        return new ArrayList<>(colors);
    }

    /** Trả về map key="size-color", value = số lượng */
    public Map<String, Integer> getQuantityMap() {
        Map<String, Integer> map = new HashMap<>();
        if (variations != null) {
            for (Variation v : variations) {
                String size = v.getSize() != null ? v.getSize() : "";
                String color = v.getColor() != null ? v.getColor() : "";
                String key = size + "-" + color;
                map.put(key, v.getQuantity());
            }
        }
        return map;
    }

    /** Tổng số lượng trong kho (tổng quantity tất cả variations) */
    public int getTotalQuantity() {
        int total = 0;
        if (variations != null) {
            for (Variation v : variations) {
                total += v.getQuantity();
            }
        }
        return total;
    }

    // -------------------------------
    // Inner classes

    public static class Description implements Serializable {
        private String field;
        private String value;

        public Description() { }

        public Description(String field, String value) {
            this.field = field;
            this.value = value;
        }

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }

    public static class Variation implements Serializable {
        private String color;
        private String size;
        private int quantity;

        public Variation() { }

        public Variation(String color, String size, int quantity) {
            this.color = color;
            this.size = size;
            this.quantity = quantity;
        }

        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }

        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}