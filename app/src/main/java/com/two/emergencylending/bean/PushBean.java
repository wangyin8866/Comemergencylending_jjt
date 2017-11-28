package com.two.emergencylending.bean;

/**
 * 项目名称：jijietong
 * 类描述：
 * 创建人：szx
 * 创建时间：2016/8/23 10:59
 * 修改人：szx
 * 修改时间：2016/8/23 10:59
 * 修改备注：
 */
public class PushBean {
    private String phone;
    private String type;
    private String title;
    private String content;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PushBean{" +
                "phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
