package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.PerSonalBean;
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
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 36420 on 2016/08/24.
 */
public class PersonalDataSavaController {
    private Activity activity;
    private Map parameter;
    private ControllerCallBack mIController;
    private String msg = "";

    public PersonalDataSavaController(Activity activity, ControllerCallBack iController) {
        this.activity = activity;
        this.mIController = iController;
        parameter = new HashMap<>();
    }

    public void PersonalDataSava(PerSonalBean perSonalBean) {
        if (!CommonUtils.isNetAvailable()) return;
        CommonUtils.showDialog(activity, "正在努力加载....");
        parameter.put("username", perSonalBean.getUsername());
        parameter.put("idcard", perSonalBean.getIdcard());
        parameter.put("education", "6402");
        parameter.put("marriage", perSonalBean.getMarriage());
        parameter.put("live_status", perSonalBean.getLive_status());

        parameter.put("huji_adr", perSonalBean.getHuji_adr());
        parameter.put("huji_adr_detail", perSonalBean.getHuji_adr_detail());
        parameter.put("live_adr", perSonalBean.getLive_adr());
        parameter.put("live_adr_detail", perSonalBean.getLive_adr_detail());
        parameter.put("bank_code", perSonalBean.getBank_code());
        parameter.put("bankcard_no", perSonalBean.getBankcard_no());
        parameter.put("bank_phone", perSonalBean.getBank_phone());

        parameter.put("idcard_z", perSonalBean.getIdCardZId());
        parameter.put("idcard_f", perSonalBean.getIdCardFId());
        parameter.put("idcard_hand", perSonalBean.getIdCardHandId());
        parameter.put("product_id", SharedPreferencesUtil.getInstance(IApplication.gainContext()).getString(SPKey.PRODUCT_ID));
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("unit_adr", perSonalBean.getUnit_adr());
        parameter.put("unit_adr_detail", perSonalBean.getUnit_adr_detail());
        parameter.put("product_id", perSonalBean.getProduct_id());
        Log.d("parameter", parameter.toString().trim());

        OKManager.getInstance().sendComplexForm(NetContants.saveuserdata, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String resopnse = result.toString().trim();
                Log.d("PersonalDataSava", resopnse);
                try {
                    JSONObject jsonObject = new JSONObject(resopnse);
                    String flag = jsonObject.optString("flag");
                    msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        success(CallBackType.SAVA_PERSONAL_INFOR, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(activity);
                    } else {
                        fail(CallBackType.SAVA_PERSONAL_INFOR, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.SAVA_PERSONAL_INFOR, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(CallBackType.SAVA_PERSONAL_INFOR, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    private void fail(int returnCode, String errorMessage) {
        if (mIController != null) {
            mIController.onFail(returnCode, errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (mIController != null) {
            mIController.onSuccess(returnCode, value);
        }
    }
}
