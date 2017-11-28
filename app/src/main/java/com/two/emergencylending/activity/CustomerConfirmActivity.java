package com.two.emergencylending.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.example.getlimtlibrary.builder.utils.ArithmeticUtils;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.CustomerConfirmController;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：客户确认页面
 * 创建人：szx
 * 创建时间：2017/1/16 14:26
 * 修改人：szx
 * 修改时间：2017/1/16 14:26
 * 修改备注：
 */
public class CustomerConfirmActivity extends BaseActivity implements Topbar.topbarClickListener, ControllerCallBack {
    @Bind(R.id.confirm_topbar)
    Topbar topbar;
    @Bind(R.id.ll_refuse)
    LinearLayout llRefuse;
    @Bind(R.id.sv_confirm)
    ScrollView confirm;
    @Bind(R.id.amount)
    TextView amount;
    @Bind(R.id.period)
    TextView period;
    @Bind(R.id.actual_to_account)
    TextView actualToAccount;
    @Bind(R.id.interest)
    TextView interest;
    @Bind(R.id.management_cost)
    TextView managementCost;
    @Bind(R.id.service_cost)
    TextView serviceCost;
    CustomerConfirmController customerConfirmController;
    CustomerDialog dialog;

    @Override
    public int setContent() {
        return R.layout.activity_customer_confirm;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        customerConfirmController = new CustomerConfirmController(this, this);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        confirm.setVisibility(View.VISIBLE);
        llRefuse.setVisibility(View.GONE);
    }

    @Override
    public void setData() {
        initData("");
    }

    public void initData(String value) {
        String loan_amt = "";
        String periods = "";
        String lending_amount = "";
        String management_cost = "";
        String credit_manage_fee = "";
        String service_fee = "";
        if (!StringUtil.isNullOrEmpty(value)) {
            try {
                JSONObject json = new JSONObject(value);
                loan_amt = json.get("loan_amt").toString();
                periods = json.get("periods").toString();
                lending_amount = json.get("lending_amount").toString();
                management_cost = json.get("period_amount").toString();
                credit_manage_fee = json.get("credit_manage_fee").toString();
                service_fee = json.get("information_service_fee").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        amount.setText(loan_amt);
        period.setText(periods + "周");
        actualToAccount.setText(lending_amount + "元");
        interest.setText(management_cost + "元/周");
        managementCost.setText("账号管理费：" + countManagFr(loan_amt, lending_amount, management_cost)
                + "元");
        serviceCost.setText("履约保证金：" + management_cost + "元");

    }

    /*
    计算账号管理费
     */
    public String countManagFr(String loan_amt, String lending_amount, String period_amount) {
        if (StringUtil.isNullOrEmpty(loan_amt)) {
            return "0";
        }
        String mLending_amount = "0";
        String mPeriod_amount = "0";
        if (!StringUtil.isNullOrEmpty(lending_amount)) {
            mLending_amount = lending_amount;
        }
        if (!StringUtil.isNullOrEmpty(period_amount)) {
            mPeriod_amount = period_amount;
        }
        String sum = ArithmeticUtils.sub(loan_amt, mLending_amount, 2);
        String result = ArithmeticUtils.sub(sum, mPeriod_amount, 2);
        if (Double.valueOf(result) <= 0) {//防止出现负数
            result = "0";
        }

        return result;
    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
    }


    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
        customerConfirmController.getConfirmInfo();
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
    }

    @OnClick({R.id.get, R.id.refuse})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.get:
                customerConfirmController.claim();
                break;
            case R.id.refuse:
                isRefuseDialog();
                break;
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
                    initData(msg.obj.toString());
                    break;
                case 3:
                    CommonUtils.goToActivity(CustomerConfirmActivity.this, ElectronicAgreementActivity.class);
                    finish();
                    break;
                case 4:
                    confirm.setVisibility(View.GONE);
                    llRefuse.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onSuccess(int returnCode, String value) {
        Message msg = new Message();
        if (returnCode == CallBackType.CUSTOMER_CONFIRM_QUERY) {
            msg.what = 2;
            msg.obj = value;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.CUSTOMER_CONFIRM_CLAIM) {
            msg.what = 3;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.CUSTOMER_CONFIRM_REFUSE) {
            msg.what = 4;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        Message msg = new Message();
        if (returnCode == CallBackType.CUSTOMER_CONFIRM_CLAIM) {
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.CUSTOMER_CONFIRM_CLAIM) {
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.CUSTOMER_CONFIRM_REFUSE) {
            msg.what = 4;
            handler.sendMessage(msg);
        }
    }

    private void isRefuseDialog() {
        dialog = new CustomerDialog(getContext());
        dialog.showChoiceDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.ll_cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.ll_sure) {
                    customerConfirmController.refuse();
                    dialog.dismiss();
                }
            }
        }, new String[]{"确定拒绝？", "", "取消", "确定"});
        dialog.show();
    }
}
