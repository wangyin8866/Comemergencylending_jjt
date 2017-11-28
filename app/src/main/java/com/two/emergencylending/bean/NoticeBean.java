package com.two.emergencylending.bean;

/**
 * 项目名称：急借通
 * 类描述：公告bean
 * 创建人：szx
 * 创建时间：2016/8/30 16:28
 * 修改人：szx
 * 修改时间：2016/8/30 16:28
 * 修改备注：
 */
public class NoticeBean {
    private String title;
    private String content;
    private String date;
    private String url;
//    private String create_date;
//    private String update_date;
//    private String del_flag;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NoticeBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
