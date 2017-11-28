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

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by User on 2016/9/29.
 * 获取认证状态接口
 */
public class GetCredentialState {
    private Activity mActivity;
    private Map<String, String> parameter;
    ControllerCallBack mController;

    public GetCredentialState(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mController = controller;
        parameter = new HashMap<>();
    }

    public void getState() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        OKManager.getInstance().sendComplexForm(NetContants.GETAUTHSTATUS, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                try {
                    if (!StringUtil.isNullOrEmpty(response)) {
                        LogUtil.d(TAG, response);
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            String res = "";
                            if (json.has("result")) {
                                res = Des3.decode(json.getString("result"));
                            }
                            success(CallBackType.CREDENTIAL_STATE, res);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.CREDENTIAL_STATE, msg);
                        }
                    } else {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.CREDENTIAL_STATE, ErrorCode.FAIL_DATA);
                    }
                } catch (Exception e) {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.CREDENTIAL_STATE, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                fail(CallBackType.CREDENTIAL_STATE, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void saveFaceState() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("user_face_status", "1");
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.SAVE_FACE_TATUS, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                try {
                    if (!StringUtil.isNullOrEmpty(response)) {
                        LogUtil.d(TAG, response);
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
//                            String res = "";
//                            if (json.has("result")) {
//                                res = Des3.decode(json.getString("result"));
//                            }
                            success(CallBackType.CREDENTIAL_FACE_STATE, "");
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.CREDENTIAL_FACE_STATE, msg);
                        }
                    } else {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.CREDENTIAL_FACE_STATE, ErrorCode.FAIL_DATA);
                    }
                } catch (Exception e) {
                    CommonUtils.closeDialog();
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.CREDENTIAL_FACE_STATE, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                fail(CallBackType.CREDENTIAL_STATE, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    /**
     * 芝麻信用授权
     */
    public void ZhiMaAuthorization() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "加载中...");
        try {
            OKManager.getInstance().sendComplexForm(NetContants.ZHI_MA_AUTHORIZATION, parameter, new OKManager.Func1() {
                @Override
                public void onResponse(String result) {
                    CommonUtils.closeDialog();
                    String response = result.toString().trim();
                    try {
                        if (!StringUtil.isNullOrEmpty(response)) {
                            LogUtil.d(TAG, response);
                            JSONObject json = new JSONObject(response);
                            json = json.getJSONObject("result");
                            String data = json.optString("data");

                            success(CallBackType.CREDENTIAL_ZHIMA_AUTHORIZATION, data);
                        } else {
                            LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                            fail(CallBackType.CREDENTIAL_ZHIMA_AUTHORIZATION, ErrorCode.FAIL_DATA);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        WyController.uploadAppLog(mActivity, "芝麻信用授权", NetContants.ZHI_MA_AUTHORIZATION, e.getMessage());
                        fail(CallBackType.CREDENTIAL_ZHIMA_AUTHORIZATION, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    CommonUtils.closeDialog();
                    LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                    fail(CallBackType.CREDENTIAL_ZHIMA_AUTHORIZATION, ErrorCode.FAIL_NETWORK);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            WyController.uploadAppLog(mActivity, "芝麻信用授权", NetContants.ZHI_MA_AUTHORIZATION, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取信用积分
     */
    public void getZhiMaData(String state, String applyId) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("state", state);
        parameter.put("applyId", applyId);
        if (!mActivity.isFinishing()) {
            CommonUtils.showDialog(mActivity, "加载中...");
        }
        try {
            OKManager.getInstance().sendComplexForm(NetContants.ZHI_MA_INTEGRAL, parameter, new OKManager.Func1() {
                @Override
                public void onResponse(String result) {
                    CommonUtils.closeDialog();
                    String response = result.toString().trim();
                    try {
                        if (!StringUtil.isNullOrEmpty(response)) {
                            JSONObject json = new JSONObject(response);
                            String code = json.getString("flag");
                            String msg = json.getString("msg");
                            if (code.equals(ErrorCode.SUCCESS)) {
                                success(CallBackType.CREDENTIAL_ZHIMA_INTEGRAL, msg);
                            } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                                IApplication.getInstance().backToLogin(mActivity);
                            } else {
                                fail(CallBackType.CREDENTIAL_ZHIMA_INTEGRAL, msg);
                            }
                        } else {
                            fail(CallBackType.CREDENTIAL_ZHIMA_INTEGRAL, ErrorCode.FAIL_DATA);
                        }
                    } catch (Exception e) {
                        CommonUtils.closeDialog();
                        WyController.uploadAppLog(mActivity, "芝麻积分获取", NetContants.ZHI_MA_INTEGRAL, e.getMessage());
                        fail(CallBackType.CREDENTIAL_ZHIMA_INTEGRAL, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    CommonUtils.closeDialog();
                    LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                    fail(CallBackType.CREDENTIAL_ZHIMA_INTEGRAL, ErrorCode.FAIL_NETWORK);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            WyController.uploadAppLog(mActivity, "芝麻积分获取", NetContants.ZHI_MA_INTEGRAL, e.getMessage());
            e.printStackTrace();
        }
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
