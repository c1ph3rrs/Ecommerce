package com.ciphers.ecommerce.Model;

public class Branch {

    String branchAddress, branchCity, branchCode, branchEmail, branchPhone, password, username;

    public Branch(){}

    public Branch(String branchAddress, String branchCity, String branchCode, String branchEmail, String branchPhone, String password, String username) {
        this.branchAddress = branchAddress;
        this.branchCity = branchCity;
        this.branchCode = branchCode;
        this.branchEmail = branchEmail;
        this.branchPhone = branchPhone;
        this.password = password;
        this.username = username;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchCity() {
        return branchCity;
    }

    public void setBranchCity(String branchCity) {
        this.branchCity = branchCity;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchEmail() {
        return branchEmail;
    }

    public void setBranchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    public void setBranchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
