package com.ciphers.ecommerce.Model;

public class ThirdCategory {

    String categoryID, productCategory,parentCategory, productImage;

    public ThirdCategory(){}

    public ThirdCategory(String categoryID, String productCategory, String parentCategory, String productImage) {
        this.categoryID = categoryID;
        this.productCategory = productCategory;
        this.parentCategory = parentCategory;
        this.productImage = productImage;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
