package com.two.emergencylending.controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.CallBackType;
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
 * Created by User on 2016/8/29.
 */
public class UploadAvatar {
    private Activity mActivity;
    private Map parameter;
    private IControllerCallBack mIController;
    private String msg;

    public UploadAvatar(Activity Activity, IControllerCallBack iController) {
        this.mActivity = Activity;
        this.mIController = iController;
        parameter = new HashMap();
    }

    public void uploadhead(String file, Bitmap mBitmap) {
        if (!CommonUtils.isNetAvailable()) return;
        String data = ToolImage.bitMapBase64(Bitmap.CompressFormat.PNG, 100, mBitmap);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("fileName", file.replace("", ""));
        parameter.put("fileExtName", "png");
        parameter.put("fileContext", data);
        Log.d("UploadAvatar", "file-path:" + file);
        Log.d("UploadAvatar", "file-Base64:" + data);
        Log.d("uploadhead", parameter.toString());
        CommonUtils.showDialog(mActivity, "正在加载中......");
        OKManager.getInstance().sendComplexFormNo(NetContants.uploadhead, parameter, new OKManager.Func1() {

            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                Log.d("UploadAvatarResponse", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                      success(CallBackType.HEAD,msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        CommonUtils.closeDialog();
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                      fail(msg);
                    }
                } catch (Exception e) {
                    CommonUtils.closeDialog();
                    fail(ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(ErrorCode.FAIL_NETWORK);
                e.printStackTrace();

            }
        });
    }

    private void fail(String errorMessage) {
        if (mIController != null) {
            mIController.onFail(errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (mIController != null) {
            mIController.onSuccess(returnCode, value);
        }
    }
}