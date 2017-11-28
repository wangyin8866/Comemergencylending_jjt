package com.two.emergencylending.controller;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import com.chinazyjr.lib.util.ToastUtils;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CommonalityFieldUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：获取验证码接口
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */

public class AuthCodeController {
    private Button btn;
    public View.OnClickListener listener;
    private Activity context;
    private Map<String, String> formara;
    public final String MSG_REG_ = "MSG_VERIFICATION_CODE_";
    public final String MSG_FIND_PASSWORD_ = "MSG_VERIFICATION_CODE_";
    String mflag, mphone, jsonString;
    boolean isWhiteBackground = false;

    public AuthCodeController(Activity context, View.OnClickListener listener, Button btn) {
        isWhiteBackground = false;
        this.context = context;
        this.listener = listener;
        this.btn = btn;

    }

    public void getAuthCode(String flag, String phone, boolean isWhite) {
        getAuthCode(flag, phone);
        isWhiteBackground = isWhite;
    }

    public void getAuthCode(String flag, String phone) {
        if (!CommonUtils.isNetAvailable()) return;
        this.mflag = flag;
        this.mphone = phone;
        formara = new HashMap<String, String>();
        formara.clear();
        formara.put("mobile", mphone);
        formara.put("flag", mflag);
//        formara.put("imgFlag", uuid); //就是获取图片验证码的UUID
//        formara.put("verificationCode", verificationCode);//图形验证码内容
        formara.put("register_platform", CommonalityFieldUtils.getDittchStr());//区分平台
        final Mytimer mytimer = new Mytimer(60000, 1000);
        OKManager.getInstance().sendComplexForm(NetContants.sendsms, formara, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                LogUtil.d("responese", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String flag = jsonObject.optString("flag");
                    String msg = jsonObject.optString("msg");
                    if (flag.equals(ErrorCode.SUCCESS)) {
                        btn.setOnClickListener(null);
                        btn.setEnabled(false);
                        btn.setBackgroundResource(R.color.lightgrays);
                        mytimer.start();
                        ToastAlone.showLongToast(context, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(context);
                    } else {
                        ToastUtils.showShort(context, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    public class Mytimer extends CountDownTimer {
        public Mytimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            btn.setText("重新发送");
            btn.setEnabled(true);
            btn.setOnClickListener(listener);
            if (isWhiteBackground) {
                btn.setBackgroundResource(R.color.transparent);
            } else {
                btn.setBackgroundResource(R.drawable.button_highlight);
            }

        }
    }

}
