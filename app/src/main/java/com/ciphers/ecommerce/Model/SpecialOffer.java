package com.ciphers.ecommerce.Model;

public class SpecialOffer {

    String productCategory, productParentCategory, productThirdCategory, specialOfferProductId, specialProductImage1,specialProductImage2,specialProductImage3,specialProductImage4,
        specialProductName, specialProductPrice, specialProductDescription, specialProductQuantity, specialProductQuantityLeft, specialProductSellerID;

    public  SpecialOffer(){

    }

    public SpecialOffer(String productCategory, String productParentCategory, String productThirdCategory, String specialOfferProductId, String specialProductImage1, String specialProductImage2, String specialProductImage3, String specialProductImage4, String specialProductName, String specialProductPrice, String specialProductDescription, String specialProductQuantity, String specialProductQuantityLeft, String specialProductSellerID) {
        this.productCategory = productCategory;
        this.productParentCategory = productParentCategory;
        this.productThirdCategory = productThirdCategory;
        this.specialOfferProductId = specialOfferProductId;
        this.specialProductImage1 = specialProductImage1;
        this.specialProductImage2 = specialProductImage2;
        this.specialProductImage3 = specialProductImage3;
        this.specialProductImage4 = specialProductImage4;
        this.specialProductName = specialProductName;
        this.specialProductPrice = specialProductPrice;
        this.specialProductDescription = specialProductDescription;
        this.specialProductQuantity = specialProductQuantity;
        this.specialProductQuantityLeft = specialProductQuantityLeft;
        this.specialProductSellerID = specialProductSellerID;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductParentCategory() {
        return productParentCategory;
    }

    public void setProductParentCategory(String productParentCategory) {
        this.productParentCategory = productParentCategory;
    }

    public String getProductThirdCategory() {
        return productThirdCategory;
    }

    public void setProductThirdCategory(String productThirdCategory) {
        this.productThirdCategory = productThirdCategory;
    }

    public String getSpecialOfferProductId() {
        return specialOfferProductId;
    }

    public void setSpecialOfferProductId(String specialOfferProductId) {
        this.specialOfferProductId = specialOfferProductId;
    }

    public String getSpecialProductImage1() {
        return specialProductImage1;
    }

    public void setSpecialProductImage1(String specialProductImage1) {
        this.specialProductImage1 = specialProductImage1;
    }

    public String getSpecialProductImage2() {
        return specialProductImage2;
    }

    public void setSpecialProductImage2(String specialProductImage2) {
        this.specialProductImage2 = specialProductImage2;
    }

    public String getSpecialProductImage3() {
        return specialProductImage3;
    }

    public void setSpecialProductImage3(String specialProductImage3) {
        this.specialProductImage3 = specialProductImage3;
    }

    public String getSpecialProductImage4() {
        return specialProductImage4;
    }

    public void setSpecialProductImage4(String specialProductImage4) {
        this.specialProductImage4 = specialProductImage4;
    }

    public String getSpecialProductName() {
        return specialProductName;
    }

    public void setSpecialProductName(String specialProductName) {
        this.specialProductName = specialProductName;
    }

    public String getSpecialProductPrice() {
        return specialProductPrice;
    }

    public void setSpecialProductPrice(String specialProductPrice) {
        this.specialProductPrice = specialProductPrice;
    }

    public String getSpecialProductDescription() {
        return specialProductDescription;
    }

    public void setSpecialProductDescription(String specialProductDescription) {
        this.specialProductDescription = specialProductDescription;
    }

    public String getSpecialProductQuantity() {
        return specialProductQuantity;
    }

    public void setSpecialProductQuantity(String specialProductQuantity) {
        this.specialProductQuantity = specialProductQuantity;
    }

    public String getSpecialProductQuantityLeft() {
        return specialProductQuantityLeft;
    }

    public void setSpecialProductQuantityLeft(String specialProductQuantityLeft) {
        this.specialProductQuantityLeft = specialProductQuantityLeft;
    }

    public String getSpecialProductSellerID() {
        return specialProductSellerID;
    }

    public void setSpecialProductSellerID(String specialProductSellerID) {
        this.specialProductSellerID = specialProductSellerID;
    }
}
