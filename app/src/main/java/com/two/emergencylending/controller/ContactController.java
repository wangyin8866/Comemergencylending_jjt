package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.BorrowInfoBean;
import com.two.emergencylending.bean.ContactBean;
import com.two.emergencylending.bean.ContactModel;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToastAlone;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by User on 2016/8/26.
 */
public class ContactController {
    private Activity mActivity;
    private Map parameter;
    private int fill_location;
    private ControllerCallBack callback;
    private String msg;
    private ContactBean contactBean;

    public ContactController(Activity activity, ControllerCallBack callback) {
        this.mActivity = activity;
        this.callback = callback;
        parameter = new HashMap<>();
    }

    //保存验证联系人状态
    public void checkContactsSave() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("is_verified_contract", "1");
        Log.d("Requset", parameter.toString().trim());
        if (PersonalDataActivity.isShowAuthor)
            CommonUtils.showDialog(mActivity, "正在努力加载.....");
        OKManager.getInstance().sendComplexForm(NetContants.CHECK_CONTACTS_SAVE, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                Log.d("response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    msg = jsonObject.optString("msg");
                    String flag = jsonObject.optString("flag");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        success(CallBackType.CHECK_CONTACTS_SAVE, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.CHECK_CONTACTS_SAVE, msg);
                    }
                } catch (JSONException e) {
                    fail(CallBackType.CHECK_CONTACTS_SAVE, ErrorCode.FAIL_DATA);
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.CHECK_CONTACTS_SAVE, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void savaContact(List<ContactModel> contactModels, BorrowInfoBean borrowInfoBean, String offline_calp_msg, String org_no, String org_name, String sall_emp_no) {
        if (!CommonUtils.isNetAvailable()) return;
        String appleinfo = SharedPreferencesUtil.getInstance(IApplication.gainContext()).getString(SPKey.APPLY_INFO);
        String renew_loan_type = SharedPreferencesUtil.getInstance(IApplication.gainContext()).getString(SPKey.IS_RENEW_LOANS);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("offline_calp_msg", offline_calp_msg);
        parameter.put("contactList", new Gson().toJson(contactModels));
        parameter.put("renew_loan_type", renew_loan_type);
        parameter.put("borrow_use", "525");

        parameter.put("sales_name", "");
        parameter.put("sales_no", sall_emp_no);
        parameter.put("store", org_no);
        parameter.put("store_name", org_name);

        if (PersonalDataActivity.isShowAuthor) {
            parameter.put("route", "1");
            parameter.put("product_id", borrowInfoBean.getProduct_id());
            parameter.put("borrow_limit", borrowInfoBean.getBorrow_limit());
            parameter.put("borrow_periods", borrowInfoBean.getBorrow_periods());
        } else {
            parameter.put("route", "0");
            parameter.put("product_id", UserInfoManager.getInstance().getPerSonalBean().getProduct_id());
            parameter.put("borrow_limit", "2000");
            parameter.put("borrow_periods", "10");

        }
        Log.d("Requset", parameter.toString().trim());
        if (PersonalDataActivity.isShowAuthor)
            CommonUtils.showDialog(mActivity, "正在努力加载.....");
        try {
            OKManager.getInstance().sendComplexForm(NetContants.saveusercontact, parameter, new OKManager.Func1() {
                @Override
                public void onResponse(String result) {
                    CommonUtils.closeDialog();
                    String response = result.toString().trim();
                    Log.d("response", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        msg = jsonObject.optString("msg");
                        String flag = jsonObject.optString("flag");
                        if (flag.equals(ErrorCode.SUCCESS)) {
                            if (!PersonalDataActivity.isShowAuthor) {
                                ToastAlone.showLongToast(mActivity, "资料保存成功!");
                            }
                            success(CallBackType.SAVA_CONTACT, msg);
                        } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            fail(CallBackType.SAVA_CONTACT, msg);
                        }
                    } catch (Exception e) {
                        fail(CallBackType.SAVA_CONTACT, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    if (!mActivity.isFinishing()) {
                        CommonUtils.closeDialog();
                    }
                    fail(CallBackType.SAVA_CONTACT, ErrorCode.FAIL_NETWORK);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            WyController.uploadAppLog(mActivity, "保存联系人", NetContants.saveusercontact, e.getMessage());
            e.printStackTrace();
        }
    }

    private void fail(int returnCode, String errorMessage) {
        if (callback != null) {
            callback.onFail(CallBackType.SAVA_CONTACT, errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (callback != null) {
            callback.onSuccess(returnCode, value);
        }
    }
}
