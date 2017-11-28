package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.PhoneInfoUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2016/8/29.
 */
public class WyController {


    public static void uploadAppLog(Activity mActivity, String title, String methodName, String message) {
        Map parameter = new HashMap();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("title", title);
        parameter.put("url", methodName);
        String brand = PhoneInfoUtils.getPhoneBrand();
        String model = PhoneInfoUtils.getPhoneModel();
        String version = PhoneInfoUtils.getSystemVersion();
        parameter.put("note", "Android:" + CommonUtils.getVersionName(mActivity) + ":" + brand + ":" + model + ":" + version + ":" + message);

        OKManager.getInstance().sendComplexFormNo(NetContants.APP_LOG, parameter, new OKManager.Func1() {

            @Override
            public void onResponse(String result) {
                Log.e("wyresult", result);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("wyresult_onFailure", e.getMessage());
            }
        });
    }
}