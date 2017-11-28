package com.two.emergencylending.bean;

import java.io.Serializable;

/**
 * 项目名称：急借通
 * 类描述：提现列表bean
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class PresentStore implements Serializable {
    private String city;
    private String id;
    private String store_addr;
    private String phone;
    private String store_name;
    private String city_name;
    private String contact;
    private String create_date;
    private String city_code;
    private String update_date;
    private String del_flag;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreAddr() {
        return store_addr;
    }

    public void setStoreAddr(String storeAddr) {
        this.store_addr = storeAddr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStoreName() {
        return store_name;
    }

    public void setStoreName(String storeName) {
        this.store_name = storeName;
    }

    public String getCityName() {
        return city_name;
    }

    public void setCityName(String cityName) {
        this.city_name = cityName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStore_addr() {
        return store_addr;
    }

    public void setStore_addr(String store_addr) {
        this.store_addr = store_addr;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
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

    @Override
    public String toString() {
        return "PresentStore{" +
                "city='" + city + '\'' +
                ", id='" + id + '\'' +
                ", store_addr='" + store_addr + '\'' +
                ", phone='" + phone + '\'' +
                ", store_name='" + store_name + '\'' +
                ", city_name='" + city_name + '\'' +
                ", contact='" + contact + '\'' +
                ", create_date='" + create_date + '\'' +
                ", city_code='" + city_code + '\'' +
                ", update_date='" + update_date + '\'' +
                ", del_flag='" + del_flag + '\'' +
                '}';
    }
}
