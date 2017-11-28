package com.two.emergencylending.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.chinazyjr.lib.manager.AssetsDataBaseManager;
import com.chinazyjr.lib.util.CommonUtils;
import com.chinazyjr.lib.util.ToastUtils;
import com.example.getlimtlibrary.builder.utils.MyLog;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.two.emergencylending.DataBase.MessageManagerDao;
import com.two.emergencylending.DataBase.NoticeDao;
import com.two.emergencylending.activity.LoginAndRegisterModule.LoginActivity;
import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.bean.City;
import com.two.emergencylending.bean.LocationBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.GpsController;
import com.two.emergencylending.fragment.HomeFragment;
import com.two.emergencylending.receiver.HomeKeyEventBroadCastReceiver;
import com.two.emergencylending.service.LocationService;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToolMetaData;
import com.two.emergencylending.view.LockPassWordUtil;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by wangyaping
 */
public class IApplication extends Application {
    private static final String TAG = IApplication.class.getSimpleName();
    public static Resources globleResource;
    public static Context globleContext;
    public static int displayWidth;
    public static int displayHeight;
    public static float xdpi;
    public static float ydpi;
    private static IApplication myApplicationInstance;
    public static boolean isBack = false;//是否点击主界面的BACK退出键
    public LocationService locationService;
    public Vibrator mVibrator;

    public static boolean isRefresh = false;
    public static boolean isToMine = false;
    public static boolean isToHome = false;
    public static boolean isFinish = false;
    public static boolean isLoginOut = false;
    public static Class currClass;
    public static String currCity;
    public static String latitude;
    public static String longitude;


    /***
     * 寄存整个应用Activity
     **/
    private final Stack<WeakReference<Activity>> activitys = new Stack<WeakReference<Activity>>();

    /***
     * 寄存整个应用Activity
     **/
    private final List<Activity> activityList = new ArrayList<Activity>();
    /**
     * 对外提供整个应用生命周期的Context
     **/
    private static Context instance;
    private MessageManagerDao mDao;
    private NoticeDao mNoticeDao;

    private GpsController gps;
    private LocationBean locationBean;
    public static String mCurrPackName = "";

    public List<Activity> getActivityList() {
        return activityList;
    }

    public Activity getActivity(String className) {
        Activity retActivity = null;
        for (Activity activity : getActivityList()) {
            String currClassName = activity.getClass().getName();
            if (currClassName.equals(className)) {
                retActivity = activity;
            }
        }
        if (retActivity == null) {
            for (Activity activity : getActivityList()) {
                return activity;
            }
        }
        return retActivity;
    }

    public Activity getRunningActivity() {
        ActivityManager activityManager = (ActivityManager) globleContext.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return getActivity(runningActivity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationBean = new LocationBean();
        instance = getApplicationContext();
        globleResource = this.getResources();
        globleContext = this;
        LogUtil.setLogOpen(false);//设置打印LOG开关
        PushManager.getInstance().initialize(globleContext);
        String cid = PushManager.getInstance().getClientid(globleContext);
        LogUtil.check(TAG, "clientId:" + cid);
        AssetsDataBaseManager.initManager(globleContext);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        displayWidth = dm.widthPixels;
        displayHeight = dm.heightPixels;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
        HomeFragment.reset();
        mCurrPackName = getPackageName();
        MyLog.i("keey", "mCurrPackName:" + mCurrPackName);
        locationService = new LocationService(globleContext);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        City.getInstance().initCitys();
        /**
         * 注册home key事件广播监听
         */
        initHomeKeyEvent();
        mDao = new MessageManagerDao(this);
        mDao.createMessageCacheTable();
        mNoticeDao = new NoticeDao(this);
        mNoticeDao.createNoticeTable();
        currCity = "";
        latitude = "";
        longitude = "";
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(globleContext));
        //初始话腾讯bugly
        CrashReport.initCrashReport(getApplicationContext(), "4d0f2343fa", true);
        //关闭友盟异常补获，使用腾讯bugly
        MobclickAgent.setCatchUncaughtExceptions(false);
//        MobclickAgent.reportError();
        MobclickAgent.setDebugMode(true);
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.startWithConfigure(
                new MobclickAgent.UMAnalyticsConfig(
                        getApplicationContext(),
                        ToolMetaData.getMetaData(getApplicationContext()).getString("UMENG_APPKEY"),
                        ToolMetaData.getMetaData(getApplicationContext()).getString("UMENG_CHANNEL"),
                        MobclickAgent.EScenarioType.E_UM_NORMAL,
                        true));
        locationService.registerListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                locationBean.setmCurrentLatitude(Double.valueOf(bdLocation.getLatitude()));//纬度

                locationBean.setmCurrentLongitude(Double.valueOf(bdLocation.getLongitude()));//经度
                locationBean.setmCurrentProvince(bdLocation.getProvince());//省
                locationBean.setmCurrentCity(bdLocation.getCity());// 城市
                locationBean.setmCurrentDistrict(bdLocation.getDistrict());// 区
                locationBean.setmCurrentStreet(bdLocation.getStreet());// 街道
                locationBean.setmCurrentStreetNumber(bdLocation.getStreetNumber());//街道号码
                locationBean.setmCurrentAddrStr(bdLocation.getAddrStr());// 地址信息
                UserInfoManager.getInstance().setLocation(locationBean);
                gps = new GpsController(null);
                if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID))
                        && !TextUtils.isEmpty(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN))
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentProvince())
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentCity())
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentDistrict())
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentStreet())
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentStreetNumber())
                        && !TextUtils.isEmpty(String.valueOf(Double.valueOf(UserInfoManager.getInstance().getLocation().getmCurrentLatitude())))
                        && !TextUtils.isEmpty(String.valueOf(Double.valueOf(UserInfoManager.getInstance().getLocation().getmCurrentLongitude())))
                        && !TextUtils.isEmpty(UserInfoManager.getInstance().getLocation().getmCurrentAddrStr())
                        ) {
                    gps.gpsLocationMsg(UserInfoManager.getInstance().getLocation());
                }

                locationService.stop();
                locationService.unregisterListener(this);
            }
        });
        locationService.start();
        Log.e("Location", "Location  init...");
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ImageLoader.getInstance().destroy();
    }

    public static IApplication getInstance() {
        if (null == myApplicationInstance) {
            myApplicationInstance = new IApplication();
        }
        return myApplicationInstance;
    }

    /**
     * 注册home key事件广播监听
     */
    private void initHomeKeyEvent() {
        HomeKeyEventBroadCastReceiver receiver = new HomeKeyEventBroadCastReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    public static Context gainContext() {
        return instance;
    }

    /**
     * 将Activity压入Application栈
     *
     * @param task 将要压入栈的Activity对象
     */
    public void pushTask(WeakReference<Activity> task) {
        activitys.push(task);
    }

    /**
     * 将传入的Activity对象从栈中移除
     *
     * @param task
     */
    public void removeTask(WeakReference<Activity> task) {
        activitys.remove(task);
    }

    /**
     * 根据指定位置从栈中移除Activity
     *
     * @param taskIndex Activity栈索引
     */
    public void removeTask(int taskIndex) {
        if (activitys.size() > taskIndex)
            activitys.remove(taskIndex);
    }

    /**
     * 将栈中Activity移除至栈顶
     */
    public void removeToTop() {
        int end = activitys.size();
        int start = 1;
        for (int i = end - 1; i >= start; i--) {
            if (!activitys.get(i).get().isFinishing()) {
                activitys.get(i).get().finish();
            }
        }
    }


    /**
     * 移除全部（用于整个应用退出）
     */
    public void removeAll() {
        // finish所有的Activity
        for (WeakReference<Activity> task : activitys) {
            if (task != null
                    && task.get() != null
                    && !task.get().isFinishing()) {
                task.get().finish();
            }
        }
    }

    /**
     * 用户是否登录
     *
     * @return true--- 未登录；false---登录
     */
    public boolean isLogin() {
        return TextUtils.isEmpty(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID)) && TextUtils.isEmpty(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
    }

    public void removeTask(Class mineInstantLoanActivityClass) {
        for (WeakReference<Activity> task : activitys) {
            if (task.get().getClass().equals(mineInstantLoanActivityClass))
                if (task != null
                        && task.get() != null
                        && !task.get().isFinishing()) {
                    task.get().finish();
                }
        }
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void finishAllActivity() {
        for (int i = activityList.size() - 1; i >= 0; --i) {
            ((Activity) activityList.get(i)).finish();
        }
    }

    public void clearUserInfo(Context context) {
        clearInfoCache(context);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.JUID);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.LOGIN_TOKEN);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.NAME);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.USERNAME);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.ID_CARD);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.RENEW_LOANS);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.PASSWORD);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.SHARE_CODE);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.APPLY_INFO);
        PersonalDataActivity.custId = "";
        PersonalDataActivity.phoneNum = "";
        PersonalDataActivity.offline_calp_msg = "";
        UserInfoManager.getInstance().setJuid("");
        UserInfoManager.getInstance().setJunxinlinPhone("");
        UserInfoManager.getInstance().clear();
        UserInfoManager.getInstance().setLogin_token("");
        UserInfoManager.getInstance().setLogin(false);
        LockPassWordUtil.clearPassWork(context);
        LockPassWordUtil.setLogin(context, false);
    }

    public void clearInfoCache(Context context) {
        for (int i = 1; i < 5; i++) {
            SharedPreferencesUtil.getInstance(context).clearItem(SPKey.contactKEY + i);
        }
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.personalKey);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.jobInfor);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.CUSTOMER_DERECT);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.CUSTOMER_INDERECT);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.IS_CLERK);
        SharedPreferencesUtil.getInstance(context).clearItem(SPKey.CITY);
    }


    public void backToLogin(Context context) {
        clearUserInfo(context);
        if (!isLoginOut) {
            isLoginOut = true;
            ToastUtils.showShort(IApplication.globleContext, "您的帐号在其他设备登录，请重新登录！");
        }
        finishAllActivity();
        CommonUtils.goToActivity(context, LoginActivity.class);
    }

    public static void AppExit() {
        MobclickAgent.onKillProcess(getInstance());
        System.exit(0);
    }
}
