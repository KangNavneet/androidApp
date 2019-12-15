package com.example.whatsapp;

public class recentChat {
    String fullName;
    String photoPath;
    String dateTime;
    String msgText;
    String msgAttach;
    String msgType;
    String msgTo;


    public recentChat(String fullName, String photoPath, String dateTime, String msgText, String msgAttach,String msgType,String msgTo) {
        this.fullName = fullName;
        this.photoPath = photoPath;
        this.dateTime = dateTime;
        this.msgText = msgText;
        this.msgAttach = msgAttach;
        this.msgType = msgType;
        this.msgTo = msgTo;
    }

    public String getMsgTo() {
        return msgTo;
    }

    public void setMsgTo(String msgTo) {
        this.msgTo = msgTo;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMsgAttach() {
        return msgAttach;
    }

    public void setMsgAttach(String msgAttach) {
        this.msgAttach = msgAttach;
    }
}
