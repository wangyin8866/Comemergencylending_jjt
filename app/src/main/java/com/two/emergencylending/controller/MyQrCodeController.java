package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.UserInfoManager;
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
 * Created by User on 2016/11/14.
 */

public class MyQrCodeController {
    private Activity activity;
    private Map parameter;
    private IControllerCallBack iControllerCallBack;
    private String msg;
    private static final String TAG = MyQrCodeController.class.getSimpleName();

    public void getMyCode(final Activity activity, IControllerCallBack iControllerCallBackI) {
        if (!CommonUtils.isNetAvailable()) return;
        this.activity = activity;
        this.iControllerCallBack = iControllerCallBackI;
        parameter = new HashMap();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        OKManager.getInstance().sendComplexForm(NetContants.MY_QR_CODE, parameter, new OKManager.Func1() {

            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                Log.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
//                        String decodeValue = Des3.decode(object);
//                        LogUtil.d("GetMyStatuDes3", decodeValue);
                        String resultData = jsonObject.optString("result");
                        JSONObject t = new JSONObject(resultData);
                        String qr_code_url = t.optString("qr_code_url");
                        String qr_code = t.optString("qr_code");
                        UserInfoManager.getInstance().setQr_code_url(qr_code_url);
                        UserInfoManager.getInstance().setQr_code(qr_code);
                        success(CallBackType.MY_CODE, msg);
                    } 
//                    else if(flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)){
//                        IApplication.getInstance().backToLogin(activity);
//                    }else {
//                        
//                    }
                } catch (JSONException e) {
                    fail(ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    private void fail(String errorMessage) {
        if (iControllerCallBack != null) {
            iControllerCallBack.onFail(errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (iControllerCallBack != null) {
            iControllerCallBack.onSuccess(returnCode, value);
        }
    }
}
