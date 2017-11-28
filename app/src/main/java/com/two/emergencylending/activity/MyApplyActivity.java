package com.two.emergencylending.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.two.emergencylending.adapter.ClerkCustomerDetailAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.ClerkCustomerBean;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.CustomerManagerController;
import com.two.emergencylending.manager.BorrowStatusManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.RegularExpUtil;
import com.two.emergencylending.view.PopWinStatus;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：我的申请
 * 创建人：szx
 * 创建时间：2017/6/15.
 * 修改人：szx
 * 修改时间：2017/6/15.
 * 修改备注：
 */
public class MyApplyActivity extends BaseActivity implements Topbar.topbarClickListener, AdapterView.OnItemClickListener, View.OnClickListener, ControllerCallBack {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.topbar_status)
    TextView topbarStatus;
    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @Bind(R.id.tv_name_phone_remind)
    TextView tv_name_phone_remind;
    @Bind(R.id.et_name_phone)
    EditText et_name_phone;
    @Bind(R.id.reminder)
    TextView reminder;


    PopWinStatus popWinStatus;
    ClerkCustomerDetailAdapter clerkCustomerDetailAdapter;
    private List<ClerkCustomerBean> clerkCustomers = new ArrayList<ClerkCustomerBean>();
    CustomerManagerController customerManagerController;

    private int status = BorrowStatusManager.CLERK_STATUS_ALL;
    private String name;
    private String phoneNum;

    @Override
    public int setContent() {
        return R.layout.activity_my_apple;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        clerkCustomerDetailAdapter = new ClerkCustomerDetailAdapter(this, clerkCustomers);
        list.setAdapter(clerkCustomerDetailAdapter);
        list.setOnItemClickListener(this);
        customerManagerController = new CustomerManagerController(this, this);
        customerManagerController.queryCustomerRecord("", "", BorrowStatusManager.getClerkCustomerFormStatus(BorrowStatusManager.CLERK_STATUS_ALL), 30, true);

    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        topbarStatus.setOnClickListener(this);
        et_name_phone.addTextChangedListener(watcher);
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (clerkCustomers.get(position).getBorrow_status().equals("")) {
            String custId = clerkCustomers.get(position).getCust_id();
            String phone = clerkCustomers.get(position).getPhone();
            Intent intent = new Intent();
            intent.putExtra("cust_id", custId);
            intent.putExtra("phone", phone);
            CommonUtils.goToActivity(getContext(), ApplyActivity.class, intent);
        }
    }


    public void queryCustomerRecordByStatus(int status) {
        this.status = status;
        customerManagerController.queryCustomerRecord(this.phoneNum, this.name, BorrowStatusManager.getClerkCustomerFormStatus(this.status), 30, true);
    }

    public void queryCustomerRecordByPhone(String phone) {
        this.phoneNum = phone;
        customerManagerController.queryCustomerRecord(this.phoneNum, this.name, BorrowStatusManager.getClerkCustomerFormStatus(this.status), 30, true);
    }

    public void queryCustomerRecordByName(String name) {
        this.name = name;
        customerManagerController.queryCustomerRecord(this.phoneNum, this.name, BorrowStatusManager.getClerkCustomerFormStatus(this.status), 30, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_status:
                showPop();
                break;
            case R.id.status0:
                popWinStatus.selectItem(R.id.status0);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_ALL));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_ALL);
                break;
            case R.id.status1:
                popWinStatus.selectItem(R.id.status1);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_APPLY));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_APPLY);
                break;
            case R.id.status2:
                popWinStatus.selectItem(R.id.status2);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_AUDIT));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_AUDIT);
                break;
            case R.id.status3:
                popWinStatus.selectItem(R.id.status3);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_AUDIT_FAIL));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_AUDIT_FAIL);
                break;
            case R.id.status4:
                popWinStatus.selectItem(R.id.status4);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_CERTIFICATE));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_CERTIFICATE);
                break;
            case R.id.status5:
                popWinStatus.selectItem(R.id.status5);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_ELECTRONIC_SIGN));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_ELECTRONIC_SIGN);
                break;
            case R.id.status6:
                popWinStatus.selectItem(R.id.status6);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_SIGN_FAIL));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_SIGN_FAIL);
                break;
            case R.id.status7:
                popWinStatus.selectItem(R.id.status7);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_LOANING));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_LOANING);
                break;
            case R.id.status8:
                popWinStatus.selectItem(R.id.status8);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_LOANING_FAIL));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_LOANING_FAIL);
                break;
            case R.id.status9:
                popWinStatus.selectItem(R.id.status9);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_LOANING_SUCCESS));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_LOANING_SUCCESS);
                break;
            case R.id.status10:
                popWinStatus.selectItem(R.id.status10);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_CLOSED_ACCOUNT));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_CLOSED_ACCOUNT);
                break;
            case R.id.status11:
                popWinStatus.selectItem(R.id.status11);
                topbarStatus.setText(BorrowStatusManager.showClerkCustomerStatus(BorrowStatusManager.CLERK_STATUS_AUDIT_SUCESS));
                queryCustomerRecordByStatus(BorrowStatusManager.CLERK_STATUS_AUDIT_SUCESS);
                break;
        }
    }

    public void showPop() {
        if (popWinStatus == null) {
            //自定义的单击事件
            popWinStatus = new PopWinStatus(MyApplyActivity.this, this, 0);
            //监听窗口的焦点事件，点击窗口外面则取消显示
            popWinStatus.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        popWinStatus.dismiss();
                    }
                }
            });
        }
        if (status == BorrowStatusManager.CLERK_STATUS_ALL) {
            popWinStatus.selectItem(R.id.status0);
        }
        //设置默认获取焦点
        popWinStatus.setFocusable(true);
        //以某个控件的x和y的偏移量位置开始显示窗口
        popWinStatus.showAsDropDown(topbarStatus, 0, 0);
        //如果窗口存在，则更新
        popWinStatus.update();
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.QUERY_CUSTOMER_RECORD) {
            try {
                JSONObject json = new JSONObject(value);
                int count = json.getInt("count");
                String listJson = json.getString("list");
                if (count > 0) {
                    clerkCustomers.clear();
                    clerkCustomers = new Gson().fromJson(listJson, new TypeToken<List<ClerkCustomerBean>>() {
                    }.getType());
                    clerkCustomerDetailAdapter.refresh(clerkCustomers);
                    list.setVisibility(View.VISIBLE);
                    ll_nodata.setVisibility(View.GONE);
                    reminder.setVisibility(View.VISIBLE);
                } else {
                    list.setVisibility(View.GONE);
                    ll_nodata.setVisibility(View.VISIBLE);
                    reminder.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.QUERY_CUSTOMER_RECORD) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                tv_name_phone_remind.setVisibility(View.INVISIBLE);
                if (s.length() == 11 && RegularExpUtil.isMobile(s.toString())) {
                    LogUtil.d("", "查询手机号码：" + s.toString());
                    queryCustomerRecordByPhone(s.toString());
                }
                if (s.length() > 0 && s.length() <= 4 && RegularExpUtil.isChinese(s.toString())) {
                    LogUtil.d("", "查询姓名:" + s.toString());
                    queryCustomerRecordByName(s.toString());
                }
            } else {
                queryCustomerRecordByName("");
                tv_name_phone_remind.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            LogUtil.d("", "Editable:" + text);
        }
    };
}
