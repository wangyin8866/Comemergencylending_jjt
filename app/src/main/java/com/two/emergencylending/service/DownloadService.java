package com.two.emergencylending.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.RemoteViews;

import com.two.emergencylending.activity.SettingActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.utils.LoadingDialog;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.zyjr.emergencylending.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadService extends Service {
    private static final int NOTIFY_ID = 0;
    private int progress;
    private NotificationManager mNotificationManager;
    private boolean canceled;
    /* 下载包安装路径 */
    private static final String savePath = Environment.getExternalStorageDirectory() + "/JJT/updateApkDemo/";

    private static final String saveFileName = savePath + "jijietong.apk";
    private ICallbackResult callback;
    private DownloadBinder binder;
    private boolean serviceIsDestroy = false;

    private Context mContext = this;


    private static String apkUrl = "";
    private static String apkName = "";

    public static String getApkName() {
        return apkName;
    }

    public static void setApkName(String apkName) {
        DownloadService.apkName = apkName;
    }

    public static String getApkUrl() {
        return apkUrl;
    }

    public static void setApkUrl(String apkUrl) {
        DownloadService.apkUrl = apkUrl;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    SettingActivity.getInstance().setDownload(false);
                    // 下载完毕
                    // 取消通知
                    mNotificationManager.cancel(NOTIFY_ID);
                    installApk();
                    break;
                case 2:
                    SettingActivity.getInstance().setDownload(false);
                    // 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
                    // 取消通知
                    mNotificationManager.cancel(NOTIFY_ID);
                    break;
                case 1:
                    int rate = msg.arg1;
                    SettingActivity.getInstance().setDownload(true);
                    if (rate <= 100) {
                        RemoteViews contentview = mNotification.contentView;
                        contentview.setTextViewText(R.id.tv_progress, rate + "%");
                        contentview.setProgressBar(R.id.progressbar, 100, rate, false);
                    } else {
                        System.out.println("下载完毕!!!!!!!!!!!");
                        // 下载完毕后变换通知形式
                        SharedPreferencesUtil.getInstance(IApplication.gainContext()).clearItem(SPKey.AppVersionNo);
                        LoadingDialog.removeDialog();
                        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                        mNotification.contentView = null;
                        Intent intent = new Intent(mContext, SettingActivity.class);
                        // 告知已完成
                        intent.putExtra("completed", "yes");
                        // 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
                        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
//                        mNotification.setLatestEventInfo(mContext, "下载完成", "文件已下载完毕", contentIntent);
                        mNotification.tickerText = "下载完成";
                        mNotification.contentIntent = contentIntent;
                        //
                        serviceIsDestroy = true;
                        stopSelf();// 停掉服务自身
//                        mNotificationManager.notify(NOTIFY_ID, mNotification);
                    }
                    mNotificationManager.notify(NOTIFY_ID, mNotification);
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("是否执行了 onBind");
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("downloadservice ondestroy");
        // 假如被销毁了，无论如何都默认取消了。
        SettingActivity.getInstance().setDownload(false);
    }

    @Override
    public boolean onUnbind(Intent intent) {
     Log.i("keey","downloadservice onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        System.out.println("downloadservice onRebind");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new DownloadBinder();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public class DownloadBinder extends Binder {
        public void start() {
            if (downLoadThread == null || !downLoadThread.isAlive()) {

                progress = 0;
                setUpNotification();
                new Thread() {
                    public void run() {
                        // 下载
                        startDownload();
                    }

                    ;
                }.start();
            }
        }

        public void cancel() {
            canceled = true;
        }

        public int getProgress() {
            return progress;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public boolean serviceIsDestroy() {
            return serviceIsDestroy;
        }

        public void cancelNotification() {
            mHandler.sendEmptyMessage(2);
        }

        public void addCallback(ICallbackResult callback) {
            DownloadService.this.callback = callback;
        }
    }

    private void startDownload() {
        canceled = false;
        downloadApk();
    }

    //
    Notification mNotification;

    // 通知栏

    /**
     * 创建通知
     */
    private void setUpNotification() {
        int icon = R.drawable.jijietong;
        CharSequence tickerText = "开始下载";
        long when = System.currentTimeMillis();
        mNotification = new Notification(icon, tickerText, when);
        // 放置在"正在运行"栏目中
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_download);
        contentView.setTextViewText(R.id.name, apkName + ".apk 正在下载...");
        // 指定个性化视图
        mNotification.contentView = contentView;

//        Intent intent = new Intent(this, NotificationUpdateActivity.class);
//        // 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
//        // 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
//        // 是这么理解么。。。
//        // intent.setAction(Intent.ACTION_MAIN);
//        // intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);

        // 指定内容意图
//        mNotification.contentIntent = contentIntent;
        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }

    //
    /**
     * 下载apk
     *
     * @param url
     */
    private Thread downLoadThread;

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        try {
            File apkfile = new File(saveFileName);
            if (!apkfile.exists()) {
                return;
            }
            installApk(saveFileName);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                Uri contentUri = FileProvider.getUriForFile(IApplication.getInstance().getRunningActivity(), "com.two.emergencylending", apkfile);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//            } else {
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
//            }
//            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            // intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//            mContext.startActivity(intent);
//            callback.OnBackResult("finish");
        } catch (Exception e) {
            callback.OnBackResult("fail");
            e.printStackTrace();
        }
    }

    /**
     * 安装apk
     */
    private void installApk(String apkUrl) {
        Log.i("keey",apkUrl);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是安卓7.0以上
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                File file = new File(apkUrl);
                Uri uri = FileProvider.getUriForFile(mContext, "com.zj.emergencylending", file);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + apkUrl), "application/vnd.android.package-archive");
            }
            startActivity(intent);
            callback.OnBackResult("finish");
        } catch (Exception e) {
            callback.OnBackResult("finish");
            e.printStackTrace();

        }
    }

    private int lastRate = 0;
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {

//                URL url = new URL("http://192.168.9.235:8080/jjt2.0.0.apk");
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = progress;
                    if (progress >= lastRate + 1) {
                        mHandler.sendMessage(msg);
                        lastRate = progress;
                        if (callback != null)
                            callback.OnBackResult(progress);
                    }
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(0);
                        // 下载完了，cancelled也要设置
                        canceled = true;
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!canceled);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

}