package com.two.emergencylending.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 2016/8/12.
 */
public class RegularExpUtil {


    // 手机号码验证
    public static boolean isMobile(String tel) {
        Pattern p = Pattern
                .compile("^1[3|4|5|7|8]\\d{9}$");
        Matcher m = p.matcher(tel);
        System.out.println("----" + m.matches());
        Log.d("m", String.valueOf(tel.matches("^1[3|4|5|7|8]\\d{9}$")));
//        Log.d("m",String.valueOf(m.matches()));
        return m.matches();
    }

    public static boolean isSupportedAddress(String address) {
        Pattern p = Pattern.compile("^^(?!\\d+$)(?![A-Za-z]+$)(?!\\-+$)[0-9A-Za-z\\d\\-\\u4e00-\\u9fa5]{2,}$");
        Matcher m = p.matcher(address);
        return m.matches();
    }

    /**
     * 身份证验证
     *
     * @param idCode
     * @return
     */
    public static boolean isIDCard(String idCode) {
        Pattern p = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))(0[1-9]|([1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[X])$");
        Matcher m = p.matcher(idCode);
        return m.matches();
    }

    public static boolean isIDCard(Context context, String idCode) {
        if (idCode.length() != 18) {
            ToastAlone.showToast(context, "请输入18位身份证号码!", Toast.LENGTH_LONG);
            return false;
        }
        if (!isOnlyNumbers(idCode)) {
            if (!(!isOnlyNumbers(idCode.substring(idCode.length() - 1)) && "X".equals(idCode.substring(idCode.length() - 1)))) {
                ToastAlone.showToast(context, "请正确填写身份证号码!", Toast.LENGTH_LONG);
                return false;
            }
        }
        return true;
    }

    public static boolean isSupportedUnit(String name) {
        Pattern p = Pattern.compile("^^(?!\\d+$)(?!\\-+$)[0-9A-Za-z@#:;'\"\\\\,\\!\\$\\s\\(\\)*~\\d\\.\\-\\u4e00-\\u9fa5]{2,}$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 验证电话号码
     *
     * @param telphone
     * @return
     */
    public static boolean telPhone(String telphone) {
        Pattern p = Pattern.compile("^[0-9\\-()（）]{7,18}$");
        Matcher m = p.matcher(telphone);
        return m.matches();
    }

    public static boolean unitPhone(String telphone) {
        Pattern p = Pattern.compile("^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$");
        Matcher m = p.matcher(telphone);
        return m.matches();
    }

    public static boolean isValidPhone(String phone) {
        String regular = "^[0-9]{7,8}$";
        String regex = phone.substring(0, 1) + "{" + phone.length() + "}";
        return phone.matches(regular) && !phone.matches(regex);
    }

    /**
     * 验证QQ号
     */
    public static boolean qq(String qq) {
        Pattern p = Pattern.compile("^[1-9]*[1-9][0-9]*$");
        Matcher m = p.matcher(qq);
        return m.matches();
    }

    /**
     * 验证密码
     */
    public static boolean pwd(String pwd) {
        Pattern p = Pattern.compile("^[A-Za-z0-9_-]+$");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    public static boolean isSupportedContact(String name) {
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5]{2,}$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    //是纯数字
    public static boolean isOnlyNumbers(String value) {
        String regEx = "^\\d*$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(value);
        return m.matches();
    }


    //是纯字母
    public static boolean isOnlyLetters(String value) {
        String regEx = "^[a-zA-Z]*$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(value);
        return m.matches();
    }


    //是中文
    public static boolean isChinese(String value) {
        String regex = "[\u4e00-\u9fa5]+";//表示+表示一个或多个中文，
        return value.matches(regex);
    }

    //包含中文
    public static boolean containChinese(String value) {
        String regex = "[\u4e00-\u9fa5]";
        return Pattern.compile(regex).matcher(value).find();
    }
}
