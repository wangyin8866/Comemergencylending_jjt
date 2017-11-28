package com.two.emergencylending.bean;

/**
 * 项目名称：急借通
 * 类描述：客户联系bean
 * 创建人：szx
 * 创建时间：2016/8/30 16:28
 * 修改人：szx
 * 修改时间：2016/8/30 16:28
 * 修改备注：
 */
public class CustomerBean {
    private String username;
    private String bi_create;
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBi_create() {
        return bi_create;
    }

    public void setBi_create(String bi_create) {
        this.bi_create = bi_create;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "CustomerBean{" +
                "username='" + username + '\'' +
                ", bi_create='" + bi_create + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
