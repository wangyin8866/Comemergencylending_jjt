package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

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
import com.two.emergencylending.utils.ToastAlone;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：客户确认
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */

public class CustomerConfirmController {
    private static final String TAG = CustomerConfirmController.class.getSimpleName();
    private Activity mActivity;
    private Map<String, String> parameter;
    ControllerCallBack mController;

    public CustomerConfirmController(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mController = controller;
        parameter = new HashMap<>();
    }

    public void getConfirmInfo() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d(TAG, parameter.toString());
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.CUSTOMER_CONFIRM_GET, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                Log.d(TAG, result.toString());
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            String res = Des3.decode(json.getString("result"));
                            success(CallBackType.CUSTOMER_CONFIRM_QUERY, res);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.CUSTOMER_CONFIRM_QUERY, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.CUSTOMER_CONFIRM_QUERY, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.CUSTOMER_CONFIRM_QUERY, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.CUSTOMER_CONFIRM_QUERY, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }


    public void claim() {
        if (!CommonUtils.isNetAvailable()) {
            ToastAlone.showLongToast(mActivity, "网络异常，请重试！");
            return;
        }
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d(TAG, parameter.toString());
        CommonUtils.showDialog(mActivity, "加载中...");
        try {
            OKManager.getInstance().sendComplexForm(NetContants.CUSTOMER_CONFIRM_CLAIM, parameter, new OKManager.Func1() {
                @Override
                public void onResponse(String result) {
                    CommonUtils.closeDialog();
                    Log.d(TAG, result.toString());
                    String response = result.toString().trim();
                    if (!StringUtil.isNullOrEmpty(result)) {
                        LogUtil.d(TAG, response);
                        try {
                            JSONObject json = new JSONObject(response);
                            String code = json.getString("flag");
                            String msg = json.getString("msg");
                            if (code.equals(ErrorCode.SUCCESS)) {
                                String res = Des3.decode(json.getString("result"));
                                success(CallBackType.CUSTOMER_CONFIRM_CLAIM, res);
                            } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                                IApplication.getInstance().backToLogin(mActivity);
                            } else {
                                LogUtil.d(TAG, msg);
                                fail(CallBackType.CUSTOMER_CONFIRM_CLAIM, msg);
                            }
                        } catch (Exception e) {
                            LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                            WyController.uploadAppLog(mActivity, "签约异常", NetContants.CUSTOMER_CONFIRM_GET, e.getMessage());
                            fail(CallBackType.CUSTOMER_CONFIRM_CLAIM, ErrorCode.FAIL_DATA);
                            e.printStackTrace();
                        }
                    } else {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.CUSTOMER_CONFIRM_CLAIM, ErrorCode.FAIL_DATA);
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    CommonUtils.closeDialog();
                    LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                    fail(CallBackType.CUSTOMER_CONFIRM_CLAIM, ErrorCode.FAIL_NETWORK);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            ToastAlone.showLongToast(mActivity, "签约异常，请重试！");
            WyController.uploadAppLog(mActivity, "签约异常", NetContants.CUSTOMER_CONFIRM_GET, e.getMessage());
            e.printStackTrace();
        }
    }

    public void refuse() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d(TAG, parameter.toString());
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.CUSTOMER_CONFIRM_REFUSE, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                Log.d(TAG, result.toString());
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
//                            String res = Des3.decode(json.getString("result"));
                            success(CallBackType.CUSTOMER_CONFIRM_REFUSE, "");
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.CUSTOMER_CONFIRM_REFUSE, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.CUSTOMER_CONFIRM_REFUSE, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.CUSTOMER_CONFIRM_REFUSE, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.CUSTOMER_CONFIRM_REFUSE, ErrorCode.FAIL_NETWORK);
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
