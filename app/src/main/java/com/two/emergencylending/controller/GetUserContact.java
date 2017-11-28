package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.ContactBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2016/8/26.
 */
public class GetUserContact {
    private Activity mActivity;
    private IControllerCallBack iController;
    private Map parameter;
    private int fill_location;
    private String msg = "";

    public GetUserContact(Activity mActivity, IControllerCallBack iController) {
        this.mActivity = mActivity;
        this.iController = iController;
        parameter = new HashMap();
    }

    public void getUserContact(int fill_location) {
        if (!CommonUtils.isNetAvailable()) return;
        this.fill_location = fill_location;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
//        parameter.put("fill_location", String.valueOf(this.fill_location));
        Log.d("GetUserContactRequset", parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.getusercontact, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                Log.d("GetUserContactResponse", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        if (response.indexOf("result") != -1) {
                            String json = jsonObject.optString("result");
//                        String decodeValue = Des3.decode(json);
//                        LogUtil.d("GetUserData", decodeValue);
                            ContactBean contactBean = new Gson().fromJson(json, ContactBean.class);
                            UserInfoManager.getInstance().setContactBean(contactBean);
                            success(CallBackType.GET_CONTACT_INFO, msg);
                        } else {
                            UserInfoManager.getInstance().setContactBean(new ContactBean());
                            success(CallBackType.GET_CONTACT_INFO, msg);
                        }
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(msg);
                    }
                } catch (Exception e) {
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
        if (iController != null) {
            iController.onFail(errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (iController != null) {
            iController.onSuccess(returnCode, value);
        }
    }

}
