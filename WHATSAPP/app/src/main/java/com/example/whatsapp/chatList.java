package com.example.whatsapp;

public class chatList {

    String mobilename,displayname,photo,lastmessage,msgType;
    long datetime;
    public chatList() {

    }

    public chatList(String mobilename, String displayname, String photo, String lastmessage, long datetime,String msgType) {
        this.mobilename = mobilename;
        this.displayname = displayname;
        this.photo = photo;
        this.lastmessage = lastmessage;
        this.datetime = datetime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMobilename() {
        return mobilename;
    }

    public void setMobilename(String mobilename) {
        this.mobilename = mobilename;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}
