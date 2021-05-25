package com.ciphers.ecommerce.Model;

public class RejectOrders {

    String orderBuyerName, orderDate, orderImg, orderName, orderPrice, orderQty, orderSellerName,orderShippingID, orderTotalPrice, orderType, rejectMessage;

    public RejectOrders(){}

    public RejectOrders(String orderBuyerName, String orderDate, String orderImg, String orderName, String orderPrice, String orderQty, String orderSellerName, String orderShippingID, String orderTotalPrice, String orderType, String rejectMessage) {
        this.orderBuyerName = orderBuyerName;
        this.orderDate = orderDate;
        this.orderImg = orderImg;
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.orderQty = orderQty;
        this.orderSellerName = orderSellerName;
        this.orderShippingID = orderShippingID;
        this.orderTotalPrice = orderTotalPrice;
        this.orderType = orderType;
        this.rejectMessage = rejectMessage;
    }

    public String getOrderBuyerName() {
        return orderBuyerName;
    }

    public void setOrderBuyerName(String orderBuyerName) {
        this.orderBuyerName = orderBuyerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderImg() {
        return orderImg;
    }

    public void setOrderImg(String orderImg) {
        this.orderImg = orderImg;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getOrderSellerName() {
        return orderSellerName;
    }

    public void setOrderSellerName(String orderSellerName) {
        this.orderSellerName = orderSellerName;
    }

    public String getOrderShippingID() {
        return orderShippingID;
    }

    public void setOrderShippingID(String orderShippingID) {
        this.orderShippingID = orderShippingID;
    }

    public String getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(String orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }
}
