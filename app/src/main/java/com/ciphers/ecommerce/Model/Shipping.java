package com.ciphers.ecommerce.Model;

public class Shipping {

    public String receiverCity, receiverName, receiverPhone, senderCity, senderName, senderPhone, shippingAddress, shippingID;

    public Shipping(){}

    public Shipping(String receiverCity, String receiverName, String receiverPhone, String senderCity, String senderName, String senderPhone, String shippingAddress, String shippingID) {
        this.receiverCity = receiverCity;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.senderCity = senderCity;
        this.senderName = senderName;
        this.senderPhone = senderPhone;
        this.shippingAddress = shippingAddress;
        this.shippingID = shippingID;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingID() {
        return shippingID;
    }

    public void setShippingID(String shippingID) {
        this.shippingID = shippingID;
    }
}
