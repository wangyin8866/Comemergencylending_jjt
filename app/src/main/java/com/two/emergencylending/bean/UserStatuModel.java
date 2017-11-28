package com.two.emergencylending.bean;

/**
 * Created by User on 2016/8/29.
 */
public class UserStatuModel {
    private String head_pic;//头像
    private int user_data_status;//个人资料
    private int user_job_status;//工作信息
    private int userAuthStatus;//认证信息
    private int  user_contact_status;//联系人
    private String notExtractAmount;//未提取金额
    private String borrowLimit;//借款金额
    private int unreadNewsCount;//未读消息数量
    private String username;//用户名
    private String phone;//密码
    private String idcard;

    public String getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(String borrowStatus) {
        this.borrowStatus = borrowStatus;
    }

    private String borrowStatus;
    private String userAuthStatusJindong;//京东认证状态
    private String  userAuthStatusShouji;//手机运营商认证状态
    public String getHeadPic() {
        return head_pic;
    }

    public void setHeadPic(String headPic) {
        UserInfoManager.getInstance().setAdvater(headPic);
        this.head_pic = headPic;
    }

    public int getUserDataStatus() {
        return user_data_status;
    }

    public void setUserDataStatus(int userDataStatus) {
        this.user_data_status = userDataStatus;
    }

    public int getUserJobStatus() {
        return user_job_status;
    }

    public void setUserJobStatus(int userJobStatus) {
        this.user_job_status = userJobStatus;
    }

    public int getUserAuthStatus() {
        return userAuthStatus;
    }

    public void setUserAuthStatus(int userAuthStatus) {
        this.userAuthStatus = userAuthStatus;
    }

    public int getUserContactStatus() {
        return  user_contact_status;
    }

    public void setUserContactStatus(int userContactStatus) {
        this. user_contact_status = userContactStatus;
    }

    public String getNotExtractAmount() {
        return notExtractAmount;
    }

    public void setNotExtractAmount(String notExtractAmount) {
        this.notExtractAmount = notExtractAmount;
    }

    public String getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(String borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public int getUnreadNewsCount() {
        return unreadNewsCount;
    }

    public void setUnreadNewsCount(int unreadNewsCount) {
        this.unreadNewsCount = unreadNewsCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return phone;
    }

    public void setMobile(String mobile) {
        this.phone = mobile;
    }

    public String getUserAuthStatusJindong() {
        return userAuthStatusJindong;
    }

    public void setUserAuthStatusJindong(String userAuthStatusJindong) {
        this.userAuthStatusJindong = userAuthStatusJindong;
    }

    public String getUserAuthStatusShouji() {
        return userAuthStatusShouji;
    }

    public void setUserAuthStatusShouji(String userAuthStatusShouji) {
        this.userAuthStatusShouji = userAuthStatusShouji;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
}
