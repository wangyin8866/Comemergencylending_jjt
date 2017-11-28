package com.two.emergencylending.controller;

import android.app.Activity;
import android.view.View;

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
 * 类描述：首页借款状态
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class BorrowStatusCotroller {
    private static final String TAG = BorrowStatusCotroller.class.getSimpleName();
    Activity mActivity;
    Map<String, String> parameter;
    ControllerCallBack mController;

    public BorrowStatusCotroller(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mController = controller;
        parameter = new HashMap<>();
    }

    public void getBorrowStatusBack() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        OKManager.getInstance().sendComplexForm(NetContants.BORROW_STATUS, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            String res = Des3.decode(json.getString("result").toString());
                            success(CallBackType.BORROW_STATUS_BACK, res);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        } else {
                            String msg = json.getString("msg");
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.BORROW_STATUS_BACK, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.BORROW_STATUS_BACK, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.BORROW_STATUS_BACK, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.BORROW_STATUS_BACK, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void getBorrowStatus(final View boutn) {
        if (!CommonUtils.isNetAvailable()) {
            boutn.setEnabled(true);
            return;
        }
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        LogUtil.i(TAG, "getBorrowStatus juid:" + SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID) + "\nlogin_token:" + SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        OKManager.getInstance().sendComplexForm(NetContants.BORROW_STATUS, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                boutn.setEnabled(true);
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        LogUtil.i(TAG, "json:" + json.toString());
                        if (code.equals(ErrorCode.SUCCESS)) {
                            String res = Des3.decode(json.getString("result"));
                            success(CallBackType.BORROW_STATUS, res);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.BORROW_STATUS, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.BORROW_STATUS, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.BORROW_STATUS, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                boutn.setEnabled(true);
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.BORROW_STATUS, ErrorCode.FAIL_NETWORK);
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
