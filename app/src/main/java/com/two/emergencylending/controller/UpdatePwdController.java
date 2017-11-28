package com.two.emergencylending.controller;

import android.app.Activity;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CommonalityFieldUtils;
import com.two.emergencylending.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2016/8/22.
 */
public class UpdatePwdController {
    private Activity mActivity;
    private Map parameter;
    private String phone, code, pwd;
    private IControllerCallBack mIController;
    private String msg = "";

    public UpdatePwdController(Activity activity, IControllerCallBack mIController) {
        this.mActivity = activity;
        this.mIController = mIController;
        parameter = new HashMap();
    }

    public void updatePassword(String phone, String code, String pwd) {
        if (!CommonUtils.isNetAvailable()) return;
        this.phone = phone;
        this.code = code;
        this.pwd = pwd;
        parameter.clear();
        parameter.put("phone", this.phone);
        parameter.put("verify_code", this.code);
        parameter.put("password", this.pwd);
        parameter.put("register_platform", CommonalityFieldUtils.getDittchStr());
        OKManager.getInstance().sendComplexForm(NetContants.UPDATEPASSWORD, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                LogUtil.d("respones", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        success(CallBackType.UPDATE_PASSWORD, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(msg);
                    }
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
        if (mIController != null) {
            mIController.onFail(errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (mIController != null) {
            mIController.onSuccess(returnCode, value);
        }
    }

}
