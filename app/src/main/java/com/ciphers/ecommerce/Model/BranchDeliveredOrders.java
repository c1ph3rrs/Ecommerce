package com.ciphers.ecommerce.Model;

public class BranchDeliveredOrders {
    
    String assignDate, buyer, date, from, id, shipping, to, tracking;

    public BranchDeliveredOrders(){}

    public BranchDeliveredOrders(String assignDate, String buyer, String date, String from, String id, String shipping, String to, String tracking) {
        this.assignDate = assignDate;
        this.buyer = buyer;
        this.date = date;
        this.from = from;
        this.id = id;
        this.shipping = shipping;
        this.to = to;
        this.tracking = tracking;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}
