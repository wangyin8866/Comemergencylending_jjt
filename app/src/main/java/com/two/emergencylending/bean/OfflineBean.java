package com.two.emergencylending.bean;

import java.util.Arrays;

/**
 * 项目名称：jijietong1.15
 * 类描述：
 * 创建人：szx
 * 创建时间：2017/5/26 21:47
 * 修改人：szx
 * 修改时间：2017/5/26 21:47
 * 修改备注：
 */
public class OfflineBean {
    private String renew_loans;
    private String sall_emp_no;
    private String org_no;
    private String org_name;
    private String online_type;

    private OfflineBaseBean base_data = new OfflineBaseBean();
    private OfflineWorkBean work_data = new OfflineWorkBean();
    private OfflineContactBean[] contacts_data;
    private OfflineApplyBean apply_data = new OfflineApplyBean();

    public String getRenew_loans() {
        return renew_loans;
    }

    public void setRenew_loans(String renew_loans) {
        this.renew_loans = renew_loans;
    }

    public OfflineBaseBean getBase_data() {
        return base_data;
    }

    public void setBase_data(OfflineBaseBean base_data) {
        this.base_data = base_data;
    }

    public OfflineWorkBean getWork_data() {
        return work_data;
    }

    public void setWork_data(OfflineWorkBean work_data) {
        this.work_data = work_data;
    }

    public OfflineContactBean[] getContacts_data() {
        return contacts_data;
    }

    public void setContacts_data(OfflineContactBean[] contacts_data) {
        this.contacts_data = contacts_data;
    }

    public OfflineApplyBean getApply_data() {
        return apply_data;
    }

    public void setApply_data(OfflineApplyBean apply_data) {
        this.apply_data = apply_data;
    }

    public String getSall_emp_no() {
        return sall_emp_no;
    }

    public void setSall_emp_no(String sall_emp_no) {
        this.sall_emp_no = sall_emp_no;
    }

    public String getOrg_no() {
        return org_no;
    }

    public void setOrg_no(String org_no) {
        this.org_no = org_no;
    }

    public String getOnline_type() {
        return online_type;
    }

    public void setOnline_type(String online_type) {
        this.online_type = online_type;
    }


    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    @Override
    public String toString() {
        return "OfflineBean{" +
                "renew_loans='" + renew_loans + '\'' +
                ", sall_emp_no='" + sall_emp_no + '\'' +
                ", org_no='" + org_no + '\'' +
                ", org_name='" + org_name + '\'' +
                ", online_type='" + online_type + '\'' +
                ", base_data=" + base_data +
                ", work_data=" + work_data +
                ", contacts_data=" + Arrays.toString(contacts_data) +
                ", apply_data=" + apply_data +
                '}';
    }
}
