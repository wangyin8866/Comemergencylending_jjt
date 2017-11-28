package com.two.emergencylending.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * 可以获取AndroidManifest.xml中相关信息
 * <p>
 * 如定义的<meta-data android:name="Key" android:value="value" />
 * <p>
 * build.gradle中定义的 manifestPlaceholders = [key: "value"](多参数中间以','分割)在AndroidManifest.xml中以${key}引用
 * <p>
 */

public class ToolMetaData {
    private static final String TAG = ToolMetaData.class.getSimpleName();

    private static ToolMetaData instance;
    private String packageName;
    private ApplicationInfo appInfo;

    private static ToolMetaData getInstance(Context context) {
        synchronized (ToolMetaData.class) {
            if (instance == null) {
                instance = new ToolMetaData(context);
            }
        }
        return instance;
    }

    private ToolMetaData(Context context) {
        try {
            // 从AndroidManifest.xml的meta-data中读取SDK配置信息
            packageName = context.getApplicationContext().getPackageName();
            appInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取AndroidManifest.xml 中 meta-data
     *
     * @param context APP上下文
     * @return 返回Bundle对象去获取对应的参数
     */
    public static Bundle getMetaData(Context context) {
        return getInstance(context).appInfo.metaData;
    }


}
