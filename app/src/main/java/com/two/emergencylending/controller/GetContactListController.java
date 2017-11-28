package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.ContactsBean;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 获取联系人列表
 */

public class GetContactListController {
    private Map parameter;
    private Activity mActivity;
    public static boolean isUpload = false;
    ControllerCallBack mController;

    public GetContactListController(Activity activity) {
        this.mActivity = activity;
        parameter = new HashMap();
    }
    public interface CallBack{
        void success();
        void error(String msg);
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void contactList() {
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        List<ContactsBean> list = CommonUtils.queryContactPhoneNumber(IApplication.gainContext());
        if ( list.size() == 0) {
            return;
        }
        parameter.put("contactList", new Gson().toJson(list));
        Log.d("parameter", parameter.toString().trim());
//        OKManager.getInstance().sendComplexForm(NetContants.GET_PHONE_CONTACT_list, parameter, new OKManager.Func1() {
//            @Override
//            public void onResponse(String result) {
//                String respond = result.toString();
//                Log.d("GetContactList", respond);
//
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//        });
        if (PersonalDataActivity.isShowAuthor)
            CommonUtils.showDialog(mActivity, "正在努力加载.....");
        OKManager.getInstance().sendComplexForm(NetContants.GET_PHONE_CONTACT_list, parameter, new OKManager.Func1() {
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
                            success();
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            error( msg);
                        }
                    } else {
                        error(ErrorCode.FAIL_DATA);
                    }
                } catch (Exception e) {
                    error(ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                error(ErrorCode.FAIL_NETWORK);
            }
        });
    }
    public void success() {
        if (callBack != null) {
            callBack.success();
        }
    }
    public void error( String errorMessage) {
        if (callBack != null) {
            callBack.error( errorMessage);
        }
    }
}
