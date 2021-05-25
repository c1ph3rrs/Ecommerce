package com.ciphers.ecommerce.Model;

public class Users {

    public String email, fullName, password, phoneNo, username, image, userJoin;

    public Users(){}


    public Users(String email, String fullName, String password, String phoneNo, String username, String image, String userJoin) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phoneNo = phoneNo;
        this.username = username;
        this.image = image;
        this.userJoin = userJoin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserJoin() {
        return userJoin;
    }

    public void setUserJoin(String userJoin) {
        this.userJoin = userJoin;
    }
}
