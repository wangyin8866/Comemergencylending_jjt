package com.two.emergencylending.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.util.Log;


/**
 * Created by ZENG DONG YANG on 2016/10/24.
 * e-mail:zengdongyang@incamel.com
 */

public class ToolPermission {
    private static final String TAG = ToolPermission.class.getSimpleName();
    private static AlertDialog dialog;
    private static DialogInterface.OnClickListener onClickListener;
    public static void setOnCancelListener(DialogInterface.OnClickListener onClickListener){
        ToolPermission.onClickListener = onClickListener;
    }
    /**
     * 简单检查及请求权限
     *
     * @param activity
     * @param permission
     * @param requestCode
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static Boolean checkSelfPermission(Activity activity, Fragment fragment, String permission, String rMsg, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true;
//        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH
//                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                int hasPermission = activity.checkSelfPermission(permission);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    if (fragment != null) {
                        fragment.requestPermissions(new String[]{permission}, requestCode);
                    } else
                        activity.requestPermissions(new String[]{permission}, requestCode);
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                Log.e(TAG, "checkSelfPermission\n" +
                        e.getLocalizedMessage() + "\n" +
                        e.getStackTrace());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            int op = AppOpsManagerUtil.getOpPermission(permission);
            if (AppOpsManagerUtil.OP_NONE != op) {
                if (AppOpsManagerUtil.isAllowed(activity, op)) {
                    return true;
                } else {
                    showAlert(activity, rMsg);
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 简单检查及请求权限
     *
     * @param activity
     * @param permissions
     * @param requestCode
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static Boolean checkSelfPermission(Activity activity, Fragment fragment, String[] permissions, String rMsg, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true;
//        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH
//                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                StringBuffer sb = new StringBuffer();
                if (permissions != null && permissions.length > 0) {
                    for (String permission : permissions) {
                        int hasPermission=  activity.checkSelfPermission(permission);
                        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                            if (sb.length() == 0) {
                                sb.append(permission);
                            } else {
                                sb.append("," + permission);
                            }
                        }
                    }
                }
                if (sb.length() == 0) {
                    return true;
                } else {
                    permissions = sb.toString().split(",");
                    if (fragment != null) {
                        fragment.requestPermissions(permissions, requestCode);
                    } else
                        activity.requestPermissions(permissions, requestCode);
                    return false;
                }
            } catch (Exception e) {
                Log.e(TAG, "checkSelfPermission\n" +
                        e.getLocalizedMessage() + "\n" +
                        e.getStackTrace());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            boolean flag = true;
            for (String permission : permissions) {
                int op = AppOpsManagerUtil.getOpPermission(permission);
                if (AppOpsManagerUtil.OP_NONE != op) {
                    //21   22
                    if (Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP
                            ||Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP_MR1)
                        if (op>48)
                            continue;
                    //19 20
                    if (Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT
                            ||Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT_WATCH)
                        if (op>43)
                            continue;
                    if (!AppOpsManagerUtil.isAllowed(activity, op)) {
                        flag = false;
                    }
                } else {
                    flag = false;
                }
            }
            if (flag) {
                return true;
            } else {
                showAlert(activity, rMsg);
                return false;
            }
        }
        return false;
    }

    /**
     * 权限获取判断
     *
     * @param permissions
     * @param grantResults
     * @return
     */

    public static Boolean checkPermission(String[] permissions, int[] grantResults) {
        Boolean flag = true;
        if (permissions != null && permissions.length > 0
                && grantResults != null && grantResults.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, permissions[i] + "  PERMISSION_GRANTED");
                } else {
                    Log.d(TAG, permissions[i] + "  PERMISSION_DENIED");
                    flag = false;
                }
            }
        }
        return flag;
    }

    private static void showAlert(final Activity activity, String msg) {
        destroy();
         dialog = new AlertDialog.Builder(activity).create();
        dialog.setMessage(msg);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (onClickListener!=null){
                    onClickListener.onClick(dialog,which);
                }
            }
        });
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT
                || Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClassName("com.android.settings",
                            "com.android.settings.Settings");
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT,
                            "com.android.settings.applications.AppOpsSummary");
                    activity.startActivity(intent);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    public static void destroy() {
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
            dialog=null;
        }
        if (onClickListener!=null){
            onClickListener=null;
        }
    }
}
