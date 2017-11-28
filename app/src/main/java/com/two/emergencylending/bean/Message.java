package com.two.emergencylending.bean;

import java.io.Serializable;

public class Message implements Serializable {

    private String phone;//手机

    private String type;//类型

    private String isRead;//已读

    private String loanState;//状态

    private String loanTime;//时间

    private String loanMessage;//内容

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    public String getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(String loanTime) {
        this.loanTime = loanTime;
    }

    public String getLoanMessage() {
        return loanMessage;
    }

    public void setLoanMessage(String loanMessage) {
        this.loanMessage = loanMessage;
    }

    @Override
    public String toString() {
        return "CenterMessage{" +
                "phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                ", isRead='" + isRead + '\'' +
                ", loanState='" + loanState + '\'' +
                ", loanTime='" + loanTime + '\'' +
                ", loanMessage='" + loanMessage + '\'' +
                '}';
    }
}
