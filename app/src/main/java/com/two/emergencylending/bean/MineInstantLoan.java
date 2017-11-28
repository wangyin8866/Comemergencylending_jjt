package com.two.emergencylending.bean;

import java.io.Serializable;

public class MineInstantLoan implements Serializable {

    private String id;
    private String borrow_limit;
    private String management_cost;
    private String create_date;
    private String borrow_periods;
    private String borrow_status;
    private String status_value;
    private String contract_code;
    private String loan_amt;
    private String periods;
    private String product_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBorrow_limit() {
        return borrow_limit;
    }

    public void setBorrow_limit(String borrow_limit) {
        this.borrow_limit = borrow_limit;
    }

    public String getManagement_cost() {
        return management_cost;
    }

    public void setManagement_cost(String management_cost) {
        this.management_cost = management_cost;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getBorrow_periods() {
        return borrow_periods;
    }

    public void setBorrow_periods(String borrow_periods) {
        this.borrow_periods = borrow_periods;
    }

    public String getBorrow_status() {
        return borrow_status;
    }

    public void setBorrow_status(String borrow_status) {
        this.borrow_status = borrow_status;
    }

    public String getStatus_value() {
        return status_value;
    }

    public void setStatus_value(String status_value) {
        this.status_value = status_value;
    }

    public String getContract_code() {
        return contract_code;
    }

    public void setContract_code(String contract_code) {
        this.contract_code = contract_code;
    }

    public String getLoan_amt() {
        return loan_amt;
    }

    public void setLoan_amt(String loan_amt) {
        this.loan_amt = loan_amt;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public String toString() {
        return "MineInstantLoan{" +
                "id='" + id + '\'' +
                ", borrow_limit='" + borrow_limit + '\'' +
                ", management_cost='" + management_cost + '\'' +
                ", create_date='" + create_date + '\'' +
                ", borrow_periods='" + borrow_periods + '\'' +
                ", borrow_status='" + borrow_status + '\'' +
                ", status_value='" + status_value + '\'' +
                ", contract_code='" + contract_code + '\'' +
                ", loan_amt='" + loan_amt + '\'' +
                ", periods='" + periods + '\'' +
                ", product_id='" + product_id + '\'' +
                '}';
    }
}
