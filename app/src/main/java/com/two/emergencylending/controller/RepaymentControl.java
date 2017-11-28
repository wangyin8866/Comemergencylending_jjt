package com.two.emergencylending.controller;

import android.content.Context;

import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.ParamKey;
import com.two.emergencylending.http.HDes3;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CommonalityFieldUtils;
import com.two.emergencylending.utils.LogUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：
 * 创建人：szx
 * 创建时间：2016/7/6 11:06
 * 修改人：szx
 * 修改时间：2016/7/6 11:06
 * 修改备注：
 */
public class RepaymentControl {
    private final String TAG = RepaymentControl.class.getSimpleName();
    public static RepaymentControl instance;
    private static Context mContext;

    public static RepaymentControl getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new RepaymentControl();
        }
        return instance;
    }

    public void requestRepayment(String idcard, String phone) {
        if (!CommonUtils.isNetAvailable()) return;
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put(ParamKey.ID_CARD, idcard);
            map.put(ParamKey.PHONE, phone);
            map.put(ParamKey.PLATFORM, "android");
            map.put(ParamKey.TYPE, CommonalityFieldUtils.getDittchStrAPP());
            CommonUtils.showDialog(mContext, "加载中...");
            OKManager.getInstance().sendJsonPost(NetContants.REPAYMENT_LOGIN, map, new OKManager.Func1() {

                @Override
                public void onFailure(Call call, IOException e) {
                    CommonUtils.closeDialog();
                    if (mOnCompleteListener != null)
                        mOnCompleteListener.complete(1, ErrorCode.FAIL_NETWORK, "", "");
                    LogUtil.e(TAG,ErrorCode.FAIL_NETWORK);
                }

                @Override
                public void onResponse(String res) {
                    CommonUtils.closeDialog();
                    try {
                        String resoult = res;
                        if (resoult.isEmpty()) {
                            mOnCompleteListener.complete(1, ErrorCode.FAIL_DATA, "", "");
                            LogUtil.d(TAG,ErrorCode.FAIL_DATA);
                            return;
                        }
                        JSONObject json = new JSONObject(resoult);
                        String code = json.get("returncode").toString();
                        String token = json.get("token").toString();
                        String message = json.get("errormsg").toString();
                        if (ErrorCode.SUCCESS.equals(code)) {
                            String result = json.get("result").toString();
                            if (mOnCompleteListener != null)
                                mOnCompleteListener.complete(0, message, token, result);
                        } else if (ErrorCode.EQUIPMENT_KICKED_OUT.equals(code)) {
//                    IApplication.getInstance().backToLogin((Activity) mContext);
                        } else {
                            if (mOnCompleteListener != null)
                                mOnCompleteListener.complete(1, message, "", "");
                            LogUtil.d(TAG,message);
                        }
                    } catch (Exception e) {
                        if (mOnCompleteListener != null)
                            mOnCompleteListener.complete(1, ErrorCode.FAIL_DATA, "", "");
                        LogUtil.e(TAG,ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                }
            });
//            HttpUtils.HttpUtil(NetContants.REPAYMENT_LOGIN, message, new HttpCallBack());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private OnCompleteListener mOnCompleteListener;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        mOnCompleteListener = onCompleteListener;
    }

    public interface OnCompleteListener {
        void complete(int status, String msg, String token, String result);
    }

    public static String getResoult(String string) throws Exception {
        if (string != null && !string.equals("") && string.contains("result")) {
            JSONObject json = new JSONObject(string);
            return HDes3.decode(json.get("result").toString());
        } else {
            return "";
        }
    }
}
