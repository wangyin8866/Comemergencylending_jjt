package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.RepayBorrowInfoBean;
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
 * Created by User on 2017/4/11.
 */

public class RepayBorrowInfoController {
    private Map parameter;
    private ControllerCallBack iController;
    private String msg = "";
    private Activity mActivty;

    public RepayBorrowInfoController(Activity activity, ControllerCallBack iController) {
        this.mActivty = activity;
        this.iController = iController;
        parameter = new HashMap();
    }

    public void getBorrowInfo() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d("RepayBorrowInfo", parameter.toString());
        OKManager.getInstance().sendComplexForm(NetContants.GET_REPAYBORROW_INFO, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                try {
                    String response = result.toString().trim();
                    Log.d("response", response);
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    String msg = jsonObject.optString("msg");
                    String resultRepay = jsonObject.optString("result");
                    RepayBorrowInfoBean borrowInfoBean = new Gson().fromJson(resultRepay, RepayBorrowInfoBean.class);
                    if (borrowInfoBean != null) {
                        UserInfoManager.getInstance().setRepayBorrowInfo(borrowInfoBean);
                    }
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        success(CallBackType.REPAYBORROW_INFO, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivty);
                    } else {
                        success(CallBackType.REPAYBORROW_INFO, msg);
                    }
                } catch (JSONException e) {
                    fail(CallBackType.REPAYBORROW_INFO, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(CallBackType.REPAYBORROW_INFO, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    private void fail(int returnCode, String errorMessage) {
        if (iController != null) {
            iController.onFail(returnCode, errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (iController != null) {
            iController.onSuccess(returnCode, value);
        }
    }
}
