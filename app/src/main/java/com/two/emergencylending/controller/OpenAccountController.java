package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.IdentityConfig;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.Des3;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：开户
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class OpenAccountController {
    private static final String TAG = OpenAccountController.class.getSimpleName();
    private Activity mActivity;
    private Map<String, String> parameter;
    ControllerCallBack mController;

    public OpenAccountController(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mController = controller;
        parameter = new HashMap<>();
    }

    public void openAccount() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("page_type", IdentityConfig.PAGE_TYPE);
        parameter.put("client_", IdentityConfig.CLIENT_);
        Log.d(TAG, parameter.toString());
        OKManager.getInstance().sendComplexForm(NetContants.OPEN_ACCOUNT, parameter, new OKManager.Func1() {

            @Override
            public void onResponse(String result) {
                Log.d(TAG, result.toString());
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            String res = Des3.decode(json.getString("result"));
                            JSONObject jsonObject = new JSONObject(res);
                            String H5Data = jsonObject.getString("openAccoutH5Desc");
                            success(CallBackType.OPEN_ACCOUNT, H5Data);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.OPEN_ACCOUNT, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.OPEN_ACCOUNT, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.OPEN_ACCOUNT, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.OPEN_ACCOUNT, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }

        });
    }

    public void accoutntDetail() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
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

    public void success(int returnCode, String value) {
        if (mController != null) {
            mController.onSuccess(returnCode, value);
        }
    }

    public void fail(int returnCode, String errorMessage) {
        if (mController != null) {
            mController.onFail(returnCode, errorMessage);
        }
    }
}
