package com.two.emergencylending.controller;

import android.app.Activity;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.Des3;
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
 * 项目名称：急借通
 * 类描述：是否是业务员
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class ClerkController {
    private String TAG = ClerkController.class.getSimpleName();
    private Activity mActivity;
    private ControllerCallBack mController;
    private Map parameter;

    public ClerkController(Activity mActivity, ControllerCallBack iController) {
        this.mActivity = mActivity;
        this.mController = iController;
        parameter = new HashMap();
    }

    public void getClerkInfo() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        CommonUtils.showDialog(mActivity, "加载中...");
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.CLERK_INFO, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                LogUtil.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("flag");

                    if (code.equals(ErrorCode.SUCCESS)) {
                        String res = Des3.decode(jsonObject.getString("result"));
                        LogUtil.d(TAG, res);
                        success(CallBackType.CLERK_INFO, res);
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(CallBackType.CLERK_INFO, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.CLERK_INFO, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.CLERK_INFO, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }


    public void getClerkDirect(String pageNo, String pageSize) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        CommonUtils.showDialog(mActivity, "加载中...");
        parameter.put("pageNo", pageNo);
        parameter.put("pageSize", pageSize);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.CLERK_DIRECT, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                CommonUtils.closeDialog();
                LogUtil.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("flag");
                    if (code.equals(ErrorCode.SUCCESS)) {
                        String res = Des3.decode(jsonObject.getString("result"));
                        LogUtil.d(TAG, res);
                        success(CallBackType.CLERK_DIRECT, res);
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(CallBackType.CLERK_DIRECT, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.CLERK_DIRECT, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.CLERK_DIRECT, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }


    public void getClerkIndirect(String pageNo, String pageSize) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        CommonUtils.showDialog(mActivity, "加载中...");
        parameter.put("pageNo", pageNo);
        parameter.put("pageSize", pageSize);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.CLERK_INDIRECT, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                CommonUtils.closeDialog();
                LogUtil.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("flag");

                    if (code.equals(ErrorCode.SUCCESS)) {
                        String res = Des3.decode(jsonObject.getString("result"));
                        LogUtil.d(TAG, res);
                        success(CallBackType.CLERK_INDIRECT, res);
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(CallBackType.CLERK_INDIRECT, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.CLERK_INDIRECT, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.CLERK_INDIRECT, ErrorCode.FAIL_NETWORK);
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
