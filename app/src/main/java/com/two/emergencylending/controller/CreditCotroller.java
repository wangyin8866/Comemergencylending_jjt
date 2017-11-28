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
import com.two.emergencylending.utils.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 项目名称：急借通
 * 类描述：提额增信控制器
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class CreditCotroller {
    private Activity mActivity;
    private Map<String, String> parameter;
    ControllerCallBack mController;

    public CreditCotroller(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mController = controller;
        parameter = new HashMap<>();
    }

    public void saveCredit(String selectCheckbox) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("selectcheckbox", selectCheckbox);
        OKManager.getInstance().sendComplexForm(NetContants.SAVE_CREDIT, parameter, new OKManager.Func1() {
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
                            success(CallBackType.SAVE_CREDIT, "");
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.SAVE_CREDIT, msg);
                        }
                    } else {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.SAVE_CREDIT, ErrorCode.FAIL_DATA);
                    }
                } catch (Exception e) {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.SAVE_CREDIT, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                fail(CallBackType.SAVE_CREDIT, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void getCredit() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        OKManager.getInstance().sendComplexForm(NetContants.GET_CREDIT, parameter, new OKManager.Func1() {
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
                            String des3Decode =json.getString("result");
//                            String res = Des3.decode(des3Decode);
                            JSONObject jsonObject = new JSONObject(des3Decode);
                            success(CallBackType.GET_CREDIT, jsonObject.get("selectcheckbox").toString());
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.GET_CREDIT, msg);
                        }
                    } else {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.GET_CREDIT, ErrorCode.FAIL_DATA);
                    }
                } catch (Exception e) {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.GET_CREDIT, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                fail(CallBackType.GET_CREDIT, ErrorCode.FAIL_NETWORK);
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
