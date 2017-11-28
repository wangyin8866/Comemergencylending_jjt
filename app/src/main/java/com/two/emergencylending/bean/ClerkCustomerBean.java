package com.two.emergencylending.bean;

import com.two.emergencylending.utils.StringUtil;

import java.io.Serializable;

/**
 * 项目名称：急借通
 * 类描述：业务员的客户bean
 * 创建人：szx
 * 创建时间：2016/8/30 16:28
 * 修改人：szx
 * 修改时间：2016/8/30 16:28
 * 修改备注：
 */
public class ClerkCustomerBean implements Serializable {

    private String parent_id;
    private String cust_id;
    private String username;
    private String phone;
    private String create_date;
    private String borrow_status = "";
    private String borrow_limit;
    private String borrow_periods;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getBorrow_status() {
        return borrow_status;
    }

    public void setBorrow_status(String borrow_status) {
        if (StringUtil.isNullOrEmpty(borrow_status)) {
            borrow_status = "";
        }
        this.borrow_status = borrow_status;
    }

    public String getBorrow_limit() {
        return borrow_limit;
    }

    public void setBorrow_limit(String borrow_limit) {
        this.borrow_limit = borrow_limit;
    }

    public String getBorrow_periods() {
        return borrow_periods;
    }

    public void setBorrow_periods(String borrow_periods) {
        this.borrow_periods = borrow_periods;
    }
}
