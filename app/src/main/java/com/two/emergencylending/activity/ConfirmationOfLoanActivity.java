package com.two.emergencylending.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.BorrowInfoBean;
import com.two.emergencylending.bean.PresentStore;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.CommonConstant;
import com.two.emergencylending.constant.IntentKey;
import com.two.emergencylending.controller.BorrowStatusCotroller;
import com.two.emergencylending.controller.ConfirmationOfLoanCotroller;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.PushCustCotroller;
import com.two.emergencylending.manager.BorrowStatusManager;
import com.two.emergencylending.manager.CustomerManagerManager;
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
 * 类描述：确认借款页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class ConfirmationOfLoanActivity extends BaseActivity implements Topbar.topbarClickListener, ControllerCallBack {
    @Bind(R.id.confirmationg_of_loan_topbar)
    Topbar topbar;
    @Bind(R.id.present_store)
    RelativeLayout present_store;
    @Bind(R.id.present_store_online)
    RelativeLayout present_store_online;
    @Bind(R.id.confirmationg_of_loan_sure)
    Button confirmationg_of_loan_sure;
    @Bind(R.id.rl_improve)
    RelativeLayout improve;
    @Bind(R.id.amount)
    TextView amount;
    @Bind(R.id.periods)
    TextView periods;
    @Bind(R.id.usage)
    TextView usage;
    @Bind(R.id.refund_way)
    TextView refundWay;
    @Bind(R.id.tv_store)
    TextView store;
    @Bind(R.id.tv_remind)
    TextView remind;

    BorrowInfoBean borrowInfoBean;
    //返回码
    private final int RESULT_CODE = 100;
    //请求码
    private final int REQUEST_CODE = 101;
    PresentStore presentStore;
    BorrowStatusCotroller borrowStatusCotroller;
    ConfirmationOfLoanCotroller confirmationOfLoanCotroller;
    PushCustCotroller pushCustCotroller;
    private String storeName = "";
    private String storeCode = "";


    private String productId;
    private String custId;

    @Override
    public int setContent() {
        return R.layout.activity_confirmation_of_loan;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        if (CustomerManagerManager.isCustomerManager()) {
            custId = getIntent().getStringExtra("custId");
        }
        productId = getIntent().getStringExtra("data");
        borrowInfoBean = new BorrowInfoBean();
        borrowStatusCotroller = new BorrowStatusCotroller(this, this);
        confirmationOfLoanCotroller = new ConfirmationOfLoanCotroller(this, this);
        pushCustCotroller = new PushCustCotroller(this, this);
        presentStore = new PresentStore();
    }

    public void initConfirmData() {
        borrowInfoBean.setProduct_id(productId);
        if (StringUtil.isNullOrEmpty(borrowInfoBean.getBorrow_limit())) {
            amount.setText(0 + " 元");
        } else {
            amount.setText(borrowInfoBean.getBorrow_limit() + " 元");
        }
        if (StringUtil.isNullOrEmpty(borrowInfoBean.getBorrow_periods())) {
            periods.setText(0 + " 周");
        } else {
            periods.setText(borrowInfoBean.getBorrow_periods() + " 周");
        }
        if (!productId.equals(CommonConstant.PRODUCT_OFFLINE)) {
            showOnline();
        } else {
            showOffline();
        }
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
        if (CustomerManagerManager.isCustomerManager()) {
            confirmationOfLoanCotroller.getConfirmationInfo(custId);
        } else {
            confirmationOfLoanCotroller.getConfirmationInfo();
        }

    }

    @Override
    public void destroy() {

    }

    private void back() {
        setResult(103);
        finish();
//        borrowStatusCotroller.getBorrowStatus();
    }

    @Override
    public void leftClick() {
        back();
    }

    @Override
    public void rightClick() {

    }

    @OnClick({R.id.present_store, R.id.confirmationg_of_loan_sure, R.id.rl_improve})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.confirmationg_of_loan_sure:
                commitBorrow();
                break;
            case R.id.rl_improve:
                Intent intent1 = new Intent(ConfirmationOfLoanActivity.this, AmountCreditActivity.class);
                intent1.putExtra(IntentKey.DATA, presentStore);
                startActivityForResult(intent1, REQUEST_CODE);
                break;
        }
    }

    public void commitBorrow() {
        if (StringUtil.isNullOrEmpty(borrowInfoBean.getProduct_id())) {
            borrowInfoBean.setProduct_id(productId);
        }
        if (CommonUtils.checkNull(amount.getText().toString(), "金额不能为空")) return;
        if (CommonUtils.checkNull(periods.getText().toString(), "周期不能为空")) return;
        if (CommonUtils.checkNull(borrowInfoBean.getBorrow_limit(), "金额不能为空")) return;
        if (CommonUtils.checkNull(borrowInfoBean.getBorrow_periods(), "周期不能为空")) return;
        if (!productId.equals(CommonConstant.PRODUCT_OFFLINE)) {
            borrowInfoBean.setStore("0");
        } else {
            if (StringUtil.isNullOrEmpty(borrowInfoBean.getStore())) {
                borrowInfoBean.setStore(storeCode);
            }
            ;
        }
        if (CustomerManagerManager.isCustomerManager()) {
            confirmationOfLoanCotroller.customerConfirmation(borrowInfoBean, custId);
        } else {
            confirmationOfLoanCotroller.confirmation(borrowInfoBean);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            presentStore = (PresentStore) data.getSerializableExtra(IntentKey.DATA);
            borrowInfoBean.setStore(presentStore.getId());
            store.setText(presentStore.getStoreName());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void showDialog() {
        CustomerDialog dialog = new CustomerDialog(ConfirmationOfLoanActivity.this);
        dialog.showCommitSuccess(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(IntentKey.FROM, 1);
                CommonUtils.goToActivity(getContext(), MineInstantLoanActivity.class, intent);
                finish();
            }
        });
        dialog.show();
    }


    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.COMMIT_BORROW_INFO) {
            setResult(100);
            finish();
        } else if (returnCode == CallBackType.BORROW_STATUS) {
            secletBorrowStatus(value);
        } else if (returnCode == CallBackType.GET_CONFIRM_INFO) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(value);
                if (borrowInfoBean == null) {
                    borrowInfoBean = new BorrowInfoBean();
                }
                borrowInfoBean.setBorrow_limit(jsonObject.optString("borrow_limit"));
                borrowInfoBean.setBorrow_periods(jsonObject.optString("borrow_periods"));
                borrowInfoBean.setBorrow_use_code("525");
                borrowInfoBean.setStore(jsonObject.optString("store"));
                storeName = jsonObject.optString("store_name");
                store.setText(storeName);
                initConfirmData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = errorMessage;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    public void showOnline() {
        improve.setVisibility(View.GONE);
        present_store.setVisibility(View.GONE);
        present_store_online.setVisibility(View.VISIBLE);
        remind.setVisibility(View.GONE);
    }

    public void showOffline() {
        improve.setVisibility(View.VISIBLE);
        present_store.setVisibility(View.VISIBLE);
        present_store_online.setVisibility(View.GONE);
        remind.setVisibility(View.VISIBLE);
    }

    private void secletBorrowStatus(String value) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(value);
            String borrowStatus = jsonObject.getString("BorrowStatus");
            int status = BorrowStatusManager.getBorrowStatus(borrowStatus);
            if (status == BorrowStatusManager.PAGE_STATUS_AUTH) {
                finish();
            } else {
                setResult(103);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 手机上的物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            back();
            return true;
        }
        return false;
    }
}
