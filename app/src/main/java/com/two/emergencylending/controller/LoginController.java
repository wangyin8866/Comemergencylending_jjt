package com.two.emergencylending.controller;

import android.app.Activity;

import com.igexin.sdk.PushManager;
import com.two.emergencylending.activity.LoginAndRegisterModule.UpDatepwdActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.Des3;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CommonalityFieldUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.PhoneInfoUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToastAlone;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 36420 on 2016/08/21.
 */
public class LoginController {
    private String TAG = LoginController.class.getSimpleName();
    Map parameter;
    Activity mActivity;
    IControllerCallBack mIController;
    String mobile, password;

    public LoginController(Activity activity, IControllerCallBack iController) {
        this.mActivity = activity;
        this.mIController = iController;
        parameter = new HashMap();
    }

    public void login(String mobile, final String password, final boolean showProgress) {
        if (!CommonUtils.isNetAvailable()) return;
        this.mobile = mobile;
        this.password = password;
        String brand = PhoneInfoUtils.getPhoneBrand();
        String model = PhoneInfoUtils.getPhoneModel();
        String version = PhoneInfoUtils.getSystemVersion();
        parameter.put("app_version_no", CommonUtils.getVersionName(mActivity));
        parameter.put("register_platform", CommonalityFieldUtils.getDittchStr());
        parameter.put("phone", this.mobile);
        parameter.put("password", this.password);
        parameter.put("login_ip", CommonUtils.getIp(mActivity));
        String clientid = PushManager.getInstance().getClientid(mActivity.getApplicationContext());
        if (StringUtil.isNullOrEmpty(clientid)) {
            PushManager.getInstance().initialize(IApplication.globleContext);
            clientid = PushManager.getInstance().getClientid(IApplication.globleContext);
        }
        parameter.put("clientid", clientid);
        parameter.put("login_platform", CommonalityFieldUtils.getDittchStr());
        parameter.put("login_device_no", brand + "," + model + "," + version);
        parameter.put("longitude", IApplication.longitude);
        parameter.put("latitude", IApplication.latitude);
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.LOGIN, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                if (showProgress) {
                    CommonUtils.closeDialog();
                }
                String response = result.toString().trim();
                LogUtil.d(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        String object = jsonObject.optString("result");
                        String decodeValue = Des3.decode(object);
                        JSONObject t = new JSONObject(decodeValue);
                        String juid = t.optString("juid");
                        String login_token = t.optString("login_token");
                        String recommendCode = t.optString("recommendCode");
                        String is_clerk = t.optString("is_clerk");
                        String renew_loans = t.optString("renew_loans");
                        LogUtil.i("juid:", juid);
                        LogUtil.i("recommendCode:", recommendCode);
                        LogUtil.i("login_token:", login_token);
                        LogUtil.i("is_clerk:", is_clerk);
                        UserInfoManager.getInstance().setJuid(juid);
                        UserInfoManager.getInstance().setLogin_token(login_token);
                        IApplication.getInstance().clearInfoCache(mActivity);//清除资料缓存
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.IS_CLERK, is_clerk);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.SHARE_CODE, recommendCode);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.JUID, juid);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.RENEW_LOANS, renew_loans);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.LOGIN_TOKEN, login_token);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.USERNAME, LoginController.this.mobile);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.PASSWORD, LoginController.this.password);
                        IApplication.isLoginOut = false;
                        success(CallBackType.LOGIN, object);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else if (flag.equals(ErrorCode.LOGIN_TO_FORGET_PASSWORD)) {
                        //增加提示
                        ToastAlone.showLongToast(mActivity, "密码错误次数超过5次");
                        CommonUtils.goToActivity(mActivity, UpDatepwdActivity.class);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(msg);
                        LogUtil.d(TAG, msg);
                    }
                } catch (Exception e) {
                    fail(ErrorCode.FAIL_DATA);
                    LogUtil.e(TAG, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(ErrorCode.FAIL_NETWORK);
                LogUtil.e(TAG, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void success(int returnCode, String value) {
        if (mIController != null) {
            mIController.onSuccess(returnCode, value);
        }
    }

    public void fail(String errorMessage) {
        if (mIController != null) {
            mIController.onFail(errorMessage);
        }
    }
}
