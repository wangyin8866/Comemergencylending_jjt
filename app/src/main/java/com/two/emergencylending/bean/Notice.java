package com.two.emergencylending.bean;

import java.io.Serializable;

public class Notice implements Serializable {

    private String userId;//用户ID

    private String type;//类型

    private String hasNotice;//有消息(0-没有通知，1-有通知)

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHasNotice() {
        return hasNotice;
    }

    public void setHasNotice(String hasNotice) {
        this.hasNotice = hasNotice;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", hasNotice='" + hasNotice + '\'' +
                '}';
    }
}
