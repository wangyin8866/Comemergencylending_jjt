package com.two.emergencylending.bean;

/**
 * Created by User on 2016/11/2.
 */

public class VersioBean {
    private String id;
    private String app_version_no;
    private String app_url;
    private String force_update_version;
    private String create_date;
    private String update_date;
    private String del_flag;
    private int is_force_update;
    private int platform_type;
    private int app_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApp_version_no() {
        return app_version_no;
    }

    public void setApp_version_no(String app_version_no) {
        this.app_version_no = app_version_no;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public String getForce_update_version() {
        return force_update_version;
    }

    public void setForce_update_version(String force_update_version) {
        this.force_update_version = force_update_version;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }

    public int getIs_force_update() {
        return is_force_update;
    }

    public void setIs_force_update(int is_force_update) {
        this.is_force_update = is_force_update;
    }

    public int getPlatform_type() {
        return platform_type;
    }

    public void setPlatform_type(int platform_type) {
        this.platform_type = platform_type;
    }

    public int getApp_type() {
        return app_type;
    }

    public void setApp_type(int app_type) {
        this.app_type = app_type;
    }
}
