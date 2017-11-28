package com.two.emergencylending.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by wangyaping
 */
public class VerifyUtil {
    //验证编辑框是否为空
    public static  boolean isEmptyET(Context context, EditText verifyET, String edittextName) {
        String s = verifyET.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {//编辑框为空
            ToastAlone.showLongToast(context, edittextName);
            verifyET.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(s.trim())) {//编辑框全为空格
            ToastAlone.showLongToast(context, edittextName);
            verifyET.requestFocus();
            return false;
        }
        return true;
    }

    //验证编辑框是否为空
    public static  boolean isEmptyTV(Context context, TextView verifyET, String edittextName) {
        String s = verifyET.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {//编辑框为空
            ToastAlone.showLongToast(context, edittextName);
            verifyET.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(s.trim())) {//编辑框全为空格
            ToastAlone.showLongToast(context, edittextName);
            verifyET.requestFocus();
            return false;
        }
        return true;
    }


    //验证编辑框的长度
    public static boolean isCorrectLenfth(Context context, EditText verifyET, int min, int max, String edittextName) {
        String s = verifyET.getText().toString();
        if (s.length() < min) {//不能小于最小长度
            ToastAlone.showLongToast(context, edittextName + String.format("不能小于%d位", min));
            verifyET.requestFocus();
            return false;
        } else if (s.length() > max) {//不能大于最大长度
            ToastAlone.showLongToast(context, edittextName + String.format("不能大于%d位", max));
            verifyET.requestFocus();
            return false;
        }
        return true;
    }
}
