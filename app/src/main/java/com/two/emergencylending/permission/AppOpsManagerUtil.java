package com.two.emergencylending.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by User on 2017/1/9.
 */

public class AppOpsManagerUtil {
    public static final int OP_NONE = -1;
    public static final int OP_COARSE_LOCATION = 0;
    public static final int OP_FINE_LOCATION = 1;
    public static final int OP_GPS = 2;
    public static final int OP_VIBRATE = 3;
    public static final int OP_READ_CONTACTS = 4;
    public static final int OP_WRITE_CONTACTS = 5;
    public static final int OP_READ_CALL_LOG = 6;
    public static final int OP_WRITE_CALL_LOG = 7;
    public static final int OP_READ_CALENDAR = 8;
    public static final int OP_WRITE_CALENDAR = 9;
    public static final int OP_WIFI_SCAN = 10;
    public static final int OP_POST_NOTIFICATION = 11;
    public static final int OP_NEIGHBORING_CELLS = 12;
    public static final int OP_CALL_PHONE = 13;
    public static final int OP_READ_SMS = 14;
    public static final int OP_WRITE_SMS = 15;
    public static final int OP_RECEIVE_SMS = 16;
    public static final int OP_RECEIVE_EMERGECY_SMS = 17;
    public static final int OP_RECEIVE_MMS = 18;
    public static final int OP_RECEIVE_WAP_PUSH = 19;
    public static final int OP_SEND_SMS = 20;
    public static final int OP_READ_ICC_SMS = 21;
    public static final int OP_WRITE_ICC_SMS = 22;
    public static final int OP_WRITE_SETTINGS = 23;
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    public static final int OP_ACCESS_NOTIFICATIONS = 25;
    public static final int OP_CAMERA = 26;
    public static final int OP_RECORD_AUDIO = 27;
    public static final int OP_PLAY_AUDIO = 28;
    public static final int OP_READ_CLIPBOARD = 29;
    public static final int OP_WRITE_CLIPBOARD = 30;
    public static final int OP_WRITE_EXTERNAL_STORAGE = 60;
    private static final String TAG = "AppOpsManagerUtil";

    public static int getOpPermission(String permission){
        if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)){
            return OP_COARSE_LOCATION;
        }
        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)){
            return OP_FINE_LOCATION;
        }
        if (Manifest.permission.VIBRATE.equals(permission)){
            return OP_VIBRATE;
        }
        if (Manifest.permission.WRITE_CONTACTS.equals(permission)){
            return OP_WRITE_CONTACTS;
        }
        if (Manifest.permission.READ_CONTACTS.equals(permission)){
            return OP_READ_CONTACTS;
        }
        if (Manifest.permission.READ_CALL_LOG.equals(permission)){
            return OP_READ_CALL_LOG;
        }
        if (Manifest.permission.WRITE_CALL_LOG.equals(permission)){
            return OP_WRITE_CALL_LOG;
        }
        if (Manifest.permission.READ_CALENDAR.equals(permission)){
            return OP_READ_CALENDAR;
        }
        if (Manifest.permission.WRITE_CALENDAR.equals(permission)){
            return OP_WRITE_CALENDAR;
        }
        if (Manifest.permission_group.LOCATION.equals(permission)){
            return OP_GPS;
        }
        if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)){
            return OP_WRITE_EXTERNAL_STORAGE;
        }
        if (Manifest.permission.CAMERA.equals(permission))
            return OP_CAMERA;
        return OP_NONE;
    }
    /**
     * 是否禁用通知
     */
    public static boolean allowNotification(Context context) {
        return isAllowed(context, OP_READ_CONTACTS);
    }

    /**
     * 是否禁用悬浮窗
     */
    public static boolean allowFloatWindow(Context context) {
        return isAllowed(context, OP_SYSTEM_ALERT_WINDOW);
    }

    /**
     * 是否禁用某项操作
     * 
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isAllowed(Context context, int op) {
        Log.d(TAG, "api level: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < 19) {
            return true;
        }
        Log.d(TAG, "op is " + op);
        String packageName = context.getApplicationContext().getPackageName();
        AppOpsManager aom = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        Class<?>[] types = new Class[]{int.class, int.class, String.class};
        Object[] args = new Object[]{op, Binder.getCallingUid(), packageName};
        try {
            Method method = aom.getClass().getDeclaredMethod("checkOpNoThrow", types);
            method.setAccessible(true);
            Object mode = method.invoke(aom, args);
            Log.d(TAG, "invoke checkOpNoThrow: " + mode);
            if ((mode instanceof Integer) && ((Integer) mode == AppOpsManager.MODE_ALLOWED)) {
                Log.d(TAG, "allowed");
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "invoke error: " + e);
            e.printStackTrace();
        }
        return false;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int isCheck(Context context, int op) {
        Log.d(TAG, "api level: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < 19) {
            return AppOpsManager.MODE_ALLOWED;
        }
        Log.d(TAG, "op is " + op);
        String packageName = context.getApplicationContext().getPackageName();
        AppOpsManager aom = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        Class<?>[] types = new Class[]{int.class, int.class, String.class};
        Object[] args = new Object[]{op, Binder.getCallingUid(), packageName};
        try {
            Method method = aom.getClass().getDeclaredMethod("checkOpNoThrow", types);
            Object mode = method.invoke(aom, args);
            Log.d(TAG, "invoke checkOpNoThrow: " + mode);
            return (int) mode;
        } catch (Exception e) {
            Log.e(TAG, "invoke error: " + e);
            e.printStackTrace();
        }
        return AppOpsManager.MODE_DEFAULT;
    }
   

}
