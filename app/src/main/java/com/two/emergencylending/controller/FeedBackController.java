package com.two.emergencylending.controller;

import android.app.Activity;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
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
public class FeedBackController {
    private String msg;
    private Activity mActivity;
    private Map parameter;
    private String mFeedBack;
    private ControllerCallBack feedCallBack;

    public FeedBackController(Activity mActivity, ControllerCallBack iControllerCallBack) {
        this.mActivity = mActivity;
        this.feedCallBack = iControllerCallBack;
        parameter = new HashMap();
    }

    public void feedBack(String feedBack) {
        if (!CommonUtils.isNetAvailable()) return;
        this.mFeedBack = feedBack;
        parameter.put("feedbackDetail", this.mFeedBack);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "正在努力加载中......");
        OKManager.getInstance().sendComplexForm(NetContants.feedback, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                LogUtil.d("resopnse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    msg = jsonObject.optString("msg");
                    String flag = jsonObject.optString("flag");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        success(CallBackType.SUBMIT_SUGGESTION, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.SUBMIT_SUGGESTION,msg);
                    }
                } catch (JSONException e) {
                    fail(CallBackType.SUBMIT_SUGGESTION,ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(CallBackType.SUBMIT_SUGGESTION,ErrorCode.FAIL_NETWORK);
                e.printStackTrace();

            }
        });
    }

    private void fail(int returnCode,String errorMessage) {
        if (feedCallBack != null) {
            feedCallBack.onFail(returnCode,errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (feedCallBack != null) {
            feedCallBack.onSuccess(returnCode, value);
        }
    }
}
