package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：开户查询
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class AccountQueryAndSvaeController {
    private static final String TAG = AccountQueryAndSvaeController.class.getSimpleName();
    private  Activity mActivity;
    private  Map<String, String> parameter;

    public void accoutntDetail(Activity activity) {
        parameter = new HashMap<>();
        this.mActivity = activity;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d(TAG, parameter.toString());
        OKManager.getInstance().sendComplexForm(NetContants.ACCOUNTDEAL, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                Log.d(TAG, result.toString());
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

}
