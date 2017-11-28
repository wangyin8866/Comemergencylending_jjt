package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2017/1/19.
 */

public class LoanTypeController {
    private Map parameter;
    private Activity mActivity;
    private IControllerCallBack mCall;
    private String msg="";
    public LoanTypeController(Activity activity, IControllerCallBack call){
        this.mActivity = activity;
        this.mCall = call;
        parameter = new HashMap();
    }
    public void loanType(){
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d("LoanTypeController",parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.UserLoanType, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                Log.d("LoanTypeController", response);
                try {
                    JSONObject    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        String jsonObject1 = jsonObject.optString("result");
                        JSONObject t = new JSONObject(jsonObject1);
                        int loan_type = t.optInt("loan_type");
                        UserInfoManager.getInstance().setLoan_type(loan_type);
                        success(112,msg);
                    }else {
                        fail(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
             
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }
    private void fail(String errorMessage) {
        if (mCall != null) {
            mCall.onFail(errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (mCall != null) {
            mCall.onSuccess(returnCode, value);
        }
    }
}
