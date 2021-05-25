package com.ciphers.ecommerce.Model;

public class Products {

    private String productId, productImage1, productImage2, productImage3, productImage4, productCategory,
            productName,productParentCategory, productPrice, productDescription, productQuantity, productQuantityLeft,
            productSellerID, productThirdCategory;

    public Products(){}

    public Products(String productId, String productImage1, String productImage2, String productImage3, String productImage4, String productCategory, String productName, String productParentCategory, String productPrice, String productDescription, String productQuantity, String productQuantityLeft, String productSellerID, String productThirdCategory) {
        this.productId = productId;
        this.productImage1 = productImage1;
        this.productImage2 = productImage2;
        this.productImage3 = productImage3;
        this.productImage4 = productImage4;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productParentCategory = productParentCategory;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
        this.productQuantityLeft = productQuantityLeft;
        this.productSellerID = productSellerID;
        this.productThirdCategory = productThirdCategory;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage1() {
        return productImage1;
    }

    public void setProductImage1(String productImage1) {
        this.productImage1 = productImage1;
    }

    public String getProductImage2() {
        return productImage2;
    }

    public void setProductImage2(String productImage2) {
        this.productImage2 = productImage2;
    }

    public String getProductImage3() {
        return productImage3;
    }

    public void setProductImage3(String productImage3) {
        this.productImage3 = productImage3;
    }

    public String getProductImage4() {
        return productImage4;
    }

    public void setProductImage4(String productImage4) {
        this.productImage4 = productImage4;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductParentCategory() {
        return productParentCategory;
    }

    public void setProductParentCategory(String productParentCategory) {
        this.productParentCategory = productParentCategory;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductQuantityLeft() {
        return productQuantityLeft;
    }

    public void setProductQuantityLeft(String productQuantityLeft) {
        this.productQuantityLeft = productQuantityLeft;
    }

    public String getProductSellerID() {
        return productSellerID;
    }

    public void setProductSellerID(String productSellerID) {
        this.productSellerID = productSellerID;
    }

    public String getProductThirdCategory() {
        return productThirdCategory;
    }

    public void setProductThirdCategory(String productThirdCategory) {
        this.productThirdCategory = productThirdCategory;
    }
}
