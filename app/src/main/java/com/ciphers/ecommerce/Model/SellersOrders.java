package com.ciphers.ecommerce.Model;

public class SellersOrders {

    public String orderBuyerName, orderDate, orderID, orderImg, orderName,orderPaymentType, orderPrice, orderQty, orderSellerName, orderShippingID, orderStatus ,orderTotalPrice, orderType;

    public SellersOrders(){}

    public SellersOrders(String orderBuyerName, String orderDate, String orderID, String orderImg, String orderName, String orderPaymentType, String orderPrice, String orderQty, String orderSellerName, String orderShippingID, String orderStatus, String orderTotalPrice, String orderType) {
        this.orderBuyerName = orderBuyerName;
        this.orderDate = orderDate;
        this.orderID = orderID;
        this.orderImg = orderImg;
        this.orderName = orderName;
        this.orderPaymentType = orderPaymentType;
        this.orderPrice = orderPrice;
        this.orderQty = orderQty;
        this.orderSellerName = orderSellerName;
        this.orderShippingID = orderShippingID;
        this.orderStatus = orderStatus;
        this.orderTotalPrice = orderTotalPrice;
        this.orderType = orderType;
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

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
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

    public String getOrderPaymentType() {
        return orderPaymentType;
    }

    public void setOrderPaymentType(String orderPaymentType) {
        this.orderPaymentType = orderPaymentType;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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
}
