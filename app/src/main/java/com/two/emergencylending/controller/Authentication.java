package com.two.emergencylending.controller;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2016/8/29.
 */
public class Authentication {
    Activity mActivity;
    Map<String, String> parameter;
    private int flag, type;
    private String code, account, password;
    public static final int MobileAuth_type = 1;
    public static final int JingDongAuth_type = 2;

    public Authentication(Activity activity) {
        this.mActivity = activity;
        parameter = new HashMap<>();
    }

    public void authenTicate(String account, String password, String code, int flag) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        this.account = account;
        this.password = password;
        this.code = code;
        this.flag = flag;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("account", this.account);
        parameter.put("password", this.password);
        if (!TextUtils.isEmpty(code)) {
            parameter.put("captcha", code);
            parameter.put("type", 2 + "");
        } else {
            parameter.put("type", 1 + "");
        }
        parameter.put("auth_type", flag + "");
        OKManager.getInstance().sendComplexForm(NetContants.JUXINLI_AUTH, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                LogUtil.d("AuthenticationResponse", response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });

    }
}
