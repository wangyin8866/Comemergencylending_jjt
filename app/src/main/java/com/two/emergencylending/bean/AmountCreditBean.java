package com.two.emergencylending.bean;

/**
 * 项目名称：急借通
 * 类描述：城市bean
 * 创建人：szx
 * 创建时间：2016/8/4 17:38
 * 修改人：szx
 * 修改时间：2016/8/4 17:38
 * 修改备注：
 */
public class AmountCreditBean {
    private int mId;
    private int pic;
    private String amountContent;
    private boolean isSeclect;

    public AmountCreditBean(int mId) {
        super();
        this.mId = mId;
    }

    public AmountCreditBean(int mId, int pic, String amountContent, boolean isSeclect) {
        super();
        this.mId = mId;
        this.pic = pic;
        this.amountContent = amountContent;
        this.isSeclect = isSeclect;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getAmountContent() {
        return amountContent;
    }

    public void setAmountContent(String amountContent) {
        this.amountContent = amountContent;
    }

    public boolean isSeclect() {
        return isSeclect;
    }

    public void setSeclect(boolean seclect) {
        isSeclect = seclect;
    }

    @Override
    public String toString() {
        return "AmountCreditBean{" +
                "mId=" + mId +
                ", pic=" + pic +
                ", amountContent='" + amountContent + '\'' +
                ", isSeclect=" + isSeclect +
                '}';
    }
}
