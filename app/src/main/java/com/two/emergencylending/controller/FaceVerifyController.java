package com.two.emergencylending.controller;

import android.app.Activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ThirdPartyKey;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：急借通
 * 类描述：人脸识别认证
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class FaceVerifyController {
    private String TAG = FaceVerifyController.class.getSimpleName();
    private Activity mActivity;
    private ControllerCallBack mController;
    private Map parameter;
    private String errorString = "";

    public FaceVerifyController(Activity mActivity, ControllerCallBack iController) {
        this.mActivity = mActivity;
        this.mController = iController;
        parameter = new HashMap();
    }

    /**
     * 如何调用Verify2.0方法
     * <p>
     */
    public void imageVerify(Map<String, byte[]> images, String delta, String name, String idCard) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("name", name);
        requestParams.put("idcard", idCard);
        try {
            requestParams.put("image_ref1", new FileInputStream(new File(
                    "image_idcard")));// 传入身份证头像照片路径
        } catch (Exception e) {

        }
        requestParams.put("delta", delta);
        requestParams.put("api_key", ThirdPartyKey.FACE_APPKEY);
        requestParams.put("api_secret", ThirdPartyKey.FACE_APPSECRET);

        requestParams.put("comparison_type", 1 + "");
        requestParams.put("face_image_type", "meglive");
        requestParams.put("idcard_name", name);
        requestParams.put("idcard_number", idCard);

        for (Map.Entry<String, byte[]> entry : images.entrySet()) {
            requestParams.put(entry.getKey(),
                    new ByteArrayInputStream(entry.getValue()));
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        final String url = "https://api.megvii.com/faceid/v2/verify";
        try {
            asyncHttpClient.post(url, requestParams,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            CommonUtils.closeDialog();
                            String successStr = new String(bytes);
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(successStr);
                                if (!jsonObject.has("error")) {
                                    // 活体最好的一张照片和公安部系统上身份证上的照片比较
                                    double confidence = jsonObject.getJSONObject(
                                            "result_faceid")
                                            .getDouble("confidence");
                                    JSONObject jsonObject2 = jsonObject
                                            .getJSONObject("result_faceid")
                                            .getJSONObject("thresholds");
                                    double threshold = jsonObject2
                                            .getDouble("1e-3");
                                    double tenThreshold = jsonObject2
                                            .getDouble("1e-4");
                                    double hundredThreshold = jsonObject2
                                            .getDouble("1e-5");
                                    LogUtil.d(TAG, "confidence:" + confidence);
                                    LogUtil.d(TAG, "tenThreshold:" + tenThreshold);
                                    if (confidence > tenThreshold) {
                                        success(CallBackType.FACE_VERIFY, "");
                                    } else {
                                        fail(CallBackType.FACE_VERIFY, "人脸识别未通过,请本人再次尝试");
                                    }
                                } else {
                                    fail(CallBackType.FACE_VERIFY, "人脸识别失败,请重试");
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                WyController.uploadAppLog(mActivity, "人脸识别", url, e1.getMessage());
                                fail(CallBackType.FACE_VERIFY, "人脸识别失败,请重试");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers,
                                              byte[] bytes, Throwable throwable) {
                            CommonUtils.closeDialog();
                            String successStr = new String(bytes);
                            WyController.uploadAppLog(mActivity, "人脸识别", url, successStr);
                            fail(CallBackType.FACE_VERIFY, "人脸识别失败");
                            // 请求失败
                        }
                    });
        } catch (Exception e) {
            fail(CallBackType.FACE_VERIFY, "人脸识别失败,请重试");
            WyController.uploadAppLog(mActivity, "人脸识别", url, e.getMessage());
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
