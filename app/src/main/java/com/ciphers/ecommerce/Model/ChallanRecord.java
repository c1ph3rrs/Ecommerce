package com.ciphers.ecommerce.Model;

public class ChallanRecord {

    String challanDesc, challanId, imageUrl;

    public ChallanRecord(){}

    public ChallanRecord(String challanDesc, String challanId, String challanImage, String imageUrl) {
        this.challanDesc = challanDesc;
        this.challanId = challanId;
        this.imageUrl = imageUrl;
    }

    public String getChallanDesc() {
        return challanDesc;
    }

    public void setChallanDesc(String challanDesc) {
        this.challanDesc = challanDesc;
    }

    public String getChallanId() {
        return challanId;
    }

    public void setChallanId(String challanId) {
        this.challanId = challanId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
