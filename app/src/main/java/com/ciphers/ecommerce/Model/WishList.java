package com.ciphers.ecommerce.Model;

public class WishList {

    String productID, productTitle;

    public WishList(){}

    public WishList(String productID, String productTitle) {
        this.productID = productID;
        this.productTitle = productTitle;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
}
