package com.two.emergencylending.controller;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToolImage;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2016/8/23.
 */
public class UploadController {
    public static final int idcardZ = 1;
    public static final int idcardF = 2;
    public static final int bankcardZ = 3;
    public static final int bankcardF = 4;
    public static final int selfhand = 5;
    private static final String Tag = UploadController.class.getSimpleName();
    private Activity mActivity;
    private Map<String, String> parameter;
    private Controller mIController;
    private String msg;
//    private int picType;
//
//    public int getPicType() {
//        return picType;
//    }
//
//    public void setPicType(int picType) {
//        this.picType = picType;
//    }

    public UploadController(Activity activity, Controller iController) {
        this.mActivity = activity;
        this.mIController = iController;
        parameter = new HashMap<>();
    }

    public void upload(final String file, final int type) {
        if (!CommonUtils.isNetAvailable()) return;
        String data = ToolImage.fileBase64(file);
//        this.picType=type;
        final String fileName = file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf("."));
        String fileExtName = file.substring(file.lastIndexOf(".") + 1, file.length());
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("fileName", fileName);
        parameter.put("fileExtName", fileExtName);
        parameter.put("fileContext", data);
        Log.d("UploadAvatar", "file-path:" + file);
        Log.d("UploadAvatar", "file-Base64:" + data);
        Log.d("fileSize",String.valueOf(file.length()));
        Log.d("UploadController", new Gson().toJson(parameter));
        CommonUtils.showDialog(mActivity, "正在努力加载.....");
        OKManager.getInstance().sendComplexFormNo(NetContants.upload, parameter, new OKManager.Func1() {

            @Override
            public void onResponse(String result) {
                Log.e("wy","onResponse");
                String response = result.toString().trim();
                Log.d("UploadSFResponse", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    msg = jsonObject.optString("msg");
                    Log.d("response", response);
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        String jsonObject1 = jsonObject.optString("result");
                        if (jsonObject.equals("")) {
                            return;
                        }
//                        String decodeString = Des3.decode(jsonObject1);
//                        LogUtil.d("decodeString", decodeString);
                        JSONObject t = new JSONObject(jsonObject1);
                        String fileid = t.optString("fileId");
                        switch (type) {
                            case idcardZ:
                                UserInfoManager.getInstance().setIDcardZ(fileid);
                                UserInfoManager.getInstance().setIdCardZId(fileid);
                                break;
                            case idcardF:
                                UserInfoManager.getInstance().setIDcardF(fileid);
                                UserInfoManager.getInstance().setIdCardFId(fileid);
                                break;
                            case bankcardZ:
                                UserInfoManager.getInstance().setBankCardZ(fileid);
                                UserInfoManager.getInstance().setBankCardZId(fileid);
                                break;
                            case bankcardF:
                                UserInfoManager.getInstance().setBankCardF(fileid);
                                UserInfoManager.getInstance().setBankCardFId(fileid);
                                break;
                            case selfhand:
                                UserInfoManager.getInstance().setIdCardHandId(fileid);
                                UserInfoManager.getInstance().setSelfHand(fileid);
                                break;
                        }
                        success(type, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(type, msg);
                    }
                } catch (Exception e) {
                    fail(type, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(type, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    private void fail(int type, String errorMessage) {
        Log.e("wy","fail");
        if (mIController != null) {
            mIController.onFail(type, errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        Log.e("wy","success");
        if (mIController != null) {
            mIController.onSuccess(returnCode, value);
        }
    }

    public interface Controller {

        void onSuccess(int returnCode, String value);

        void onFail(int type, String errorMessage);
    }

}
