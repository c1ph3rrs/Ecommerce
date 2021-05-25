package com.ciphers.ecommerce.Model;

public class DeliveryGuy {

    String deliveryGuyAddress, deliveryGuyEmail, password, deliveryGuyFullName, deliveryGuyImage, deliveryGuyPhone,deliveryGuyStatus, deliveryGuyUsername;

    public DeliveryGuy(){}

    public DeliveryGuy(String deliveryGuyAddress, String deliveryGuyEmail, String password, String deliveryGuyFullName, String deliveryGuyImage, String deliveryGuyPhone, String deliveryGuyStatus, String deliveryGuyUsername) {
        this.deliveryGuyAddress = deliveryGuyAddress;
        this.deliveryGuyEmail = deliveryGuyEmail;
        this.password = password;
        this.deliveryGuyFullName = deliveryGuyFullName;
        this.deliveryGuyImage = deliveryGuyImage;
        this.deliveryGuyPhone = deliveryGuyPhone;
        this.deliveryGuyStatus = deliveryGuyStatus;
        this.deliveryGuyUsername = deliveryGuyUsername;
    }

    public String getDeliveryGuyAddress() {
        return deliveryGuyAddress;
    }

    public void setDeliveryGuyAddress(String deliveryGuyAddress) {
        this.deliveryGuyAddress = deliveryGuyAddress;
    }

    public String getDeliveryGuyEmail() {
        return deliveryGuyEmail;
    }

    public void setDeliveryGuyEmail(String deliveryGuyEmail) {
        this.deliveryGuyEmail = deliveryGuyEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeliveryGuyFullName() {
        return deliveryGuyFullName;
    }

    public void setDeliveryGuyFullName(String deliveryGuyFullName) {
        this.deliveryGuyFullName = deliveryGuyFullName;
    }

    public String getDeliveryGuyImage() {
        return deliveryGuyImage;
    }

    public void setDeliveryGuyImage(String deliveryGuyImage) {
        this.deliveryGuyImage = deliveryGuyImage;
    }

    public String getDeliveryGuyPhone() {
        return deliveryGuyPhone;
    }

    public void setDeliveryGuyPhone(String deliveryGuyPhone) {
        this.deliveryGuyPhone = deliveryGuyPhone;
    }

    public String getDeliveryGuyStatus() {
        return deliveryGuyStatus;
    }

    public void setDeliveryGuyStatus(String deliveryGuyStatus) {
        this.deliveryGuyStatus = deliveryGuyStatus;
    }

    public String getDeliveryGuyUsername() {
        return deliveryGuyUsername;
    }

    public void setDeliveryGuyUsername(String deliveryGuyUsername) {
        this.deliveryGuyUsername = deliveryGuyUsername;
    }
}
