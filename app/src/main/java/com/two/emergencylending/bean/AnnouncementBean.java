package com.two.emergencylending.bean;

import java.io.Serializable;

/**
 * 项目名称：急借通
 * 类描述：公告bean
 * 创建人：szx
 * 创建时间：2016/8/30 16:28
 * 修改人：szx
 * 修改时间：2016/8/30 16:28
 * 修改备注：
 */
public class AnnouncementBean implements Serializable {
    private String act_name;
    private String act_content;
    private String act_date;
    private String act_url;


    public String getAct_name() {
        return act_name;
    }

    public void setAct_name(String act_name) {
        this.act_name = act_name;
    }

    public String getAct_content() {
        return act_content;
    }

    public void setAct_content(String act_content) {
        this.act_content = act_content;
    }

    public String getAct_date() {
        return act_date;
    }

    public void setAct_date(String act_date) {
        this.act_date = act_date;
    }

    public String getAct_url() {
        return act_url;
    }

    public void setAct_url(String act_url) {
        this.act_url = act_url;
    }

    @Override
    public String toString() {
        return "AnnouncementBean{" +
                "act_name='" + act_name + '\'' +
                ", act_content='" + act_content + '\'' +
                ", act_date='" + act_date + '\'' +
                ", act_url='" + act_url + '\'' +
                '}';
    }
}
