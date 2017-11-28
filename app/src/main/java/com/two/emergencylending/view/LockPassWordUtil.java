package com.two.emergencylending.view;

import android.content.Context;

import com.two.emergencylending.constant.AppConfig;
import com.two.emergencylending.utils.SharedPreferencesUtil;


public class LockPassWordUtil {
    private LockPassWordUtil() {
        //Do nothing
    }

    /**
     * 取得手势密码
     *
     * @return 
     */
    public static String getPassword(Context context) {
        String password = SharedPreferencesUtil.getInstance(context).getString(AppConfig.GESTUREPASSWORD, ""); //, "0,1,2,3,4,5,6,7,8"
//        Logger.d("getPassword =- " + password);
        return password;
    }

    /**
     * 取得手势密码的状态，是否是忘记密码后清空手势密码保存的
     *
     * @param context
     * @return true:手势密码开启；false：手势密码关闭
     */
    public static Boolean getPasswordStatus(Context context) {
        Boolean status = SharedPreferencesUtil.getInstance(context).getBoolean(AppConfig.CLEAN, false); //, "0,1,2,3,4,5,6,7,8"
//        Logger.d("getPasswordStatus =- " + status);
        return status;
    }

    /**
     * 更新手势密码的状态，若进入手势密码界面，手势密码不为空，将此状态更改为false
     *
     * @param context
     */
    public static void setPasswordStatus(Context context, boolean clear) {
        SharedPreferencesUtil.getInstance(context).setBoolean(AppConfig.CLEAN, clear);
    }

    /**
     * 重新设置密码，重置密码成功时，更改缓存中的值
     *
     * @param context  上下文
     * @param password 新的手势密码
     * @param clear    true:手势密码开启；false：手势密码关闭
     */
    public static void savePassWord(Context context, String password, boolean clear) {
//        Logger.d("savePassWord == " + password + clear);
        SharedPreferencesUtil.getInstance(context).setString(AppConfig.GESTUREPASSWORD, password);
        LockPassWordUtil.setPasswordStatus(context, clear);
    }

    /**
     * 清空密码，当login的user改变时，以及关闭密码时
     *
     * @param context
     */
    public static void clearPassWork(Context context) {
        savePassWord(context, "", false);
    }


    /**
     * 设置手势开关
     *
     * @param context
     * @param isLogin
     */
    public static void setLogin(Context context, boolean isLogin) {
        SharedPreferencesUtil.getInstance(context).setBoolean(AppConfig.LOGINKEY, isLogin);
    }

    /**
     * 手势开关
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        return SharedPreferencesUtil.getInstance(context).getBoolean(AppConfig.LOGINKEY, true);
    }

}
