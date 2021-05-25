package com.ciphers.ecommerce.Model;

public class ClosedBuyerLocations {

    String buyerUsername;

    public ClosedBuyerLocations() {
    }

    public ClosedBuyerLocations(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }

}
