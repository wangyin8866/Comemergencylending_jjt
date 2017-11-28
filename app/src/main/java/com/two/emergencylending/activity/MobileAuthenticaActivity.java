package com.two.emergencylending.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.WyController;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：手机认证页面
 * 创建人：wyp
 * 创建时间：2016/5/26 14:49
 * 修改人：wyp
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class MobileAuthenticaActivity extends BaseActivity implements Topbar.topbarClickListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    @Bind(R.id.credentials_topbar)
    Topbar credentialsTopbar;
    @Bind(R.id.mobile_phone_pwd)
    EditText mobile_phone_pwd;
    @Bind(R.id.mobile_phone_account)
    EditText mobile_phone_account;
    @Bind(R.id.cb_check)
    CheckBox cbCheck;
    private boolean isSelected = true;
    CustomerDialog dialog;
    @Bind(R.id.mobile_phone_code)
    EditText mobile_phone_code;
    @Bind(R.id.ll_code)
    LinearLayout ll_code;
    private boolean isCAPTCHA;
    Map parameter;
    private String isEffective;//0:无效 1：有效

    @Override
    public int setContent() {
        return R.layout.layout_mobileauthentica;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }


    @Override
    public void init() {
//        authentication = new Authentication(this);
        dialog = new CustomerDialog(getContext());
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        credentialsTopbar.getLeftIco().setImageResource(R.drawable.icon_back);
        credentialsTopbar.getLeftIco().setVisibility(View.VISIBLE);
        credentialsTopbar.getRightButton().setTextColor(IApplication.globleResource.getColor(R.color.title));
        credentialsTopbar.getRightButton().setVisibility(View.VISIBLE);
        credentialsTopbar.getRightButton().setText("确定");
        mobile_phone_account.setText(SharedPreferencesUtil.getInstance(this).getString(SPKey.USERNAME));
        cbCheck.setChecked(isSelected);
        parameter = new HashMap<>();
        //从后台获取isEffective 值，判断时候有效

        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        try {
            OKManager.getInstance().sendComplexForm(NetContants.JUXINLI_AUTH_2, parameter, new OKManager.Func1() {
                @Override
                public void onResponse(String result) {
                    CommonUtils.closeDialog();
                    String response = result.toString().trim();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject = jsonObject.getJSONObject("result");
                        isEffective = jsonObject.optString("status");
                        Log.e("wy_isEffective", isEffective);
                        if ("1".equals(isEffective)) {//如果有回执参数需要直接去保存  //有效
                            phone_account = mobile_phone_account.getText().toString().trim();
                            phone_pwd = UserInfoManager.getInstance().getPhone_pwd();
                            phone_account = mobile_phone_account.getText().toString().trim();
                            phone_pwd = UserInfoManager.getInstance().getPhone_pwd();
                            mobile_phone_pwd.setText(phone_pwd);
                            ll_code.setVisibility(View.VISIBLE);
                            //                        authenTicate();
                        } else {
                            mobile_phone_pwd.setText("");
                            ll_code.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        WyController.uploadAppLog(MobileAuthenticaActivity.this, "运营商异常", NetContants.JUXINLI_AUTH_2, e.getMessage());
                        ToastAlone.showLongToast(MobileAuthenticaActivity.this, "数据异常,请重试");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    ToastAlone.showLongToast(MobileAuthenticaActivity.this, "网络超时");
                    CommonUtils.closeDialog();
                }
            });
        } catch (Exception e) {
            WyController.uploadAppLog(MobileAuthenticaActivity.this, "运营商异常", NetContants.JUXINLI_AUTH_2, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        credentialsTopbar.setOnTopbarClickListener(this);
        cbCheck.setOnCheckedChangeListener(this);
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
        confirm();
    }


    @OnClick({R.id.tv_agreement_register, R.id.iv_question})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement_register:
                CommonUtils.goToActivity(getContext(), MobileProtocolActivity.class);
                break;
            case R.id.iv_question:
                phoneAuthenticaDialog();
                break;
        }
    }

    private void phoneAuthenticaDialog() {
        dialog.mobileDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.tv_move) {
                    CommonUtils.callPhone(getContext(), "10086");
                    dialog.dismiss();
                } else if (view.getId() == R.id.tv_unicom) {
                    CommonUtils.callPhone(getContext(), "10010");
                    dialog.dismiss();
                } else if (view.getId() == R.id.tv_telecommunication) {
                    CommonUtils.callPhone(getContext(), "10001");
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    String phone_pwd = "", phone_account = "", code = "";

    public void confirm() {
        phone_account = mobile_phone_account.getText().toString().trim();
        phone_pwd = mobile_phone_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(phone_account)) {
            ToastAlone.showLongToast(getContext(), "手机账号不能为空!");
            return;
        }
        if (TextUtils.isEmpty(phone_pwd)) {
            ToastAlone.showLongToast(getContext(), "手机认证密码不能为空!");
            return;
        }
        if (!isSelected) {
            ToastAlone.showToast(getContext(), "请同意协议！", Toast.LENGTH_SHORT);
            return;
        }
        if (isCAPTCHA) {
            code = mobile_phone_code.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                ToastAlone.showToast(this, "请输入您的验证码!", 0);
                return;
            }
            authenTicate();
        } else {
            authenTicate();
        }

    }

    public void authenTicate() {
        if (!CommonUtils.isNetAvailable()) return;

        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("account", phone_account);
        parameter.put("password", phone_pwd);
        if ("1".equals(isEffective)) {
            parameter.put("captcha", code);
            parameter.put("type", "2");
            if (!TextUtils.isEmpty(UserInfoManager.getInstance().getJunxinlinPhone())) {
                parameter.put("huizhijilu", UserInfoManager.getInstance().getJunxinlinPhone());
            }
        } else {
            parameter.put("type", "1");
        }
        parameter.put("auth_type", "1");
        LogUtil.d("Request", parameter.toString().trim());
        CommonUtils.showDialog(MobileAuthenticaActivity.this, "正在努力加载....");
        OKManager.getInstance().sendComplexForm(NetContants.JUXINLI_AUTH, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                Log.e("JUXINLI_AUTH", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.optString("msg");
                    String flag = jsonObject.optString("flag");
                    if (flag.equals(ErrorCode.JXL_AUTHCODE_SUCCESS)) {
                        isEffective = "1";
                        mobile_phone_code.setText("");
                        if (jsonObject.has("result")) {
                            String junxinlinPhone = jsonObject.optString("result");
                            if (!TextUtils.isEmpty(junxinlinPhone)) {//回执参数不为空
                                UserInfoManager.getInstance().setJunxinlinPhone(junxinlinPhone);
                            }
                        }
                        //保存密码
                        UserInfoManager.getInstance().setPhone_pwd(phone_pwd);
                        isCAPTCHA = true;
                        ll_code.setVisibility(View.VISIBLE);
                        ToastAlone.showLongToast(MobileAuthenticaActivity.this, msg);
                    } else if (flag.equals(ErrorCode.SUCCESS)) {
                        isEffective = "0";
                        Log.e("wy", "asdasdasd");
                        ToastAlone.showLongToast(MobileAuthenticaActivity.this, msg);
                        finish();
                    } else {
                        isEffective = "0";
                        ToastAlone.showLongToast(MobileAuthenticaActivity.this, msg);
                    }
                } catch (JSONException e) {
                    isEffective = "0";
                    ToastAlone.showLongToast(MobileAuthenticaActivity.this, "数据异常,请重试");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                isEffective = "0";
                e.printStackTrace();
                ToastAlone.showLongToast(MobileAuthenticaActivity.this, "网络超时，请返回重新认证");
                CommonUtils.closeDialog();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isSelected = true;
        } else {
            isSelected = false;
        }
    }
}
