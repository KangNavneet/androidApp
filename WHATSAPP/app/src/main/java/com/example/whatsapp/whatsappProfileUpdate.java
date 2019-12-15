package com.example.whatsapp;

public class whatsappProfileUpdate {

   public  String email,name,status,profileImage;

    public whatsappProfileUpdate(String email, String name, String status, String profileImage) {
        this.email = email;
        this.name = name;
        this.status = status;
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
