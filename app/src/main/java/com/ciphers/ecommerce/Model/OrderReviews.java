package com.ciphers.ecommerce.Model;

public class OrderReviews {

    String buyerName, ratting, review, sellerName;

    public OrderReviews(){}

    public OrderReviews(String buyerName, String ratting, String review, String sellerName) {
        this.buyerName = buyerName;
        this.ratting = ratting;
        this.review = review;
        this.sellerName = sellerName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
