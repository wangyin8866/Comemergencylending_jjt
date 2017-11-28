package com.two.emergencylending.bean;

import java.io.Serializable;

public class BankBean implements Serializable {
    private static final long serialVersionUID = -2364450950686205050L;
    private String theBank;
    private String bankName;
    private boolean isChecked = false;

    public String getTheBank() {
        return theBank;
    }

    public void setTheBank(String theBank) {
        this.theBank = theBank;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "BankBean{" +
                "theBank='" + theBank + '\'' +
                ", bankName='" + bankName + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
