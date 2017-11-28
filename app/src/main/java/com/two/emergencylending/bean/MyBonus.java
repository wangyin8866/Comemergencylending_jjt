package com.two.emergencylending.bean;

public class MyBonus {
    private String phone;
    private String create_date;
    private String commission;

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

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "MyBonus{" +
                "phone='" + phone + '\'' +
                ", create_date='" + create_date + '\'' +
                ", commission='" + commission + '\'' +
                '}';
    }

}
