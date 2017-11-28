package com.two.emergencylending.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.umeng.analytics.MobclickAgent;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import butterknife.ButterKnife;


/**
 * Created by wangyaping
 */
public abstract class BaseActivity extends FragmentActivity implements IBaseActivity {
    protected Context banseContext;
    protected View mContentView;
    protected WeakReference<Activity> content = null;
    protected IApplication iApplication;
    public Resources resources;
    public SharedPreferencesUtil mSharedPreferencesUtil;
    private long beginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IApplication.getInstance().addActivity(this);
        Log.i("ActivityManage:", this.getClass().getName());// 打印出每个activity的类名
        try {
            iApplication = (IApplication) getApplication();
            banseContext = this;
            content = new WeakReference<Activity>(this);
            iApplication.pushTask(content);
            resources = this.getResources();
            mSharedPreferencesUtil = SharedPreferencesUtil.getInstance(getApplicationContext());
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            ImageView iv = new ImageView(this);
            iv.setImageResource(setStatusColor());
            LayoutParams statusParams;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (getStick()) {
                    statusParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
                } else {
                    statusParams = new LayoutParams(LayoutParams.MATCH_PARENT, getStatusBarHeight());
                }
            } else {
                statusParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
            }
            layout.addView(iv, statusParams);
            mContentView = LayoutInflater.from(this).inflate(setContent(), null);
            LayoutParams contentParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layout.addView(mContentView, contentParams);
            setContentView(layout);
            ButterKnife.bind(this);
            init();
            setData();
            setListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean getStick() {
        return false;
    }

//    protected abstract  int setBarColor();

    /**
     * 获取当前Activity
     *
     * @return
     */
    protected Activity getContext() {
        if (null != content)
            return content.get();
        else
            return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("BaseActivity:::onResumeonResumeon-----------------");
        resume();
        CommonUtils.DealGeatherPassword(this);
        beginTime = System.currentTimeMillis();
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        IApplication.getInstance().removeActivity(this);
        destroy();
        super.onDestroy();
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

    protected void dolongtimen(int page_id) {
        long ontime = System.currentTimeMillis() - beginTime;
    }
}