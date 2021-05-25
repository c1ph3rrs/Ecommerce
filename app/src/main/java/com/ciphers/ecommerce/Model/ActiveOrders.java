package com.ciphers.ecommerce.Model;

public class ActiveOrders {

    String productBuyer, productDate, productID, productImg, productName, productOrderID,productOrderPayment, productPrice, productQuantity, productSeller, productShippingID, productType;

    public ActiveOrders(){}

    public ActiveOrders(String productBuyer, String productDate, String productID, String productImg, String productName, String productOrderID, String productOrderPayment, String productPrice, String productQuantity, String productSeller, String productShippingID, String productType) {
        this.productBuyer = productBuyer;
        this.productDate = productDate;
        this.productID = productID;
        this.productImg = productImg;
        this.productName = productName;
        this.productOrderID = productOrderID;
        this.productOrderPayment = productOrderPayment;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSeller = productSeller;
        this.productShippingID = productShippingID;
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

    public String getProductOrderID() {
        return productOrderID;
    }

    public void setProductOrderID(String productOrderID) {
        this.productOrderID = productOrderID;
    }

    public String getProductOrderPayment() {
        return productOrderPayment;
    }

    public void setProductOrderPayment(String productOrderPayment) {
        this.productOrderPayment = productOrderPayment;
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

    public String getProductShippingID() {
        return productShippingID;
    }

    public void setProductShippingID(String productShippingID) {
        this.productShippingID = productShippingID;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
