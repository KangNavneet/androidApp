package com.example.whatsapp;

public class whatsappDataEntry {

    public String mobileNo;
    public String fullName;
    public String emailId;
    public String photo;
    public String status;
    public String photopath = "https://firebasestorage.googleapis.com/v0/b/whatsapp-146bb.appspot.com/o/mobile-organizers.png?alt=media&token=ecad4329-7f2d-4ae3-b0b8-7309c6e56c89";

    public whatsappDataEntry(String mobileNo, String fullName, String emailId, String photo, String status,String photopath) {
        this.mobileNo = mobileNo;
        this.fullName = fullName;
        this.emailId = emailId;
        this.photo = photo;
        this.status = status;
        this.photopath = photopath;
    }

    public whatsappDataEntry() {

    }


    public String getMobileNo() {
        return mobileNo;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getPhoto() {
        return photo;
    }

    public String getStatus() {
        return status;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }
}
