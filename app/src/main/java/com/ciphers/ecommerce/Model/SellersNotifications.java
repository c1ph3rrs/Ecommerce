package com.ciphers.ecommerce.Model;

public class SellersNotifications {

    public String notificationSeller,notificationSellerBuyer, notificationSellerDate, notificationSellerImg, notificationSellerKey, notificationSellerOrderKey,
            notificationSellerStatus, notificationSellerTitle, notificationSellerType;

    public SellersNotifications(){}

    public SellersNotifications(String notificationSeller, String notificationSellerBuyer, String notificationSellerDate, String notificationSellerImg, String notificationSellerKey, String notificationSellerOrderKey, String notificationSellerStatus, String notificationSellerTitle, String notificationSellerType) {
        this.notificationSeller = notificationSeller;
        this.notificationSellerBuyer = notificationSellerBuyer;
        this.notificationSellerDate = notificationSellerDate;
        this.notificationSellerImg = notificationSellerImg;
        this.notificationSellerKey = notificationSellerKey;
        this.notificationSellerOrderKey = notificationSellerOrderKey;
        this.notificationSellerStatus = notificationSellerStatus;
        this.notificationSellerTitle = notificationSellerTitle;
        this.notificationSellerType = notificationSellerType;
    }

    public String getNotificationSeller() {
        return notificationSeller;
    }

    public void setNotificationSeller(String notificationSeller) {
        this.notificationSeller = notificationSeller;
    }

    public String getNotificationSellerBuyer() {
        return notificationSellerBuyer;
    }

    public void setNotificationSellerBuyer(String notificationSellerBuyer) {
        this.notificationSellerBuyer = notificationSellerBuyer;
    }

    public String getNotificationSellerDate() {
        return notificationSellerDate;
    }

    public void setNotificationSellerDate(String notificationSellerDate) {
        this.notificationSellerDate = notificationSellerDate;
    }

    public String getNotificationSellerImg() {
        return notificationSellerImg;
    }

    public void setNotificationSellerImg(String notificationSellerImg) {
        this.notificationSellerImg = notificationSellerImg;
    }

    public String getNotificationSellerKey() {
        return notificationSellerKey;
    }

    public void setNotificationSellerKey(String notificationSellerKey) {
        this.notificationSellerKey = notificationSellerKey;
    }

    public String getNotificationSellerOrderKey() {
        return notificationSellerOrderKey;
    }

    public void setNotificationSellerOrderKey(String notificationSellerOrderKey) {
        this.notificationSellerOrderKey = notificationSellerOrderKey;
    }

    public String getNotificationSellerStatus() {
        return notificationSellerStatus;
    }

    public void setNotificationSellerStatus(String notificationSellerStatus) {
        this.notificationSellerStatus = notificationSellerStatus;
    }

    public String getNotificationSellerTitle() {
        return notificationSellerTitle;
    }

    public void setNotificationSellerTitle(String notificationSellerTitle) {
        this.notificationSellerTitle = notificationSellerTitle;
    }

    public String getNotificationSellerType() {
        return notificationSellerType;
    }

    public void setNotificationSellerType(String notificationSellerType) {
        this.notificationSellerType = notificationSellerType;
    }
}
