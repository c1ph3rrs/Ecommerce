package com.ciphers.ecommerce.Model;

public class PlacedOrder {

    public String orderShippingID, productDate, productImg, productName, productOrderID, productOrderPayment, productPrice, productQty, productSellerName, productTotalPrice, productType;

    public PlacedOrder(){}

    public PlacedOrder(String orderShippingID, String productDate, String productImg, String productName, String productOrderID, String productOrderPayment, String productPrice, String productQty, String productSellerName, String productTotalPrice, String productType) {
        this.orderShippingID = orderShippingID;
        this.productDate = productDate;
        this.productImg = productImg;
        this.productName = productName;
        this.productOrderID = productOrderID;
        this.productOrderPayment = productOrderPayment;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.productSellerName = productSellerName;
        this.productTotalPrice = productTotalPrice;
        this.productType = productType;
    }

    public String getOrderShippingID() {
        return orderShippingID;
    }

    public void setOrderShippingID(String orderShippingID) {
        this.orderShippingID = orderShippingID;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
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

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductSellerName() {
        return productSellerName;
    }

    public void setProductSellerName(String productSellerName) {
        this.productSellerName = productSellerName;
    }

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
