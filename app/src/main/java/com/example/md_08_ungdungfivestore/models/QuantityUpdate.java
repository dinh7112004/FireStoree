package com.example.md_08_ungdungfivestore.models;

// Class dùng để gửi JSON Body cho PUT updateQuantity: { "quantity": X }
public class QuantityUpdate {
    private int quantity;

    public QuantityUpdate(int quantity) {
        this.quantity = quantity;
    }
}