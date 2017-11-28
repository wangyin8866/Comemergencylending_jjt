package com.two.emergencylending.controller;

import android.app.Activity;
import android.content.Intent;

import com.igexin.sdk.PushManager;
import com.two.emergencylending.activity.GestureLockModule.SetGuestActivity;
import com.two.emergencylending.activity.MainActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.Des3;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CommonalityFieldUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.PhoneInfoUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToolMetaData;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：注册接口
 * 创建人：szx
 * 创建时间：2016/7/6 11:06
 * 修改人：szx
 * 修改时间：2016/7/6 11:06
 * 修改备注：
 */
public class RegisterController {
    private static final String TAG = RegisterController.class.getSimpleName();
    private Map parameter;
    private Activity mActivity;
    ControllerCallBack mIController;
    private String mIdCard, mPhone, mVerify_code, mPassword, mRecommend_code;

    public RegisterController(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mIController = controller;
        parameter = new HashMap();
    }

    public void register(final Boolean isHome, String mobile, String verify_code, String password, String recommend_code) {
        if (!CommonUtils.isNetAvailable()) return;
        this.mPhone = mobile;
        this.mVerify_code = verify_code;
        this.mPassword = password;
        this.mRecommend_code = recommend_code;
        parameter.clear();
        String brand = PhoneInfoUtils.getPhoneBrand();
        String model = PhoneInfoUtils.getPhoneModel();
        String version = PhoneInfoUtils.getSystemVersion();
        parameter.put("phone", this.mPhone);
        String clientid = PushManager.getInstance().getClientid(mActivity);
        if (StringUtil.isNullOrEmpty(clientid)) {
            PushManager.getInstance().initialize(IApplication.globleContext);
            clientid = PushManager.getInstance().getClientid(IApplication.globleContext);
        }
        parameter.put("clientid", clientid);
        parameter.put("chan_code", ToolMetaData.getMetaData(mActivity).getString("UMENG_CHANNEL"));
        parameter.put("act_code", "0");
        parameter.put("verify_code", this.mVerify_code);
        parameter.put("password", this.mPassword);
        parameter.put("recommend_code", this.mRecommend_code);
        parameter.put("register_platform",  CommonalityFieldUtils.getDittchStr());
        parameter.put("register_ip", CommonUtils.getIp(mActivity));
        parameter.put("register_device_no", brand + "," + model + "," + version);
        parameter.put("longitude", IApplication.longitude);
        parameter.put("latitude", IApplication.latitude);
        LogUtil.d(TAG, parameter.toString());
        OKManager.getInstance().sendComplexForm(NetContants.REGISTER, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                LogUtil.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    String msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
//                        ToastAlone.showLongToast(mActivity, msg);
                        String object = jsonObject.optString("result");
                        String decodeValue = Des3.decode(object);
                        JSONObject t = new JSONObject(decodeValue);
                        String juid = t.optString("juid");
                        String login_token = t.optString("login_token");
                        String recommendCode = t.optString("recommendCode");
                        String is_clerk = t.optString("is_clerk");
                        String renew_loans = t.optString("renew_loans");
                        LogUtil.i("recommendCode:", recommendCode);
                        UserInfoManager.getInstance().setJuid(juid);
                        UserInfoManager.getInstance().setLogin_token(login_token);
                        IApplication.getInstance().clearInfoCache(mActivity);//清除资料缓存
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.IS_CLERK, is_clerk);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.SHARE_CODE, recommendCode);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.RENEW_LOANS, renew_loans);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.USERNAME, RegisterController.this.mPhone);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.PASSWORD, RegisterController.this.mPassword);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.JUID, juid);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.LOGIN_TOKEN, login_token);
                        IApplication.isLoginOut = false;
                        if (isHome) {
                            IApplication.getInstance().isRefresh = true;
                            success(CallBackType.REGISTER, "");
                        } else {
                            CommonUtils.goToActivity(mActivity, MainActivity.class);
//                            CommonUtils.goToActivity(mActivity, SetGuestActivity.class, new Intent().putExtra("skipt", 2));
                            mActivity.finish();
                        }
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.REGISTER, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    fail(CallBackType.REGISTER, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(CallBackType.REGISTER, ErrorCode.FAIL_NETWORK);
            }
        });
    }


    public void registerRenew(final Boolean isHome, String mobile, String idcard, String verify_code, String password) {
        if (!CommonUtils.isNetAvailable()) return;
        this.mIdCard = idcard;
        this.mPhone = mobile;
        this.mVerify_code = verify_code;
        this.mPassword = password;
        parameter.clear();
        String brand = PhoneInfoUtils.getPhoneBrand();
        String model = PhoneInfoUtils.getPhoneModel();
        String version = PhoneInfoUtils.getSystemVersion();
        parameter.put("phone", this.mPhone);
        parameter.put("idcard", this.mIdCard);
        String clientid = PushManager.getInstance().getClientid(mActivity);
        if (StringUtil.isNullOrEmpty(clientid)) {
            PushManager.getInstance().initialize(IApplication.globleContext);
            clientid = PushManager.getInstance().getClientid(IApplication.globleContext);
        }
        parameter.put("clientid", clientid);
        parameter.put("chan_code", ToolMetaData.getMetaData(mActivity).getString("UMENG_CHANNEL"));
        parameter.put("act_code", "0");
        parameter.put("verify_code", this.mVerify_code);
        parameter.put("password", this.mPassword);
        parameter.put("register_platform",  CommonalityFieldUtils.getDittchStr());
        parameter.put("register_ip", CommonUtils.getIp(mActivity));
        parameter.put("register_device_no", brand + "," + model + "," + version);
        parameter.put("longitude", IApplication.longitude);
        parameter.put("latitude", IApplication.latitude);
        LogUtil.d(TAG, parameter.toString());
        OKManager.getInstance().sendComplexForm(NetContants.REGISTER_RENEW, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                LogUtil.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    String msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
//                        ToastAlone.showLongToast(mActivity, msg);
                        String object = jsonObject.optString("result");
                        String decodeValue = Des3.decode(object);
                        JSONObject t = new JSONObject(decodeValue);
                        String juid = t.optString("juid");
                        String login_token = t.optString("login_token");
                        String recommendCode = t.optString("recommendCode");
                        String is_clerk = t.optString("is_clerk");
                        LogUtil.i("recommendCode:", recommendCode);
                        UserInfoManager.getInstance().setJuid(juid);
                        UserInfoManager.getInstance().setLogin_token(login_token);
                        IApplication.getInstance().clearInfoCache(mActivity);//清除资料缓存
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.IS_CLERK, is_clerk);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.SHARE_CODE, recommendCode);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.USERNAME, RegisterController.this.mPhone);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.PASSWORD, RegisterController.this.mPassword);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.JUID, juid);
                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.LOGIN_TOKEN, login_token);
                        IApplication.isLoginOut = false;
                        if (isHome) {
                            IApplication.getInstance().isRefresh = true;
                            success(CallBackType.REGISTER, "");
                        } else {
                            CommonUtils.goToActivity(mActivity, SetGuestActivity.class, new Intent().putExtra("skipt", 2));
                            mActivity.finish();
                        }
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.REGISTER, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    fail(CallBackType.REGISTER, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(CallBackType.REGISTER, ErrorCode.FAIL_NETWORK);
            }
        });
    }

    public void success(int returnCode, String value) {
        if (mIController != null) {
            mIController.onSuccess(returnCode, value);
        }
    }

    public void fail(int returnCode, String errorMessage) {
        if (mIController != null) {
            mIController.onFail(returnCode, errorMessage);
        }
    }

}
