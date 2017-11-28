package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.ContactListModel;
import com.two.emergencylending.bean.PresentStore;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2016/8/29.
 */
public class GetUserContactList {
    private Activity mActivity;
    private ControllerCallBack iController;
    private Map parameter;
    private String msg;

    public GetUserContactList(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.iController = controller;
        parameter = new HashMap();
    }

    public void getContactList() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        Log.d("Request", parameter.toString().trim());
        CommonUtils.showDialog(mActivity,"加载中");
        OKManager.getInstance().sendComplexForm(NetContants.getusercontactlist, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                Log.d("GetUserContactList", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    String msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        String object = jsonObject.optString("result");
                        String count = jsonObject.optString("count");
//                        String decodeValue = Des3.decode(object);
//                        LogUtil.d("GetUserContactListDes3", decodeValue);
                        JSONObject jsonObject1 = new JSONObject(object);
                          List<PresentStore> stores =  new Gson().fromJson(jsonObject1.optString("storeList"),
                                  new TypeToken<List<PresentStore>>() {
                                  }.getType());
                        UserInfoManager.getInstance().setStores(stores);
                        String contact=jsonObject1.optString("contactList");
                        if (object.indexOf("contactList") != -1){
                            List<ContactListModel> contactList = new Gson().fromJson(contact,
                                    new TypeToken<List<ContactListModel>>() {
                                    }.getType());
                            UserInfoManager.getInstance().setContactListModels(contactList);
                        }
                        success(CallBackType.GET_CONTACT_LIST, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                        return;
                    } else {
                        fail(CallBackType.GET_CONTACT_LIST,msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.GET_CONTACT_LIST,ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.GET_CONTACT_LIST,ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    private void fail(int returnCode,String errorMessage) {
        if (iController != null) {
            iController.onFail( returnCode,errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (iController != null) {
            iController.onSuccess(returnCode, value);
        }
    }
}
