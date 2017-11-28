package com.two.emergencylending.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.AuthCodeController;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.CustomerManagerController;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.RegularExpUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：申请检测
 * 创建人：szx
 * 创建时间：2017/6/15.
 * 修改人：szx
 * 修改时间：2017/6/15.
 * 修改备注：
 */
public class ApplyCheckActivity extends BaseActivity implements Topbar.topbarClickListener, View.OnClickListener, ControllerCallBack {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.et_figure_code)
    EditText et_figure_code;
    @Bind(R.id.btn_figure_code)
    ImageView btn_figure_code;
    @Bind(R.id.verify_code)
    EditText verify_code;
    @Bind(R.id.btn_code)
    Button btn_code;
    @Bind(R.id.btn_next)
    Button next;

    private String phoneNum;
    private AuthCodeController authCodeController;
    private CustomerManagerController customerManagerController;

    @Override
    public int setContent() {
        return R.layout.activity_apple_check;
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
        authCodeController = new AuthCodeController(this, this, btn_code);
        customerManagerController = new CustomerManagerController(this, this);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        btn_figure_code.setOnClickListener(this);
        btn_code.setOnClickListener(this);
        next.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_figure_code:
                break;
            case R.id.btn_code:
                if (StringUtil.isNullOrEmpty(phone.getText().toString())) {
                    ToastAlone.showShortToast(IApplication.globleContext, "请输入手机号码！");
                } else if (!RegularExpUtil.isMobile(phone.getText().toString())) {
                    ToastAlone.showShortToast(IApplication.globleContext, "请输入正确的手机号码！");
                } else {
                    authCodeController.getAuthCode(authCodeController.MSG_REG_, phone.getText().toString(), true);
                }
                break;
            case R.id.btn_next:
                if (StringUtil.isNullOrEmpty(phone.getText().toString())) {
                    ToastAlone.showShortToast(IApplication.globleContext, "请输入手机号码！");
                } else if (!RegularExpUtil.isMobile(phone.getText().toString())) {
                    ToastAlone.showShortToast(IApplication.globleContext, "请输入正确的手机号码！");
                } else if (StringUtil.isNullOrEmpty(verify_code.getText().toString())) {
                    ToastAlone.showShortToast(IApplication.globleContext, "请输入验证码！");
                } else {
                    phoneNum = phone.getText().toString();
                    customerManagerController.getCustomeerID(phoneNum, verify_code.getText().toString());
                }
                break;
        }
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.GET_CUSTOMER_ID) {
            Intent intent = new Intent();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(value);
                String cust_id = jsonObject.getString("cust_id");
                intent.putExtra("cust_id", cust_id);
                intent.putExtra("phone", phoneNum);
                CommonUtils.goToActivity(this, ApplyActivity.class, intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.GET_CUSTOMER_ID) {
            ToastAlone.showShortToast(this, errorMessage);
        }
    }
}
