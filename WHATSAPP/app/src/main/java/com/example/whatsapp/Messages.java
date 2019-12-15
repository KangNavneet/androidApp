package com.example.whatsapp;

import org.w3c.dom.Text;

public class Messages {

    String msgTo,msgFrom,colForFilter,msgText,attchPath,offlinePath,offlinePathOth,msgDateTime;
    Long msgId;
    String msgType;
    Boolean msgDel;



    public Messages() {

    }

    public Messages(Long msgId, String msgTo, String msgFrom, String colForFilter, String msgText, String attchPath, String offlinePath, String offlinePathOth, String msgDateTime, String msgType, Boolean msgDel) {
        this.msgId = msgId;
        this.msgTo = msgTo;
        this.msgFrom = msgFrom;
        this.colForFilter = colForFilter;
        this.msgText = msgText;
        this.attchPath = attchPath;
        this.offlinePath = offlinePath;
        this.offlinePathOth = offlinePathOth;
        this.msgDateTime = msgDateTime;
        this.msgType = msgType;
        this.msgDel = msgDel;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getMsgTo() {
        return msgTo;
    }

    public void setMsgTo(String msgTo) {
        this.msgTo = msgTo;
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    public String getColForFilter() {
        return colForFilter;
    }

    public void setColForFilter(String colForFilter) {
        this.colForFilter = colForFilter;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getAttchPath() {
        return attchPath;
    }

    public void setAttchPath(String attchPath) {
        this.attchPath = attchPath;
    }

    public String getOfflinePath() {
        return offlinePath;
    }

    public void setOfflinePath(String offlinePath) {
        this.offlinePath = offlinePath;
    }

    public String getOfflinePathOth() {
        return offlinePathOth;
    }

    public void setOfflinePathOth(String offlinePathOth) {
        this.offlinePathOth = offlinePathOth;
    }

    public String getMsgDateTime() {
        return msgDateTime;
    }

    public void setMsgDateTime(String msgDateTime) {
        this.msgDateTime = msgDateTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Boolean getMsgDel() {
        return msgDel;
    }

    public void setMsgDel(Boolean msgDel) {
        this.msgDel = msgDel;
    }
}
