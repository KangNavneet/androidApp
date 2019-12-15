package com.example.whatsapp;

public class contactClass {
String fullName;
String mobileNo;
String status;
String photopath;
String emailId;
String photo;


    public contactClass() {
        this.fullName = fullName;
        this.mobileNo = mobileNo;
        this.status = status;
        this.photopath = photopath;
        this.emailId = emailId;
        this.photo = photo;
    }

    public contactClass(String fullName, String mobileNo, String status, String photopath, String emailId, String photo) {
        this.fullName = fullName;
        this.mobileNo = mobileNo;
        this.status = status;
        this.photopath = photopath;
        this.emailId = emailId;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailid) {
        this.emailId = emailId;
    }
}
