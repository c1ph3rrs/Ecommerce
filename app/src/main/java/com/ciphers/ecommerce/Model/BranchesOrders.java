package com.ciphers.ecommerce.Model;

public class BranchesOrders {

    String date, orderID, sendFrom, sendTo, shippingID, trackingID;

    public BranchesOrders() { }

    public BranchesOrders(String date, String orderID, String sendFrom, String sendTo, String shippingID, String trackingID) {
        this.date = date;
        this.orderID = orderID;
        this.sendFrom = sendFrom;
        this.sendTo = sendTo;
        this.shippingID = shippingID;
        this.trackingID = trackingID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getShippingID() {
        return shippingID;
    }

    public void setShippingID(String shippingID) {
        this.shippingID = shippingID;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }
}
