package com.two.emergencylending.controller;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;

import com.chinazyjr.lib.util.ToastUtils;
import com.google.gson.Gson;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.VersioBean;
import com.two.emergencylending.constant.AppConfig;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.service.DownloadService;
import com.two.emergencylending.service.ICallbackResult;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CommonalityFieldUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.LoadingDialog;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.zyjr.emergencylending.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 36420 on 2016/08/21.
 */
public class UpdateVersionController implements ICallbackResult {
    Map parameter;
    Activity mActivity;
    IControllerCallBack controller;
    private CustomerDialog mCustomerDialog;
    private static boolean isDownload = false;
    private boolean isBinded;
    private DownloadService.DownloadBinder binder;

    public UpdateVersionController(Activity activity, IControllerCallBack controller) {
        this.mActivity = activity;
        this.controller = controller;
        mCustomerDialog = new CustomerDialog(mActivity);
    }

    public static boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

    public ServiceConnection conns = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownloadService.DownloadBinder) service;
            System.out.println("服务启动!!!");
            // 开始下载
            isBinded = true;
            binder.addCallback(UpdateVersionController.this);
            binder.start();
        }
    };

    public UpdateVersionController getAppVersion() {
        if (!CommonUtils.isNetAvailable()) return this;
        parameter = new HashMap();
        parameter.clear();
//        SharedPreferencesUtil.getInstance(mActivity).clearItem(SPKey.AppVersionNo);
        parameter.put("platform_type", AppConfig.platform_type);
        parameter.put("app_version_no", CommonUtils.getVersionName(mActivity, mActivity.getPackageName()));
        parameter.put("app_type", CommonalityFieldUtils.getUpVersionType());
        LogUtil.d("request", parameter.toString().trim());
//        CommonUtils.showDialog(mActivity, "正在努力加载");
        OKManager.getInstance().sendComplexForm(NetContants.VERSION, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                try {
                    String responseInfo = result.toString().trim();
                    LogUtil.d("Updateresponse", responseInfo);
                    JSONObject object = null;
                    object = new JSONObject(responseInfo);
                    String flag = object.optString("flag");
                    String msg = object.optString("msg");
                    //  String appVersionNo= SharedPreferencesUtil.getInstance(mActivity).getString(SPKey.AppVersionNo,"0"); 
                    String jsonObject = object.optString("result");
                    if (jsonObject.equals("")) {
                        return;
                    }
                    VersioBean versioBean = new Gson().fromJson(jsonObject, VersioBean.class);

                    String appVersionNo = SharedPreferencesUtil.getInstance(mActivity).getString(SPKey.AppVersionNo, "0");
                    if (flag.equals("0003")) {
//                        if (appVersionNo.equals("0")) {
                        DownloadService.setApkUrl(versioBean.getApp_url());
                        DownloadService.setApkName(mActivity.getString(R.string.app_all_name));
                        updateVersionDialog("", flag);
//                        }
                    } else if (flag.equals("0004")) {
                        if (appVersionNo.equals("0")) {
                            DownloadService.setApkUrl(versioBean.getApp_url());
                            DownloadService.setApkName(mActivity.getString(R.string.app_all_name));
                            updateVersionDialog("", flag);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    @Override
    public void OnBackResult(Object result) {
        if ("finish".equals(result)) {
            setDownload(false);
            LoadingDialog.setForbid(false);
            LoadingDialog.removeDialog();
            return;
        } else if ("fail".equals(result)) {
            setDownload(false);
            LoadingDialog.setForbid(false);
            LoadingDialog.removeDialog();
            ToastAlone.showLongToast(IApplication.globleContext, "版本更新失败，请致腾讯应用宝市场下载");
            IApplication.getInstance().AppExit();
            return;
        }
    }


    public void onDestroy() {
        if (mCustomerDialog != null && mCustomerDialog.isShowing()) {
            mCustomerDialog.dismiss();
        }
    }

    /**
     * 版本更新提示框
     */
    public void updateVersionDialog(String content, String force) {
        final String finalForce = force;
        Boolean isForce = false;
        String cancle = "取消";
        if (force.equals("0003")) {
            isForce = true;
            cancle = "退出";
        }
        mCustomerDialog.alertMessage(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.ll_cancel) {
                    mCustomerDialog.dismiss();
                    if (finalForce.equals("0003")) {
                        IApplication.getInstance().AppExit();
                    }
//                    if (finalForce.equals("0004")){
//                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.AppVersionNo,"1");
//                    }
                } else if (v.getId() == R.id.ll_sure) {
                    if (isDownload()) {
                        ToastUtils.showLong(mActivity, "已经在下载中");
                        mCustomerDialog.dismiss();
                    } else {
//                        SharedPreferencesUtil.getInstance(mActivity).setString(SPKey.AppVersionNo, "1");
                        setDownload(true);
                        Intent it = new Intent(mActivity, DownloadService.class);
                        mActivity.startService(it);
                        mActivity.bindService(it, conns, Context.BIND_AUTO_CREATE);
                        mCustomerDialog.dismiss();
                        if (finalForce.equals("0003")) {
                            LoadingDialog.setForbid(true);
                        }
                        LoadingDialog.loadDialog(mActivity, "新版本下载中，可在状态栏中查看...");
                    }
                }
            }
        }, new String[]{"有新版本", content, cancle, "立即更新"}, isForce);
    }

}