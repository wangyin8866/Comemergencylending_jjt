package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.JobInforBean;
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
 * Created by User on 2016/8/24.
 */
public class QueryWorkinforController {
    private Activity mActivity;
    private Map parameter;
    private ControllerCallBack iController;
    private String msg = "";

    public QueryWorkinforController(Activity Activity, ControllerCallBack iController) {
        this.mActivity = Activity;
        this.iController = iController;
        parameter = new HashMap();
    }

    public void queryWorkInfor() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d("QueryWorkinfor", parameter.toString());
        CommonUtils.showDialog(mActivity, "加载中");
        OKManager.getInstance().sendComplexForm(NetContants.getUserJobByCons, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                Log.d("QueryWorkinfor", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    String msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        if (response.indexOf("result") != -1) {
                            String json = jsonObject.optString("result");
                            JobInforBean jobinfo = new Gson().fromJson(json, JobInforBean.class);
                            UserInfoManager.getInstance().setJobBean(jobinfo);
                            success(CallBackType.GET_JOB_INFOR, msg);
                        } else {
                            UserInfoManager.getInstance().setJobBean(new JobInforBean());
                            success(CallBackType.GET_JOB_INFOR, msg);
                        }
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.GET_JOB_INFOR, msg);
                    }

                } catch (Exception e) {
                    fail(CallBackType.GET_JOB_INFOR, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.GET_JOB_INFOR, ErrorCode.FAIL_NETWORK);
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
