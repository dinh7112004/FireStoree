package com.example.md_08_ungdungfivestore.models;

public class Address {

    private String fullName;
    private String phone; // Tên trường lưu số điện thoại
    private String street;
    private String ward;
    private String district;
    private String province;

    // Constructor rỗng (Cần thiết cho Retrofit/Gson)
    public Address() {
    }

    // Constructor đầy đủ
    public Address(String fullName, String phone, String street, String ward, String district, String province) {
        this.fullName = fullName;
        this.phone = phone;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.province = province;
    }

    // --- GETTERS ---

    public String getFullName() {
        return fullName;
    }

    // ⭐ PHƯƠNG THỨC MỚI ĐỂ KHẮC PHỤC LỖI
    public String getPhoneNumber() {
        return phone; // Trả về giá trị của trường 'phone'
    }

    // Getter gốc
    public String getPhone() {
        return phone;
    }

    public String getStreet() {
        return street;
    }

    public String getWard() {
        return ward;
    }

    public String getDistrict() {
        return district;
    }

    public String getProvince() {
        return province;
    }

    // --- SETTERS --- (Cần thiết)

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}