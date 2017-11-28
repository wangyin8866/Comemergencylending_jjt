package com.two.emergencylending.manager;

import android.Manifest;

import com.chinazyjr.lib.util.FileUtils;
import com.chinazyjr.lib.util.TimeUtils;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.FileConfig;
import com.two.emergencylending.utils.ToastAlone;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * 项目名称：j急借通
 * 类描述：埋点
 * 创建人：szx
 * 创建时间：2016/9/22 16:09
 * 修改人：szx
 * 修改时间：2016/9/22 16:09
 * 修改备注：
 */
public class BuryPointManager {
    private static final String FileName = "md.txt";

    public static String getBuryTime() {
        return TimeUtils.getCurrentTime();
    }

    public static void buryActivityBegin(String eventId) {
        buryBegin(eventId, "");
    }

    public static void buryActivityEnd(String eventId) {
        buryEnd(eventId, "");
    }

    public static void buryBegin(String eventId, String eventData) {
        bury(eventId + "A", getBuryTime(), eventData);
    }

    public static void buryEnd(String eventId, String eventData) {
        bury(eventId + "B", getBuryTime(), eventData);
    }

    public static void buryClick(String eventId, String eventData) {
        bury(eventId + "C", getBuryTime(), eventData);
    }


    private static void bury(String eventId, String eventTime, String eventData) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("eventId", eventId);
            jsonObject.put("eventTime", eventTime);
            jsonObject.put("eventData", eventData);
            bury(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void bury(final String content) {
        Acp.getInstance(IApplication.globleContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        try {
                            FileUtils.writeFile(FileConfig.BP_PATH + File.separator + FileName, content + "\n", true);
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
