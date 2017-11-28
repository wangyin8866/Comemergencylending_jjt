package com.two.emergencylending.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.ThirdPartyKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.ToolImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2017/2/10.
 */

public class IDcardController {
    private static IDcardController instance;
    private Map parameter;
    private String TAG = IDcardController.class.getSimpleName();
    private File file;

    public File getFile() {
        return file;
    }

    //采用单例模式获取对象
    public static IDcardController getInstance() {
        synchronized (IDcardController.class) {
            if (instance == null) {
                instance = new IDcardController();
            }
        }
        return instance;
    }

    public void getIDInfo(Context context, Bitmap bitmap, final IControllerCallBack callBack) {

        file = new File(Environment.getExternalStorageDirectory().getPath() + "/myIdCard/");
        /** 检测文件夹是否存在，不存在则创建文件夹 **/
        if (!file.exists() && !file.isDirectory())
            file.mkdirs();
        file = new File(file.getPath() + "/" + System.currentTimeMillis() + ".jpg");
        ToolImage.compressBitmapToFile(bitmap, Bitmap.CompressFormat.JPEG, 100, file);
        parameter = new HashMap();
        parameter.put("api_key", ThirdPartyKey.FACE_APPKEY);
        parameter.put("api_secret", ThirdPartyKey.FACE_APPSECRET);
//        parameter.put("image", file);//身份证照片图片地址
        parameter.put("legality", 1 + "");//传入1可以判断身份证是否  被编辑/是真实身份证/是复印件/是屏幕翻拍/是临时身份证
        String url = "https://api.faceid.com/faceid/v1/ocridcard";
        CommonUtils.showDialog(context, "正在努力加载.....");
        OKManager.getInstance().postAsynFile(url, "image", file, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                LogUtil.d(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject legality = jsonObject.optJSONObject("legality");
//                    Double Temporary_ID_Photo = legality.optDouble("Temporary ID Photo");
//                    Double Photocopy = legality.optDouble("Photocopy");
//                    Double Screen = legality.optDouble("Screen");
//                    Double Edited = legality.optDouble("Edited");
//                    Double toal = ID_Photo+Temporary_ID_Photo+Photocopy+Screen+Edited;
//                

                    if ("back".equals(jsonObject.getString("side"))) {
                        // 身份证背后信息
                        String valid_date = jsonObject.optString("valid_date");
                        String issued_by = jsonObject.optString("issued_by");
                        String side = jsonObject.optString("side");
                        if (callBack != null) {
//                            callBack.onSuccess(1,ID_Photo<=1&&ID_Photo>0.8?"true":"false");
                            callBack.onSuccess(0, "请求成功");
                        } else
                            callBack.onFail("请使用正式身份证拍照");
                    } else if ("front".equals(jsonObject.getString("side"))) {
                        // 身份证正面信息
                        String name = jsonObject.optString("name");
                        String id_card_number = jsonObject.optString("id_card_number");
                        String address = jsonObject.optString("address");

                        UserInfoManager.getInstance().setUserName(name);
                        UserInfoManager.getInstance().setUser_IdCard(id_card_number);
                        UserInfoManager.getInstance().setUserHuijiDelAddress(address);

                        if (callBack != null) {
                            callBack.onSuccess(0, "请求成功");
                        } else
                            callBack.onFail("请使用正式身份证拍照");
                    } else {
                        String error = jsonObject.optString("error");
                        callBack.onFail(error);
                    }
                } catch (JSONException e) {
                    callBack.onFail("请使用正式身份证拍照");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFail("请使用正式身份证拍照");
                e.printStackTrace();
            }
        });
    }
}
