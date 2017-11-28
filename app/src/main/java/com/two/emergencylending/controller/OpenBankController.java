package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.CodeBean;
import com.two.emergencylending.bean.OpenBank;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2017/1/18.
 */

public class OpenBankController {
    private IControllerCallBack mIController;
    private Activity activity;
    private Map parameter;
    private String msg = "";

    public OpenBankController(Activity activity, IControllerCallBack callBack) {
        this.activity = activity;
        this.mIController = callBack;
        parameter = new HashMap<>();
    }

    public void openBank(String city) {
        parameter.put("city_name", city);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d("parameter", parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.OPENBARD, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                try {
                    String respond = result.toString();
                    JSONObject object = new JSONObject(respond);
                    String flag = object.optString("flag");
                    msg = object.optString("msg");
                    Log.d("GPSLocationMSG", respond);
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        JSONObject jsonObject = object.optJSONObject("result");

                        String count = jsonObject.optString("count");
                        int loan_type = jsonObject.optInt("loan_type");
                        UserInfoManager.getInstance().setLoan_type(loan_type);
                        UserInfoManager.getInstance().setCount(count);
                        List<OpenBank> openBank = new Gson().fromJson(jsonObject.optString("bankList"), new TypeToken<List<OpenBank>>() {
                        }.getType());
                        if (openBank != null && openBank.size() > 0) {
                            List<CodeBean> codeBeanList = new ArrayList<CodeBean>();
                            for (int i = 0; i < openBank.size(); i++) {
                                codeBeanList.add(new CodeBean(i, openBank.get(i).getCode_(), openBank.get(i).getDesc_()));
                            }
                            UserInfoManager.getInstance().setOpenBanks(codeBeanList);
                        }
                        success(1222, msg);
                    }else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(activity);
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
