package com.ciphers.ecommerce.Model;

public class Sellers {

//    String sellerName, sellerUsername, password, sellerShopName, sellerShopStatus, sellerShopEmail, sellerShopPhone, sellerShopLocation, sellerShopIdentity, shopImage, sellerJoin;

    String password, sellerJoin, sellerName, sellerShopCategory, sellerShopEmail, sellerShopIdentity, sellerShopLocation, sellerShopName, sellerShopPhone, sellerStatus, sellerUsername, shopImage;

    public Sellers(){}

    public Sellers(String password, String sellerJoin, String sellerName, String sellerShopCategory, String sellerShopEmail, String sellerShopIdentity, String sellerShopLocation, String sellerShopName, String sellerShopPhone, String sellerStatus, String sellerUsername, String shopImage) {
        this.password = password;
        this.sellerJoin = sellerJoin;
        this.sellerName = sellerName;
        this.sellerShopCategory = sellerShopCategory;
        this.sellerShopEmail = sellerShopEmail;
        this.sellerShopIdentity = sellerShopIdentity;
        this.sellerShopLocation = sellerShopLocation;
        this.sellerShopName = sellerShopName;
        this.sellerShopPhone = sellerShopPhone;
        this.sellerStatus = sellerStatus;
        this.sellerUsername = sellerUsername;
        this.shopImage = shopImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSellerJoin() {
        return sellerJoin;
    }

    public void setSellerJoin(String sellerJoin) {
        this.sellerJoin = sellerJoin;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerShopCategory() {
        return sellerShopCategory;
    }

    public void setSellerShopCategory(String sellerShopCategory) {
        this.sellerShopCategory = sellerShopCategory;
    }

    public String getSellerShopEmail() {
        return sellerShopEmail;
    }

    public void setSellerShopEmail(String sellerShopEmail) {
        this.sellerShopEmail = sellerShopEmail;
    }

    public String getSellerShopIdentity() {
        return sellerShopIdentity;
    }

    public void setSellerShopIdentity(String sellerShopIdentity) {
        this.sellerShopIdentity = sellerShopIdentity;
    }

    public String getSellerShopLocation() {
        return sellerShopLocation;
    }

    public void setSellerShopLocation(String sellerShopLocation) {
        this.sellerShopLocation = sellerShopLocation;
    }

    public String getSellerShopName() {
        return sellerShopName;
    }

    public void setSellerShopName(String sellerShopName) {
        this.sellerShopName = sellerShopName;
    }

    public String getSellerShopPhone() {
        return sellerShopPhone;
    }

    public void setSellerShopPhone(String sellerShopPhone) {
        this.sellerShopPhone = sellerShopPhone;
    }

    public String getSellerStatus() {
        return sellerStatus;
    }

    public void setSellerStatus(String sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }
}
