package com.ciphers.ecommerce.Model;

public class WithdrawRequest {

    String paymentID, sellerAccount, sellerBank, sellerPayment, sellerUsername, sellerWithdrawDate;

    public WithdrawRequest(){}

    public WithdrawRequest(String paymentID, String sellerAccount, String sellerBank, String sellerPayment, String sellerUsername, String sellerWithdrawDate) {
        this.paymentID = paymentID;
        this.sellerAccount = sellerAccount;
        this.sellerBank = sellerBank;
        this.sellerPayment = sellerPayment;
        this.sellerUsername = sellerUsername;
        this.sellerWithdrawDate = sellerWithdrawDate;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public String getSellerBank() {
        return sellerBank;
    }

    public void setSellerBank(String sellerBank) {
        this.sellerBank = sellerBank;
    }

    public String getSellerPayment() {
        return sellerPayment;
    }

    public void setSellerPayment(String sellerPayment) {
        this.sellerPayment = sellerPayment;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public String getSellerWithdrawDate() {
        return sellerWithdrawDate;
    }

    public void setSellerWithdrawDate(String sellerWithdrawDate) {
        this.sellerWithdrawDate = sellerWithdrawDate;
    }
}
