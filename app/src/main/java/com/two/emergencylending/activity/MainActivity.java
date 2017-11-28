package com.two.emergencylending.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinazyjr.lib.util.ToastUtils;
import com.two.emergencylending.activity.GestureLockModule.GuestLoginActivity;
import com.two.emergencylending.adapter.TabsAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.IControllerCallBack;
import com.two.emergencylending.controller.LoginController;
import com.two.emergencylending.controller.UpdateVersionController;
import com.two.emergencylending.fragment.HomeFragment;
import com.two.emergencylending.fragment.MineFragment;
import com.two.emergencylending.fragment.RepaymentFragment;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.LockPassWordUtil;
import com.two.emergencylending.view.UITabWidget;
import com.umeng.analytics.MobclickAgent;
import com.zyjr.emergencylending.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 项目名称：急借通
 * 类描述：主页面
 * 创建人：wyp
 * 创建时间：2016/8/29 16:09
 * 修改人：wyp
 * 修改时间：2016/8/29 16:09
 * 修改备注：
 */
public class MainActivity extends FragmentActivity implements UITabWidget.OnItemChangedListener, ControllerCallBack, IControllerCallBack {
    private UITabWidget mTabWidget;
    private TabsAdapter mTabsAdapter;
    private FragmentManager manager;
    private int curPosition = 0;
    private long mExitTime = 0;
    private View mContentView;
    private ImageView status;
    private FragmentTransaction transaction;
    protected WeakReference<Activity> content = null;
    private int p;
    private UpdateVersionController upAppVer;
//  private GetContactListController contactList;

    public void setStatusColor(int res) {
        status.setImageResource(res);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        status = new ImageView(this);
        status.setImageResource(R.color.circle_color);
        LinearLayout.LayoutParams statusParams;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            statusParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        } else {
            statusParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        }
        layout.addView(status, statusParams);
        mContentView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(mContentView, contentParams);
        setContentView(layout);
        content = new WeakReference<Activity>(this);
        IApplication.getInstance().pushTask(content);
        IApplication.getInstance().addActivity(this);
        mTabWidget = (UITabWidget) findViewById(R.id.maintab_view1);
        mTabWidget.setOnItemChangedListener(this);
        manager = getSupportFragmentManager();
        mTabsAdapter = new TabsAdapter(this);
        createTabView();// 加载Tab界面
        if (savedInstanceState == null) {// 默认选中第一项Tab
//            int p = SharedPreferencesUtil.getInstance(this).getInt(SPKey.POSITION, 0);
//            mTabWidget.setChecked(p, true);
            mTabWidget.setChecked(0, true);
        } else {
            p = savedInstanceState.getInt("tabPosition", 0);
            mTabWidget.setChecked(p, true);
        }

        upAppVer = new UpdateVersionController(this, null).getAppVersion();

        if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(this).getString(SPKey.USERNAME)) &&
                !TextUtils.isEmpty(SharedPreferencesUtil.getInstance(this).getString(SPKey.PASSWORD))) {
            if (LockPassWordUtil.isLogin(this)) {//获取手势开头状态
                if (LockPassWordUtil.getPasswordStatus(this)) {//获取设置手势状态
                    CommonUtils.goToActivity(this, GuestLoginActivity.class);
                }
            }
//            else {
//                //!IApplication.getInstance().isRegister && 
//                IApplication.getInstance().isRegister = false;
//            }
            if (IApplication.getInstance().isLogin()) {
                autoLogin();
            }
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tabPosition", curPosition);
    }

    /*
     * 动态初始化内容界面
     */
    private void createTabView() {
        int position = 0;
        addTabSpec(UITabWidget.WIDGET_WHAT_HOME, position++);
        addTabSpec(UITabWidget.WIDGET_WHAT_REPAYMENT, position++);
        addTabSpec(UITabWidget.WIDGET_WHAT_MINE, position++);
//		addTabSpec(UITabWidget.WIDGET_WHAT_MORE, position++);
        mTabsAdapter.notifyDataSetChanged();
    }

    /*
     * 添加选项卡界面
     */
    private void addTabSpec(String key, int position) {
        int textId = 0, resId = 0;
        if (key.equals(UITabWidget.WIDGET_WHAT_HOME)) { // 首页
            resId = R.drawable.btn_home_selector;
            textId = R.string.tab_home_text;
            mTabsAdapter.addTab(HomeFragment.class);
        } else if (key.equals(UITabWidget.WIDGET_WHAT_REPAYMENT)) {   //还款
            resId = R.drawable.btn_repayment;
            textId = R.string.tab_repayment_text;
            mTabsAdapter.addTab(RepaymentFragment.class);
        } else if (key.equals(UITabWidget.WIDGET_WHAT_MINE)) { // 我的
            resId = R.drawable.btn_carlorful_selector;
            textId = R.string.tab_mine_text;
            mTabsAdapter.addTab(MineFragment.class);
        }
        mTabWidget.addTabView(resId, textId, position);
    }

    @Override
    public void onChanged(int position) {
//        SharedPreferencesUtil.getInstance(this).setInt(SPKey.POSITION, position);
        switchFrament(position);
    }

    public void switchFrament(int position) {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.main_pager, mTabsAdapter.getItem(position));
        transaction.commitAllowingStateLoss();
    }

    /**
     * 手机上的物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                mExitTime = System.currentTimeMillis();
                ToastAlone.showToast(this, "再按一次退出程序", 1);
            } else {
                IApplication.getInstance().removeAll();
            }
            return true;
        }
        return false;
    }

    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            LogUtil.e("sbar", "get status bar height fail");
            e1.printStackTrace();
        }
        return sbar;
    }

    public void changeToHome() {
        mTabWidget.setChecked(0, true);
    }

    public void changeToMine() {
        mTabWidget.setChecked(1, true);
    }

    @Override
    protected void onResume() {
        IApplication.currClass = this.getClass();
        super.onResume();
        SharedPreferencesUtil.getInstance(this).getBoolean("isHome", false);
        System.out.println("BaseActivity:::onResumeonResumeon-----------------");
        CommonUtils.DealGeatherPassword(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        upAppVer.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.LOCATION) {
            IApplication.currCity = value;
        }
    }

    @Override
    public void onFail(String errorMessage) {

    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
    }

    public void autoLogin() {
        new LoginController(this, new IControllerCallBack() {
            @Override
            public void onSuccess(int returnCode, String value) {
                //获取首页信息、数据
            }

            @Override
            public void onFail(String errorMessage) {
                //跳转到登录界面
            }

        }).login(SharedPreferencesUtil.getInstance(this).getString(SPKey.USERNAME), SharedPreferencesUtil.getInstance(this).getString(SPKey.PASSWORD), false);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

}
