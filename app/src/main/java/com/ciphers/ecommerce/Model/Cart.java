package com.ciphers.ecommerce.Model;

public class Cart {

    String productBuyer, productDate, productID, productImg, productName, productPrice, productQuantity, productSeller, productTime, productType;

    public Cart(){}

    public Cart(String productBuyer, String productDate, String productID, String productImg, String productName, String productPrice, String productQuantity, String productSeller, String productTime, String productType) {
        this.productBuyer = productBuyer;
        this.productDate = productDate;
        this.productID = productID;
        this.productImg = productImg;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSeller = productSeller;
        this.productTime = productTime;
        this.productType = productType;
    }

    public String getProductBuyer() {
        return productBuyer;
    }

    public void setProductBuyer(String productBuyer) {
        this.productBuyer = productBuyer;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductSeller() {
        return productSeller;
    }

    public void setProductSeller(String productSeller) {
        this.productSeller = productSeller;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
