package com.ciphers.ecommerce.Model;

public class Admins {

    String branch, city, email, fullName, password, phoneNo, userImage, username;

    public Admins() {
    }

    public Admins(String branch,String city, String email, String fullName, String password, String phoneNo, String userImage, String username) {
        this.branch = branch;
        this.city = city;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phoneNo = phoneNo;
        this.userImage = userImage;
        this.username = username;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
