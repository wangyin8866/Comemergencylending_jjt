package com.two.emergencylending.bean;

import java.io.Serializable;

public class CenterMessage implements Serializable {
    private String id = "";
    private String userId = "";
    private String newsTitle = "";
    private String newsDetail = "";
    private String newsType = "";
    private String status = "";//2代表已读
    private String createDate = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDetail() {
        return newsDetail;
    }

    public void setNewsDetail(String newsDetail) {
        this.newsDetail = newsDetail;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "CenterMessage{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", newsTitle='" + newsTitle + '\'' +
                ", newsDetail='" + newsDetail + '\'' +
                ", newsType='" + newsType + '\'' +
                ", status='" + status + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
