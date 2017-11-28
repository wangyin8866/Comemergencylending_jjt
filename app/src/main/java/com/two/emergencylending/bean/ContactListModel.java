package com.two.emergencylending.bean;

/**
 * Created by User on 2016/8/29.
 */
public class ContactListModel  {
    private String id;
    private  String contact_name;
    private  String relation;
    private  String contact_phone;
    private  int fill_location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactName() {
        return contact_name;
    }

    public void setContactName(String contactName) {
        this.contact_name = contactName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getContactPhone() {
        return contact_phone;
    }

    public void setContactPhone(String contactPhone) {
        this.contact_phone = contactPhone;
    }

    public int getFill_location() {
        return fill_location;
    }

    public void setFill_location(int fill_location) {
        this.fill_location = fill_location;
    }

    @Override
    public String toString() {
        return "ContactListModel{" +
                "id='" + id + '\'' +
                ", contact_name='" + contact_name + '\'' +
                ", relation='" + relation + '\'' +
                ", contact_phone='" + contact_phone + '\'' +
                ", fill_location=" + fill_location +
                '}';
    }
}
