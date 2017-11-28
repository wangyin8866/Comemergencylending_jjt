package com.two.emergencylending.bean;

import java.util.List;

/**
 * 项目名称：急借通
 * 类描述：客户联系bean
 * 创建人：szx
 * 创建时间：2016/8/30 16:28
 * 修改人：szx
 * 修改时间：2016/8/30 16:28
 * 修改备注：
 */
public class ClerkInfoBean {
	private String cur_month_com_count;
	private String cur_month_com_sum;
	private String pre_month_com_count;
	private String pre_month_com_sum;

	private String dir_no_store_count;
	private String dir_no_bill_count;
	private String indir_no_store_count;
	private String indir_no_bill_count;
    private List<AnnouncementBean> list;
	public String getCur_month_com_count() {
		return cur_month_com_count;
	}

	public void setCur_month_com_count(String cur_month_com_count) {
		this.cur_month_com_count = cur_month_com_count;
	}

	public String getCur_month_com_sum() {
		return cur_month_com_sum;
	}

	public void setCur_month_com_sum(String cur_month_com_sum) {
		this.cur_month_com_sum = cur_month_com_sum;
	}

	public String getPre_month_com_count() {
		return pre_month_com_count;
	}

	public void setPre_month_com_count(String pre_month_com_count) {
		this.pre_month_com_count = pre_month_com_count;
	}

	public String getPre_month_com_sum() {
		return pre_month_com_sum;
	}

	public void setPre_month_com_sum(String pre_month_com_sum) {
		this.pre_month_com_sum = pre_month_com_sum;
	}

	public String getDir_no_store_count() {
		return dir_no_store_count;
	}

	public void setDir_no_store_count(String dir_no_store_count) {
		this.dir_no_store_count = dir_no_store_count;
	}

	public String getDir_no_bill_count() {
		return dir_no_bill_count;
	}

	public void setDir_no_bill_count(String dir_no_bill_count) {
		this.dir_no_bill_count = dir_no_bill_count;
	}

	public String getIndir_no_store_count() {
		return indir_no_store_count;
	}

	public void setIndir_no_store_count(String indir_no_store_count) {
		this.indir_no_store_count = indir_no_store_count;
	}

	public String getIndir_no_bill_count() {
		return indir_no_bill_count;
	}

	public void setIndir_no_bill_count(String indir_no_bill_count) {
		this.indir_no_bill_count = indir_no_bill_count;
	}

    public List<AnnouncementBean> getList() {
        return list;
    }

    public void setList(List<AnnouncementBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ClerkInfoBean{" +
                "cur_month_com_count='" + cur_month_com_count + '\'' +
                ", cur_month_com_sum='" + cur_month_com_sum + '\'' +
                ", pre_month_com_count='" + pre_month_com_count + '\'' +
                ", pre_month_com_sum='" + pre_month_com_sum + '\'' +
                ", dir_no_store_count='" + dir_no_store_count + '\'' +
                ", dir_no_bill_count='" + dir_no_bill_count + '\'' +
                ", indir_no_store_count='" + indir_no_store_count + '\'' +
                ", indir_no_bill_count='" + indir_no_bill_count + '\'' +
                ", list=" + list +
                '}';
    }
}
