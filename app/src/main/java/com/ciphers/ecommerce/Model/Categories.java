package com.ciphers.ecommerce.Model;

public class Categories {

    String categoryID, productCategory, parentCategory;

    public Categories() {

    }

    public Categories(String categoryID, String productCategory, String parentCategory) {
        this.categoryID = categoryID;
        this.productCategory = productCategory;
        this.parentCategory = parentCategory;
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
}



