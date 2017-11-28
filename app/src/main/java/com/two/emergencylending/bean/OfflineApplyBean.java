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
public class OfflineApplyBean {
    private String cont_code;
    private String create_date;
    private String borrow_status;
    private String periods;
    private String loan_amt;
    private String store_name;
    private String store_no;
    private String sales_name;
    private String sall_emp_no;

    public String getCont_code() {
        return cont_code;
    }

    public void setCont_code(String cont_code) {
        this.cont_code = cont_code;
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
        this.borrow_status = borrow_status;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getLoan_amt() {
        return loan_amt;
    }

    public void setLoan_amt(String loan_amt) {
        this.loan_amt = loan_amt;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_no() {
        return store_no;
    }

    public void setStore_no(String store_no) {
        this.store_no = store_no;
    }

    public String getSales_name() {
        return sales_name;
    }

    public void setSales_name(String sales_name) {
        this.sales_name = sales_name;
    }

    public String getSall_emp_no() {
        return sall_emp_no;
    }

    public void setSall_emp_no(String sall_emp_no) {
        this.sall_emp_no = sall_emp_no;
    }

    @Override
    public String toString() {
        return "OfflineApplyBean{" +
                "cont_code='" + cont_code + '\'' +
                ", create_date='" + create_date + '\'' +
                ", borrow_status='" + borrow_status + '\'' +
                ", periods='" + periods + '\'' +
                ", loan_amt='" + loan_amt + '\'' +
                ", store_name='" + store_name + '\'' +
                ", store_no='" + store_no + '\'' +
                ", sales_name='" + sales_name + '\'' +
                ", sall_emp_no='" + sall_emp_no + '\'' +
                '}';
    }
}
