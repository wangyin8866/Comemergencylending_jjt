package com.two.emergencylending.utils;

import android.Manifest;
import android.util.Log;

import com.chinazyjr.lib.util.FileUtils;
import com.chinazyjr.lib.util.TimeUtils;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.FileConfig;
import com.two.emergencylending.http.Des3;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * 项目名称：急借通
 * 类描述：
 * 创建人：szx
 * 创建时间：2016/10/12 10:19
 * 修改人：szx
 * 修改时间：2016/10/12 10:19
 * 修改备注：
 */
public class LogUtil {
    private final static String logSuffix = ".log";
    private final static String checkSuffix = ".c";
    private final static String errorSuffix = ".error";
    private static boolean logOpen = false;
    private static boolean saveOpen = true;

    public static boolean isLogOpen() {
        return logOpen;
    }

    public static void setLogOpen(boolean logOpen) {
        LogUtil.logOpen = logOpen;
    }

    public static boolean isSaveOpen() {
        return saveOpen;
    }

    public static void setSaveOpen(boolean saveOpen) {
        LogUtil.saveOpen = saveOpen;
    }

    public static void v(String tag, String msg) {
        if (StringUtil.isNullOrEmpty(msg)) {
            msg = "";
        }
        if (logOpen) {
            Log.v(tag, msg);
        }
        if (saveOpen) {
            writeLog(logSuffix, tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (StringUtil.isNullOrEmpty(msg)) {
            msg = "";
        }
        if (logOpen) {
            Log.d(tag, msg);
        }
        if (saveOpen) {
            writeLog(logSuffix, tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (StringUtil.isNullOrEmpty(msg)) {
            msg = "";
        }
        if (logOpen) {
            Log.i(tag, msg);
        }
        if (saveOpen) {
            writeLog(logSuffix, tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (StringUtil.isNullOrEmpty(msg)) {
            msg = "";
        }
        if (logOpen) {
            Log.w(tag, msg);
        }
        if (saveOpen) {
            writeLog(logSuffix, tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (StringUtil.isNullOrEmpty(msg)) {
            msg = "";
        }
        if (logOpen) {
            Log.e(tag, msg);
        }
        if (saveOpen) {
            writeLog(errorSuffix, tag, msg);
        }
    }

    public static void check(String tag, String msg) {
        if (StringUtil.isNullOrEmpty(msg)) {
            msg = "";
        }
        if (saveOpen) {
            writeLog(checkSuffix, tag, msg);
        }
    }

    private static void writeLog(final String suffix, final String tag, final String msg) {
        Acp.getInstance(IApplication.globleContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        try {
                            String fileName = TimeUtils.getCurrDate() + suffix;
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("time", TimeUtils.getCurrentTime());
                            jsonObject.put("tag", tag);
                            jsonObject.put("msg", Des3.encode(msg));
                            FileUtils.writeFile(FileConfig.LOG_PATH + File.separator + fileName, jsonObject.toString() + "\n", true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastAlone.showShortToast(IApplication.gainContext(), permissions.toString() + "权限拒绝");
                    }
                });
    }
}
