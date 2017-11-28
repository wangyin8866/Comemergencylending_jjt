package com.two.emergencylending.controller;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.bean.UserStatuModel;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2016/8/29.
 */
public class GetMyStatu {
    private Activity mActivity;
    private Map parameter;
    private ControllerCallBack mIController;
    private String msg;

    public GetMyStatu(Activity activity, ControllerCallBack iController) {
        this.mActivity = activity;
        this.mIController = iController;
        parameter = new HashMap();
    }

    public void getMystatu() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        LogUtil.d("GetMyStatuRequest", parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.getmydata, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                LogUtil.d("GetMyStatuResponse", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        String object = jsonObject.optString("result");
//                        String decodeValue = Des3.decode(object);
//                        LogUtil.d("GetMyStatuDes3", decodeValue);
                        UserStatuModel userStatu = new Gson().fromJson(object, new TypeToken<UserStatuModel>() {
                        }.getType());
                        if (object.indexOf("user_data_status") == -1) {
                            userStatu.setUserDataStatus(0);//morende 
                        }
                        if (object.indexOf("user_job_status") == -1) {
                            userStatu.setUserJobStatus(0);//morende 
                        }
                        if (object.indexOf("user_contact_status") == -1) {
                            userStatu.setUserContactStatus(0);//morende 
                        }
                        UserInfoManager.getInstance().setUserStatuModels(userStatu);
                        UserInfoManager.getInstance().setAdvater(userStatu.getHeadPic());
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.NAME, userStatu.getUsername());
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.ID_CARD, userStatu.getIdcard());
                        success(CallBackType.GET_USER_STATU, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.GET_USER_STATU, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.GET_USER_STATU, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(CallBackType.GET_USER_STATU, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }


    public void getMystatuFromAuth() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        LogUtil.d("getMystatuFromAuth", parameter.toString().trim());
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.getmydata, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                LogUtil.d("getMystatuFromAuth", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        String object = jsonObject.optString("result");
                        success(CallBackType.GET_USER_STATU_FROM_AUTH, object);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.GET_USER_STATU_FROM_AUTH, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.GET_USER_STATU_FROM_AUTH, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.GET_USER_STATU_FROM_AUTH, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }


    private void fail(int returnCode, String errorMessage) {
        if (mIController != null) {
            mIController.onFail(returnCode, errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (mIController != null) {
            mIController.onSuccess(returnCode, value);
        }
    }
}
