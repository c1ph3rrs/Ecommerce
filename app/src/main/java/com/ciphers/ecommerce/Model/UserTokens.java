package com.ciphers.ecommerce.Model;

public class UserTokens {

    String buyerToken;

    public UserTokens(){}

    public UserTokens(String buyerToken) {
        this.buyerToken = buyerToken;
    }

    public String getBuyerToken() {
        return buyerToken;
    }

    public void setBuyerToken(String buyerToken) {
        this.buyerToken = buyerToken;
    }
}
