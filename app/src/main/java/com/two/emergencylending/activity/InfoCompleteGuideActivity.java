package com.two.emergencylending.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.two.emergencylending.utils.LogUtil;
import com.zyjr.emergencylending.R;

import java.lang.reflect.Field;

/**
 * 项目名称：急借通
 * 类描述：完善认证信息引导页
 * 创建人：szx
 * 创建时间：2016/9/6 16:09
 * 修改人：szx
 * 修改时间：2016/9/6 16:09
 * 修改备注：
 */
public class InfoCompleteGuideActivity extends Activity implements View.OnClickListener {
    ImageView guide;
    View status_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_info_guide);
        status_bar = (View) findViewById(R.id.status_bar);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.height = getStatusBarHeight();// 控件的高强制设成20
        status_bar.setLayoutParams(layoutParams);
        guide = (ImageView) findViewById(R.id.info_guide);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        guide.setLayoutParams(params);
        guide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_guide:
                finish();
                break;
        }
    }

    /**
     * 手机上的物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
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
}
