package com.two.emergencylending.base;

import java.io.Serializable;

/**
 * 项目名称：急借通
 * 类描述：提现历史Bean
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class DepositHistroy implements Serializable {

    private String create_date;
    private String commission;

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
        return "DepositHistroy{" +
                "create_date='" + create_date + '\'' +
                ", commission='" + commission + '\'' +
                '}';
    }
}
