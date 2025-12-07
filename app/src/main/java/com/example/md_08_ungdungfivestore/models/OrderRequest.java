package com.example.md_08_ungdungfivestore.models;

import java.util.List;

public class OrderRequest {

    private Address shippingAddress;
    private String paymentMethod;
    private String note;
    private List<CartItem> items;

    // ⭐ ĐÃ THÊM: Phí vận chuyển và Tổng tiền
    private double shipping_fee;
    private double total_amount;

    // ⭐ CONSTRUCTOR ĐÃ CẬP NHẬT (THÊM 2 ĐỐI SỐ MỚI) ⭐
    public OrderRequest(Address shippingAddress, String paymentMethod, String note, List<CartItem> items, double shipping_fee, double total_amount) {
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.note = note;
        this.items = items;
        this.shipping_fee = shipping_fee; // Thêm
        this.total_amount = total_amount; // Thêm
    }

    // GETTERS CŨ
    public Address getShippingAddress() {
        return shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getNote() {
        return note;
    }

    public List<CartItem> getItems() {
        return items;
    }

    // ⭐ GETTERS MỚI ĐÃ THÊM ⭐
    public double getShipping_fee() {
        return shipping_fee;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    // Lưu ý: Bạn có thể thêm Setters nếu cần, nhưng không bắt buộc cho request này.
}