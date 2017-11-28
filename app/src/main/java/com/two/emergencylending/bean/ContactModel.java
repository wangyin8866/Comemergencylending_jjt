package com.two.emergencylending.bean;


public class ContactModel {
    private String contact_name;
    private String relation;
    private String contact_phone;
    private int fill_location;

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public int getFill_location() {
        return fill_location;
    }

    public void setFill_location(int fill_location) {
        this.fill_location = fill_location;
    }

    @Override
    public String toString() {
        return "ContactModel{" +
                "contact_name='" + contact_name + '\'' +
                ", relation='" + relation + '\'' +
                ", contact_phone='" + contact_phone + '\'' +
                ", fill_location=" + fill_location +
                '}';
    }
}
