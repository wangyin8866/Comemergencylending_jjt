package com.two.emergencylending.controller;

import android.app.Activity;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToastAlone;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2016/8/22.
 */
public class LogoutController {
    private Activity mActivity;
    private Map parameter;
    private IControllerCallBack controller;
    private String msg="";
    public LogoutController(Activity activity,IControllerCallBack controller) {
        this.mActivity = activity;
        this.controller = controller;
    }

    public void loginout() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter = new HashMap();
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        LogUtil.d("Request", parameter.toString());
        OKManager.getInstance().sendComplexForm(NetContants.LOGINOUT, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                LogUtil.d("Logout", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                     msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
//                        IApplication.getInstance().clearUserInfo(mActivity);
//                        IApplication.isToHome = true;
//                        mActivity.finish();
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    }else {
                    }
                } catch (JSONException e) {
                    ToastAlone.showLongToast(mActivity,ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastAlone.showLongToast(mActivity,ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

 
}
