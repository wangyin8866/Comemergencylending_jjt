package com.two.emergencylending.controller;

import android.app.Activity;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.Des3;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：是否可以借款接口
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class BorrowAbleCotroller {
    private static final String TAG = BorrowAbleCotroller.class.getSimpleName();
    Activity mActivity;
    Map<String, String> parameter;
    ControllerCallBack mController;

    public BorrowAbleCotroller(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mController = controller;
        parameter = new HashMap<>();
    }


    public void borrowAble() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        OKManager.getInstance().sendComplexForm(NetContants.BORROWABLE, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            String res = Des3.decode(json.getString("result"));
                            success(CallBackType.BORRABLE, res);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            String msg = json.getString("msg");
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.BORRABLE, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.BORRABLE, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.BORRABLE, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.BORRABLE, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void success(int returnCode, String value) {
        if (mController != null) {
            mController.onSuccess(returnCode, value);
        }
    }

    public void fail(int returnCode, String errorMessage) {
        if (mController != null) {
            mController.onFail(returnCode, errorMessage);
        }
    }

}
