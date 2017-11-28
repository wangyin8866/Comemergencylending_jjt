package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 线下产品无需调用认证
 */

public class OffLinewSubJXLController {
    private static final String TAG = OffLinewSubJXLController.class.getSimpleName();
    private Activity mActivity;
    private Map<String, String> parameter;
    ControllerCallBack mController;

    public OffLinewSubJXLController(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mController = controller;
        parameter = new HashMap<>();
    }

    public void offlineSubMitCustomer(String custId) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        CommonUtils.showDialog(mActivity, "加载中...");
        parameter.put("cust_id", custId);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        OKManager.getInstance().sendComplexForm(NetContants.offLineSubmitJxl, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                Log.d(TAG, result.toString().trim());
                String response = result.toString().trim();
                try {
                    JSONObject json = new JSONObject(response);
                    String code = json.getString("flag");
                    String msg = json.getString("msg");
                    if ("0000".equals(code)) {
                        success(CallBackType.CUSTOMER_OFFLINE_SKIP_JXL, msg);
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.CUSTOMER_OFFLINE_SKIP_JXL, msg);
                    }
                } catch (JSONException e) {
                    fail(CallBackType.CUSTOMER_OFFLINE_SKIP_JXL, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.CUSTOMER_OFFLINE_SKIP_JXL, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void offlineSubMit() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        CommonUtils.showDialog(mActivity, "加载中...");
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        OKManager.getInstance().sendComplexForm(NetContants.offLineSubmitJxl, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                Log.d(TAG, result.toString().trim());
                String response = result.toString().trim();
                try {
                    JSONObject json = new JSONObject(response);
                    String code = json.getString("flag");
                    String msg = json.getString("msg");
                    if ("0000".equals(code)) {
                        success(CallBackType.OFFLINE_SKIP_JXL, msg);
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.OFFLINE_SKIP_JXL, msg);
                    }
                } catch (JSONException e) {
                    fail(CallBackType.OFFLINE_SKIP_JXL, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.OFFLINE_SKIP_JXL, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
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
