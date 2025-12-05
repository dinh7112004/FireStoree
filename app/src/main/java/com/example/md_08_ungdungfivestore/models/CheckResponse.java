package com.example.md_08_ungdungfivestore.models;

public class CheckResponse {
    private boolean inWishlist;

    public CheckResponse() { }

    public CheckResponse(boolean inWishlist) {
        this.inWishlist = inWishlist;
    }

    public boolean isInWishlist() {
        return inWishlist;
    }

    public void setInWishlist(boolean inWishlist) {
        this.inWishlist = inWishlist;
    }
}
