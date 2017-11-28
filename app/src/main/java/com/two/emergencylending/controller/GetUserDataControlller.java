package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.chinazyjr.lib.util.Logger;
import com.google.gson.Gson;
import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.PerSonalBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 36420 on 2016/08/24.
 */
public class GetUserDataControlller {
    public Activity mActivity;
    private Map parameter;
    private ControllerCallBack mIController;
    private String msg = "";

    public GetUserDataControlller(Activity activity, ControllerCallBack iController) {
        this.mActivity = activity;
        this.mIController = iController;
        parameter = new HashMap();
    }

    public void getuserPersonalData() {
        if (!CommonUtils.isNetAvailable()) return;
        if (!PersonalDataActivity.isShowAuthor) {
            parameter.put("route", "0");
        } else {
            parameter.put("route", "1");
            parameter.put("borrow_limit", UserInfoManager.getInstance().getBorrowInfo().getBorrow_limit());
        }
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Logger.d("GetUserDataControlller", parameter.toString().trim());
        CommonUtils.showDialog(mActivity, "加载中");
        OKManager.getInstance().sendComplexForm(NetContants.getuserdata, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                try {
                    CommonUtils.closeDialog();
                    String responseInfo = result.toString().trim();
                    Log.d("GetUserDataControlller", responseInfo);
                    JSONObject object = null;
                    object = new JSONObject(responseInfo);
                    String flag = object.optString("flag");
                    msg = object.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        if (responseInfo.indexOf("result") != -1) {
                            String jsonObject = object.optString("result");
                            PerSonalBean perSonalBean = new Gson().fromJson(jsonObject, PerSonalBean.class);
                            if (!StringUtil.isNullOrEmpty(perSonalBean.getResult())) {
                                JSONObject res = new JSONObject(perSonalBean.getResult());
                                if (res.has("online_msg")) {
                                    PersonalDataActivity.offline_calp_msg = res.getString("online_msg");
                                } else {
                                    PersonalDataActivity.offline_calp_msg = null;
                                }
                                if (res.has("org_no")) {
                                    PersonalDataActivity.org_no = res.getString("org_no");
                                } else {
                                    PersonalDataActivity.org_no = null;
                                }
                                if (res.has("sall_emp_no")) {
                                    PersonalDataActivity.sall_emp_no = res.getString("sall_emp_no");
                                } else {
                                    PersonalDataActivity.sall_emp_no = null;
                                }
                                if (res.has("org_name")) {
                                    PersonalDataActivity.org_name = res.getString("org_name");
                                } else {
                                    PersonalDataActivity.org_name = null;
                                }
                            }
                            UserInfoManager.getInstance().setPerSonalBean(perSonalBean);
                            success(CallBackType.GET_PERSONAL_INFOR, msg);
                        } else {
                            UserInfoManager.getInstance().setPerSonalBean(new PerSonalBean());
                            success(CallBackType.GET_PERSONAL_INFOR, msg);
                        }
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        UserInfoManager.getInstance().setPerSonalBean(new PerSonalBean());
                        IApplication.getInstance().backToLogin(mActivity);
                        return;
                    } else {
                        UserInfoManager.getInstance().setPerSonalBean(new PerSonalBean());
                        fail(CallBackType.GET_PERSONAL_INFOR, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.closeDialog();
                    fail(CallBackType.GET_PERSONAL_INFOR, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                CommonUtils.closeDialog();
                UserInfoManager.getInstance().setPerSonalBean(new PerSonalBean());
                fail(CallBackType.GET_PERSONAL_INFOR, ErrorCode.FAIL_NETWORK);
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
