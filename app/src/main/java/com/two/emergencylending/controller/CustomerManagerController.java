package com.two.emergencylending.controller;

import android.app.Activity;
import android.content.Intent;

import com.two.emergencylending.activity.ApplyCheckFailActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.Des3;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CommonalityFieldUtils;
import com.two.emergencylending.utils.DateUtil;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.zyjr.emergencylending.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：客户经理接口
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class CustomerManagerController {
    private String TAG = CustomerManagerController.class.getSimpleName();
    private Activity mActivity;
    private ControllerCallBack mController;
    private Map parameter;

    public CustomerManagerController(Activity mActivity, ControllerCallBack iController) {
        this.mActivity = mActivity;
        this.mController = iController;
        parameter = new HashMap();
    }

    public void getCustomeerID(String phone, String verifyCode) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        CommonUtils.showDialog(mActivity, "加载中...");
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("cust_phone", phone);
        parameter.put("verify_code", verifyCode);
        parameter.put("register_platform", CommonalityFieldUtils.getDittchStr());
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.GET_CUSTOMER_ID, parameter, new OKManager.Func1() {
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
                        success(CallBackType.GET_CUSTOMER_ID, res);
                    } else if (code.equals(ErrorCode.IS_APPLYING)) {
                        Intent intent = new Intent();
                        intent.putExtra("msg",jsonObject.optString("msg"));
                        CommonUtils.goToActivity(mActivity, ApplyCheckFailActivity.class,intent);
                        mActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(CallBackType.GET_CUSTOMER_ID, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.GET_CUSTOMER_ID, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.GET_CUSTOMER_ID, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void queryCustomerRecord(String phone, String username, String borrow_status, int day) {
        String today = DateUtil.today();
        String startDay = DateUtil.startdayByToday(day);
        queryCustomerRecord(phone, username, borrow_status, startDay, today);
    }

    public void queryCustomerRecord(String phone, String username, String borrow_status, int day, boolean showProgress) {
        String today = DateUtil.today();
        String startDay = DateUtil.startdayByToday(day);
        queryCustomerRecord(phone, username, borrow_status, startDay, today, showProgress);
    }

    public void queryCustomerRecord(String phone, String username, String borrow_status, String borrow_start_date, String borrow_end_date) {
        queryCustomerRecord(phone, username, borrow_status, borrow_start_date, borrow_end_date, false);
    }

    public void queryCustomerRecord(String phone, String username, String borrow_status, String borrow_start_date, String borrow_end_date, boolean showProgress) {
        if (!CommonUtils.isNetAvailable()) return;
        if (showProgress) {
            CommonUtils.showDialog(mActivity, "加载中...");
        }
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        if (!StringUtil.isNullOrEmpty(phone)) {
            parameter.put("phone", phone);
        }
        if (!StringUtil.isNullOrEmpty(username)) {
            parameter.put("username", username);
        }
        parameter.put("borrow_status", borrow_status);
        parameter.put("borrow_start_date", borrow_start_date);
        parameter.put("borrow_end_date", borrow_end_date);
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.QUERY_CUSTOMER_RECORD, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                LogUtil.d(TAG, response);
                CommonUtils.closeDialog();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("flag");
                    if (code.equals(ErrorCode.SUCCESS)) {
                        String res = Des3.decode(jsonObject.getString("result"));
                        LogUtil.d(TAG, res);
                        success(CallBackType.QUERY_CUSTOMER_RECORD, res);
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(CallBackType.QUERY_CUSTOMER_RECORD, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.QUERY_CUSTOMER_RECORD, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.QUERY_CUSTOMER_RECORD, ErrorCode.FAIL_NETWORK);
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
