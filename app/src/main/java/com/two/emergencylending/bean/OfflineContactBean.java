package com.two.emergencylending.bean;

/**
 * 项目名称：jijietong1.15
 * 类描述：
 * 创建人：szx
 * 创建时间：2017/5/26 21:47
 * 修改人：szx
 * 修改时间：2017/5/26 21:47
 * 修改备注：
 */
public class OfflineContactBean {

    private String contact_name;
    private String contact_phone;
    private String relation;

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "OfflineContactBean{" +
                "contact_name='" + contact_name + '\'' +
                ", contact_phone='" + contact_phone + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}
