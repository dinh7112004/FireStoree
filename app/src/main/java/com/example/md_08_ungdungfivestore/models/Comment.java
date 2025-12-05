package com.example.md_08_ungdungfivestore.models;



public class Comment {
    private String id;
    private String productId;
    private String userName;
    private String content;
    private float rating; // điểm đánh giá (1-5)
    private String date;

    public Comment() {}

    public Comment(String id, String productId, String userName, String content, float rating, String date) {
        this.id = id;
        this.productId = productId;
        this.userName = userName;
        this.content = content;
        this.rating = rating;
        this.date = date;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

