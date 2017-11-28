package com.two.emergencylending.activity.GuideModule;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.two.emergencylending.activity.MainActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.permission.ToolPermission;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SettingManager;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.zyjr.emergencylending.R;


/**
 * 项目名称：急借通
 * 类描述：欢迎页
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class WelcomeActivity extends BaseActivity {
    private SharedPreferencesUtil sharedpreferencesutil;

    @Override
    public int setContent() {
        return R.layout.activity_welcome;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    protected boolean getStick() {
        return true;
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        sharedpreferencesutil = SharedPreferencesUtil.getInstance(getContext());
        widowInit();
    }

    @Override
    public void setData() {
        if (ToolPermission.checkSelfPermission(this, null, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, "请允许权限", 111)) {
            toHome();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            toHome();
            com.two.emergencylending.utils.LogUtil.setSaveOpen(false);
            com.chinazyjr.lib.util.Logger.setSave(false);
        }
        ToolPermission.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111)
            if (ToolPermission.checkPermission(permissions, grantResults)) {
                toHome();
            } else {
                toHome();
                com.two.emergencylending.utils.LogUtil.setSaveOpen(false);
                com.chinazyjr.lib.util.Logger.setSave(false);
            }
    }


    private void toHome() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (SharedPreferencesUtil.getInstance(WelcomeActivity.this).getBoolean(SPKey.IS_FIRST_INSTAN, true)) {
                    SharedPreferencesUtil.getInstance(WelcomeActivity.this).setBoolean(SPKey.IS_FIRST_INSTAN, false);
                    Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    CommonUtils.goToActivity(getContext(), MainActivity.class);
                    finish();
                }
            }
        }).start();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
    }

    @Override
    public void destroy() {

    }

    private void widowInit() {
        int beginY = 0;
        getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        Rect frame = new Rect();
        int statusBarHeight = frame.top;
        SettingManager.getInstance().setSystemStatusAreaHeight(frame.top);
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//获取状态栏的的高度
        beginY = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        if (beginY == 0) {
            beginY = statusBarHeight;
        }
        int systemBottomHeight = 0;
        int full_height = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        systemBottomHeight = (full_height - frame.bottom);
        SettingManager.getInstance().setSystemBottomAreaheight(systemBottomHeight);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SettingManager.getInstance().setDensity(dm.density);
        SettingManager.getInstance().setScreenWidth(dm.widthPixels);
        SettingManager.getInstance().setScreenHeight(dm.heightPixels - frame.top - systemBottomHeight);
    }
}
