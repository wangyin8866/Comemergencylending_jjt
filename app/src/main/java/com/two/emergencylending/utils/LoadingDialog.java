package com.two.emergencylending.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * 加载框
 *
 * @author wangwx
 * @Description
 * @date 2014年6月25日 下午3:03:56
 */
public class LoadingDialog {
    public static Activity myActivity;
    // public static Dialog dialog;
    public static ProgressDialog dialog;
    public static View dialogView;
    public static ImageView frame;

    private static boolean forbid = false;

    public static boolean isForbid() {
        return forbid;
    }

    public static void setForbid(boolean forbid) {
        LoadingDialog.forbid = forbid;
    }


    // public static DialogLoadingCircleView circleView;

    /**
     * 显示加载框
     *
     * @param context
     */
    public static void progressShow(Context context) {
        try {
            if (dialog == null && context != null) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("加载中，请稍后...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
            }
        } catch (Exception e) {
            LogUtil.d("LoadingDialog",e.getMessage());
        }
    }

    /**
     * 取消加载框
     */
    public static void progressDismiss() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            LogUtil.d("LoadingDialog",e.getMessage());
        }
    }


    /**
     * 显示加载框
     *
     * @param context
     */
    public static void newProgressShow(Context context) {
        try {
            if (dialog == null && context != null) {
                dialog = new ProgressDialog(context);
                dialog.setMessage("加载中，请稍后...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                dialog.show();

    /*dialog = new Dialog(context,R.style.style_loading_dialog);
    dialogView = LinearLayout.inflate(context, R.layout.loading_dialog, null);
    frame = (ImageView) dialogView.findViewById(R.id.frame);
    frame.setBackgroundResource(R.drawable.loading_dialog_frame);
    final AnimationDrawable drawable = (AnimationDrawable) frame.getBackground();
    drawable.start();
    DialogLoadingCircleView circleView = (DialogLoadingCircleView) dialogView.findViewById(R.id.circleView);
    circleView.setVisibility(View.VISIBLE);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    params.leftMargin = IApplication.margin * 2;
    params.rightMargin = IApplication.margin * 2;
    dialog.setContentView(dialogView, params);*/
//    dialog.show();
            }
        } catch (Exception e) {
            LogUtil.d("LoadingDialog",e.getMessage());
        }
    }

    /**
     * 取消加载框
     */
    public static void newProgressDismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 提交信息加载提示框
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog loadDialog(Context context, String message) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (forbid) {
                        return true;
                    }
                }
                return false;
            }
        });
        dialog.show();
        return dialog;
    }

    public static void removeDialog() {
        dialog.dismiss();
    }


}
