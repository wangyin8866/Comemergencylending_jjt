package com.two.emergencylending.bean;

/**
 * 城市列表model  拼音
 */
public class CityModel {

    private String name;   //显示的数据
    private String firstName;  //显示数据拼音的首字母
    private String pingyin;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPingyin() {
        return pingyin;
    }

    public void setPingyin(String pingyin) {
        this.pingyin = pingyin;
    }

}
