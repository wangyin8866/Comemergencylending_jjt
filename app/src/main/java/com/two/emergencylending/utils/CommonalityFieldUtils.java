package com.two.emergencylending.utils;

import com.two.emergencylending.application.IApplication;

/**
 * Created by zj on 2017/7/17.
 */

public class CommonalityFieldUtils {
    /**
     * 渠道标识字段
     */
    public static final String DITCH_JJT_STR = "JJT_1";//急借通
    public static final String DITTCH_YXT_STR = "YXT_1";//盈信通

    public static String getDittchStr() {

        if (IApplication.mCurrPackName.equals("com.two.emergencylending")) {//如果当前是盈信通
            return DITTCH_YXT_STR;
        }
        return DITCH_JJT_STR;
    }

    /**
     * 返回对应的字段
     *
     * @return
     */
    public static String getDittchStrN() {

        if (IApplication.mCurrPackName.equals("com.two.emergencylending")) {//如果当前是盈信通
            return "YXT";
        }
        return "JJT";
    }
    /**
     * 返回对应的字段
     *
     * @return
     */
    public static String getDittchStrAPP() {

        if (IApplication.mCurrPackName.equals("com.two.emergencylending")) {//如果当前是盈信通
            return "yxtapp";
        }
        return "jjtapp";
    }
    /**
     * 获取当前APP名称
     *
     * @return
     */
    public static String getCurrAppName() {

        if (IApplication.mCurrPackName.equals("com.two.emergencylending")) {//如果当前是盈信通
            return "盈信通";
        }
        return "急借通";
    }

    /**
     * @return 更新版本锁需要传的值，1是急借通 2是盈信通
     */
    public static String getUpVersionType() {
        if (IApplication.mCurrPackName.equals("com.two.emergencylending")) {//如果当前是盈信通
            return "2";
        }
        return "1";
    }
}
