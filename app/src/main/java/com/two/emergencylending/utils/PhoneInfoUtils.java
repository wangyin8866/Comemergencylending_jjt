package com.two.emergencylending.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.chinazyjr.lib.app.AppBase;

/**
 * 项目名称：ZyLib
 * 类描述：
 * 创建人：szx
 * 创建时间：2016/2/4 16:56
 * 修改人：szx
 * 修改时间：2016/2/4 16:56
 * 修改备注：
 */
public class PhoneInfoUtils {
    private static final String TAG = com.chinazyjr.lib.util.PhoneInfoUtils.class.getSimpleName();
    private static PhoneInfoUtils instance;

    private PhoneInfoUtils() {
        throw new AssertionError();
    }

    public static PhoneInfoUtils getInstance() {
        if (instance == null) {
            instance = new PhoneInfoUtils();
        }
        return instance;
    }

    private static TelephonyManager getPhoneManager(Context context) {
        return (TelephonyManager) context.getSystemService(AppBase.globleContext.TELEPHONY_SERVICE);
    }


    /*
    * 获取手机号码
    * */
    public static String getPhoneNum(Context context) {
        return getPhoneManager(context).getLine1Number();
    }

    /*
    * 获取手机国际识别码IMEI
    * */
    public static String getPhoneIMEI(Context context) {
        return getPhoneManager(context).getDeviceId();
    }

    /*
     * 获取手机IESI
     * */
    public String getPhoneIESI(Context context) {
        return getPhoneManager(context).getSubscriberId();
    }


    /*
    * 获取手机品牌信息
    * */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /*
    * 获取手机型号
    * */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /*
     * 获取系统版本号
     * */
    public static String getSystemSDK() {
        return Build.VERSION.SDK;
    }

    /*
     * 获取系统版本号名
     * */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }


    /*
    * 获取手机UUID
    * */
    public static String getRandomUUID() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取手机服务商信息
     */
    public static String getProvidersName(Context context) {
        String ProvidersName = "N/A";
        try {
            String IMSI = getPhoneIMEI(context);
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return ProvidersName;
    }


}
