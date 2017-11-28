package com.two.emergencylending.activity.GestureLockModule;

import android.view.View;
import android.widget.TextView;

import com.two.emergencylending.activity.MainActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.view.GestureHandler;
import com.two.emergencylending.view.GestureLockView;
import com.two.emergencylending.view.LockPassWordUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 36420 on 2016/08/08.
 */
public class SetGuestActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.gestureLockView)
    GestureLockView gestureLockView;
    @Bind(R.id.tv_gesture)
    TextView tv_gesture;
    @Bind(R.id.tv_skip)
    TextView tvSkip;
    @Bind(R.id.sg)
    View sg;
    private int skipt;

    @Override
    public int setContent() {
        return R.layout.activity_logingesture;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        GestureHandler.init();
        skipt = getIntent().getIntExtra("skipt", 0);
        if (skipt == 1) {
            tvSkip.setVisibility(View.INVISIBLE);
        } else if (skipt == 2) {
            tvSkip.setText("跳过此步骤");
            tvSkip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        gestureLockView.setOnGestureFinishListener(
                new GestureLockView.OnGestureFinishListener() {
                    @Override
                    public void OnGestureFinish(boolean success, String key) {
                        if (GestureHandler.islimitNum(gestureLockView, key)) {
                            return;
                        }
                        if (GestureHandler.setGestureLock(gestureLockView, sg, tv_gesture, key)) {
                            LockPassWordUtil.setLogin(getContext(), true);
                            LockPassWordUtil.savePassWord(getContext(), key, true);
                            //设置成功,跳转
                            if (skipt == 2) {
                                CommonUtils.goToActivity(getContext(), MainActivity.class);
//                                IApplication.getInstance().isRegister = true;
                            }
                            finish();
                        }

                    }
                });
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
        SharedPreferencesUtil.getInstance(getContext()).setBoolean("isShowGestureView", true);
    }

    @Override
    public void destroy() {
        SharedPreferencesUtil.getInstance(getContext()).setBoolean("isShowGestureView", false);
    }

    @OnClick(R.id.tv_skip)
    public void onClick(View view) {
        //跳转
        LockPassWordUtil.setLogin(getContext(), false);
        CommonUtils.goToActivity(getContext(), MainActivity.class);
        finish();
    }

    @Override
    public void leftClick() {
        if (skipt == 2) {
            CommonUtils.goToActivity(getContext(), MainActivity.class);
        } else {
            finish();
        }

    }

    @Override
    public void rightClick() {

    }

}
