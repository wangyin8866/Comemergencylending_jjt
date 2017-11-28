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
 * 类描述：电子签约地址获取
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */

public class ElectronicAgreementController {
    private static final String TAG = ElectronicAgreementController.class.getSimpleName();
    private Activity mActivity;
    private Map<String, String> parameter;
    ControllerCallBack mController;

    public ElectronicAgreementController(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mController = controller;
        parameter = new HashMap<>();
    }

    public void getElectronicAgreementUrl() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d(TAG, parameter.toString());
        OKManager.getInstance().sendComplexForm(NetContants.ELECTRONIC_AGREEMENT_GET_URL, parameter, new OKManager.Func1() {
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
                            success(CallBackType.ELECTRONIC_AGREEMENT, res);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.ELECTRONIC_AGREEMENT, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.ELECTRONIC_AGREEMENT, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.ELECTRONIC_AGREEMENT, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.ELECTRONIC_AGREEMENT, ErrorCode.FAIL_NETWORK);
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
