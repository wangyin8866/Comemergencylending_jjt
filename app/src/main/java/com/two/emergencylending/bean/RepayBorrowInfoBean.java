package com.two.emergencylending.bean;

/**
 * Created by User on 2017/4/11.
 */

public class RepayBorrowInfoBean {

    /**
     * id : 86234c9bc0524a38a9ef9d55fb458e25
     * user_id : 2cfd6f6398f545408fff291acaf798a3
     * product_id : 4
     * borrow_limit : 5000
     * borrow_periods : 15
     * borrow_use : 521
     * borrow_status : 6
     * contract_code : SQJ201704061650200633
     * create_date : 2017-04-06 16:44:58
     * update_date : 2017-04-06 16:45:03
     * del_flag : 0
     */

    private String id;
    private String user_id;
    private String product_id;
    private int borrow_limit;
    private int borrow_periods;
    private String borrow_use;
    private int borrow_status;
    private String contract_code;
    private String create_date;
    private String update_date;
    private String del_flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getBorrow_limit() {
        return borrow_limit;
    }

    public void setBorrow_limit(int borrow_limit) {
        this.borrow_limit = borrow_limit;
    }

    public int getBorrow_periods() {
        return borrow_periods;
    }

    public void setBorrow_periods(int borrow_periods) {
        this.borrow_periods = borrow_periods;
    }

    public String getBorrow_use() {
        return borrow_use;
    }

    public void setBorrow_use(String borrow_use) {
        this.borrow_use = borrow_use;
    }

    public int getBorrow_status() {
        return borrow_status;
    }

    public void setBorrow_status(int borrow_status) {
        this.borrow_status = borrow_status;
    }

    public String getContract_code() {
        return contract_code;
    }

    public void setContract_code(String contract_code) {
        this.contract_code = contract_code;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
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
}
