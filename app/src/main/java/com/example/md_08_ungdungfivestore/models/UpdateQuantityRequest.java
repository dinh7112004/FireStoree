package com.example.md_08_ungdungfivestore.models;

public class UpdateQuantityRequest {
    private int quantity;

    public UpdateQuantityRequest(int quantity) {
        this.quantity = quantity;
    }

    // Không cần Getters/Setters nếu chỉ dùng để gửi request
}