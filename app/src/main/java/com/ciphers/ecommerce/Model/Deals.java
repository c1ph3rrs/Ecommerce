package com.ciphers.ecommerce.Model;

public class Deals {

    String dealProductId, dealProductImage1, dealProductImage2, dealProductImage3, dealProductImage4, dealProductName, dealProductPrice, dealProductDescription,dealProductQuantity,dealProductQuantityLeft, dealProductSellerID;

    public Deals(){

    }

    public Deals(String dealProductId, String dealProductImage1, String dealProductImage2, String dealProductImage3, String dealProductImage4, String dealProductName, String dealProductPrice, String dealProductDescription, String dealProductQuantity, String dealProductQuantityLeft, String dealProductSellerID) {
        this.dealProductId = dealProductId;
        this.dealProductImage1 = dealProductImage1;
        this.dealProductImage2 = dealProductImage2;
        this.dealProductImage3 = dealProductImage3;
        this.dealProductImage4 = dealProductImage4;
        this.dealProductName = dealProductName;
        this.dealProductPrice = dealProductPrice;
        this.dealProductDescription = dealProductDescription;
        this.dealProductQuantity = dealProductQuantity;
        this.dealProductQuantityLeft = dealProductQuantityLeft;
        this.dealProductSellerID = dealProductSellerID;
    }

    public String getDealProductId() {
        return dealProductId;
    }

    public void setDealProductId(String dealProductId) {
        this.dealProductId = dealProductId;
    }

    public String getDealProductImage1() {
        return dealProductImage1;
    }

    public void setDealProductImage1(String dealProductImage1) {
        this.dealProductImage1 = dealProductImage1;
    }

    public String getDealProductImage2() {
        return dealProductImage2;
    }

    public void setDealProductImage2(String dealProductImage2) {
        this.dealProductImage2 = dealProductImage2;
    }

    public String getDealProductImage3() {
        return dealProductImage3;
    }

    public void setDealProductImage3(String dealProductImage3) {
        this.dealProductImage3 = dealProductImage3;
    }

    public String getDealProductImage4() {
        return dealProductImage4;
    }

    public void setDealProductImage4(String dealProductImage4) {
        this.dealProductImage4 = dealProductImage4;
    }

    public String getDealProductName() {
        return dealProductName;
    }

    public void setDealProductName(String dealProductName) {
        this.dealProductName = dealProductName;
    }

    public String getDealProductPrice() {
        return dealProductPrice;
    }

    public void setDealProductPrice(String dealProductPrice) {
        this.dealProductPrice = dealProductPrice;
    }

    public String getDealProductDescription() {
        return dealProductDescription;
    }

    public void setDealProductDescription(String dealProductDescription) {
        this.dealProductDescription = dealProductDescription;
    }

    public String getDealProductQuantity() {
        return dealProductQuantity;
    }

    public void setDealProductQuantity(String dealProductQuantity) {
        this.dealProductQuantity = dealProductQuantity;
    }

    public String getDealProductQuantityLeft() {
        return dealProductQuantityLeft;
    }

    public void setDealProductQuantityLeft(String dealProductQuantityLeft) {
        this.dealProductQuantityLeft = dealProductQuantityLeft;
    }

    public String getDealProductSellerID() {
        return dealProductSellerID;
    }

    public void setDealProductSellerID(String dealProductSellerID) {
        this.dealProductSellerID = dealProductSellerID;
    }
}
