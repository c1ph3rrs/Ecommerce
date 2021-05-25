package com.ciphers.ecommerce.Model;

public class Collection {

    String collectionProductImage, collectionProductCategory, collectionProductName, collectionProductPrice, collectionProductDescription, collectionProductQuantity;


    public Collection(){

    }

    public Collection(String collectionProductImage, String collectionProductCategory, String collectionProductName, String collectionProductPrice, String collectionProductDescription, String collectionProductQuantity) {
        this.collectionProductImage = collectionProductImage;
        this.collectionProductCategory = collectionProductCategory;
        this.collectionProductName = collectionProductName;
        this.collectionProductPrice = collectionProductPrice;
        this.collectionProductDescription = collectionProductDescription;
        this.collectionProductQuantity = collectionProductQuantity;
    }

    public String getCollectionProductImage() {
        return collectionProductImage;
    }

    public void setCollectionProductImage(String collectionProductImage) {
        this.collectionProductImage = collectionProductImage;
    }

    public String getCollectionProductCategory() {
        return collectionProductCategory;
    }

    public void setCollectionProductCategory(String collectionProductCategory) {
        this.collectionProductCategory = collectionProductCategory;
    }

    public String getCollectionProductName() {
        return collectionProductName;
    }

    public void setCollectionProductName(String collectionProductName) {
        this.collectionProductName = collectionProductName;
    }

    public String getCollectionProductPrice() {
        return collectionProductPrice;
    }

    public void setCollectionProductPrice(String collectionProductPrice) {
        this.collectionProductPrice = collectionProductPrice;
    }

    public String getCollectionProductDescription() {
        return collectionProductDescription;
    }

    public void setCollectionProductDescription(String collectionProductDescription) {
        this.collectionProductDescription = collectionProductDescription;
    }

    public String getCollectionProductQuantity() {
        return collectionProductQuantity;
    }

    public void setCollectionProductQuantity(String collectionProductQuantity) {
        this.collectionProductQuantity = collectionProductQuantity;
    }
}
