package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2016/8/24.
 */
public class AddJobWorkInforCotroller {
    private Activity mActivity;
    private Map<String, String> parameter;
    private ControllerCallBack mIController;
    private String msg = "";
    private JobInforBean jobInforBean;
    public AddJobWorkInforCotroller(Activity activity, ControllerCallBack iController) {
        this.mActivity = activity;
        this.mIController = iController;
        parameter = new HashMap<>();
    }

    public void addWorkInfor(JobInforBean jobInforBean) {
        if (!CommonUtils.isNetAvailable()) return;
        CommonUtils.showDialog(mActivity, "正在努力加载.....");
        this.jobInforBean = jobInforBean;
        parameter.put("professional", jobInforBean.getProfessional());
        parameter.put("month_pay", jobInforBean.getMonth_pay());//月收入
        parameter.put("unit_name",jobInforBean.getUnit_name());//单位名字
        parameter.put("unit_phone", jobInforBean.getUnit_phone());
        parameter.put("unit_adr", jobInforBean.getUnit_adr());
        parameter.put("unit_adr_detail", jobInforBean.getUnit_adr_detail());
        parameter.put("unit_department", jobInforBean.getUnit_department());
        parameter.put("unit_industry",jobInforBean.getUnit_industry());
        parameter.put("title", jobInforBean.getTitle());
        parameter.put("product_id", UserInfoManager.getInstance().getPerSonalBean().getProduct_id());
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d("Request", parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.userJob, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                Log.d("response", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        success(CallBackType.SAVA_JOB_INFOR, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.SAVA_JOB_INFOR,msg);
                    }
                } catch (JSONException e) {
                    fail(CallBackType.SAVA_JOB_INFOR,ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(CallBackType.SAVA_JOB_INFOR,ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    private void fail(int returnCode,String errorMessage) {
        if (mIController != null) {
            mIController.onFail(returnCode,errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (mIController != null) {
            mIController.onSuccess(returnCode, value);
        }
    }
}
