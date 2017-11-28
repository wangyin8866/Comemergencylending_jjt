package com.two.emergencylending.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/10/30.
 */

public class MessageInfo {
  private String name;
    private Bitmap contactPhoto;
    private String smsContent;
    private String smsDate;
    private String type;
    private String nameId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(Bitmap contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public void setSmsDate(String smsDate) {
        this.smsDate = smsDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "name='" + name + '\'' +
                ", contactPhoto=" + contactPhoto +
                ", smsContent='" + smsContent + '\'' +
                ", smsDate='" + smsDate + '\'' +
                ", type='" + type + '\'' +
                ", nameId='" + nameId + '\'' +
                '}';
    }
}
