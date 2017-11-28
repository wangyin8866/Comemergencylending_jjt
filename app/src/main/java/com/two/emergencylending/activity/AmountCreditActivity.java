package com.two.emergencylending.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.chinazyjr.lib.util.ToastUtils;
import com.zyjr.emergencylending.R;
import com.two.emergencylending.adapter.AmountCreditAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.AmountCreditBean;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.CreditCotroller;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.view.Topbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：提额增信页面
 * 创建人：szx
 * 创建时间：2016/9/23.
 * 修改人：szx
 * 修改时间：2016/9/23.
 * 修改备注：
 */
public class AmountCreditActivity extends BaseActivity implements Topbar.topbarClickListener, ControllerCallBack {

    @Bind(R.id.amount_credit_topbar)
    Topbar amount_credit_topbar;
    @Bind(R.id.lv_amount_credit)
    ListView lv_amount_credit;
    private List<AmountCreditBean> amountCreditBeans;
    AmountCreditAdapter amountCreditAdapter;
    CreditCotroller creditCotroller;

    @Override
    public int setContent() {
        return R.layout.activity_amount_credit;
    }

    @Override
    public int setStatusColor() {
        return R.color.white;
    }

    @Override
    public void init() {
        amountCreditBeans = new ArrayList<AmountCreditBean>();
        creditCotroller = new CreditCotroller(this, this);
        creditCotroller.getCredit();
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        amount_credit_topbar.getLeftIco().setVisibility(View.VISIBLE);
        amount_credit_topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        amount_credit_topbar.getRightButton().setTextColor(IApplication.globleResource.getColor(R.color.lightgrays));
        amount_credit_topbar.getRightButton().setVisibility(View.VISIBLE);
        amount_credit_topbar.getRightButton().setText("提交");

    }

    @Override
    public void setData() {
        AmountCreditBean bean1 = new AmountCreditBean(0);
        AmountCreditBean bean2 = new AmountCreditBean(1);
        AmountCreditBean bean3 = new AmountCreditBean(2);
        AmountCreditBean bean4 = new AmountCreditBean(3);
        bean1.setPic(R.drawable.icon_identity1);
        bean2.setPic(R.drawable.icon_identity2);
        bean3.setPic(R.drawable.icon_identity3);
        bean4.setPic(R.drawable.icon_identity4);
        bean1.setAmountContent("本人在职员工,我有养老保险或\r\n积金连续缴费记录>=6个月");
        bean2.setAmountContent("本人所投寿险保单缴费用期限>=5年\r\n且已连续正常缴满3年");
        bean3.setAmountContent("本人为女性,持有一张额度5000\r\n元以上的信用卡,信用卡状态正常\r\n无逾期,且激活使用6个月以上");
        bean4.setAmountContent("本人养老保险或公积金连续缴费记录\r\n>=12个月,并且购买过传统型寿险.");
        bean1.setSeclect(false);
        bean2.setSeclect(false);
        bean3.setSeclect(false);
        bean4.setSeclect(false);
        amountCreditBeans.add(bean1);
        amountCreditBeans.add(bean2);
        amountCreditBeans.add(bean3);
        amountCreditBeans.add(bean4);
        amountCreditAdapter = new AmountCreditAdapter(getContext(), amountCreditBeans);
        lv_amount_credit.setAdapter(amountCreditAdapter);
    }

    @Override
    public void setListener() {
        amount_credit_topbar.setOnTopbarClickListener(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {
        subimit();
    }

    private void subimit() {
            String checkBox = "";
            for (AmountCreditBean bean : amountCreditBeans) {
                if (bean.isSeclect()) {
                    if (checkBox.length() > 0) {
                        checkBox += ",";
                    }
                    checkBox += bean.getmId();
                }
            }
            if (checkBox.equals("")) {
                ToastUtils.showShort(this, "请至少选择一项");
                return;
            }
            creditCotroller.saveCredit(checkBox);
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.GET_CREDIT) {
            if (!StringUtil.isNullOrEmpty(value)) {
                String[] res = value.split(",");
                for (int i = 0; i < res.length; i++) {
                    int checkedId = Integer.valueOf(res[i]);
                    for (AmountCreditBean bean : amountCreditBeans) {
                        if (bean.getmId() == checkedId) {
                            bean.setSeclect(true);
                        }
                    }
                }
                amountCreditAdapter.notifyDataSetChanged();
            }
        } else if (returnCode == CallBackType.SAVE_CREDIT) {
            ToastUtils.showShort(IApplication.globleContext, "提交成功");
            finish();
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.GET_CREDIT) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.SAVE_CREDIT) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                case 2:
                    subimit();
                    break;
                default:
                    break;
            }
        }
    };
}
