package com.two.emergencylending.activity.GestureLockModule;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.two.emergencylending.activity.LoginAndRegisterModule.LoginActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.AppConfig;
import com.two.emergencylending.constant.IntentKey;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.view.GestureHandler;
import com.two.emergencylending.view.GestureLockView;
import com.two.emergencylending.view.LockPassWordUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 36420 on 2016/08/09.
 */
public class UpdateGuestActivity extends BaseActivity implements Topbar.topbarClickListener {

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
        tv_gesture.setText("请输入原手势密码");
        topbar.getTvTitle().setText("修改手势密码");
        tvSkip.setText("跳过次步骤");
        tvSkip.setVisibility(View.INVISIBLE);
        GestureHandler.setIsGone(sg);
    }

    @Override
    public void setData() {
        GestureHandler.init();
        gestureLockView.setAnamition();
        //获取密码
        gestureLockView.setKey(LockPassWordUtil.getPassword(this));
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
                        //验证原密码
                        if (!GestureHandler.getFlag()) {
                            if (!GestureHandler.modifyChvalidate(gestureLockView, tv_gesture, key)) {
                                //验证不通过
                                if (GestureHandler.getNum() >= AppConfig.limitloginPwdNum) {
                                    GestureHandler.setNumOf0();//置零
                                    //登录失败，跳到用户登录页面
                                    LockPassWordUtil.setLogin(getContext(), false);
                                    Intent intent = new Intent();
                                    intent.putExtra(IntentKey.FROM, 0);
                                    CommonUtils.goToActivity(getContext(), LoginActivity.class, intent);
                                    finish();
                                }
                            } else {
                                GestureHandler.setNumOf0();//置零
                            }
                            return;
                        }
                        //设置新密码
                        if (GestureHandler.modifyGestureLock(gestureLockView, sg, tv_gesture, key)) {
                            //修改成功
                            LockPassWordUtil.setLogin(getContext(), true);
                            LockPassWordUtil.savePassWord(getContext(), key, true);
                            finish();
                        }

                    }
                });
    }

    @Override
    public void resume() {
         IApplication.currClass = this.getClass();
    }

    @Override
    public void destroy() {

    }

    @OnClick(R.id.tv_skip)
    public void onClick(View view) {
        LockPassWordUtil.clearPassWork(this);
        //跳转,到主页
        LockPassWordUtil.setPasswordStatus(this, false);
        LockPassWordUtil.setLogin(getContext(), false);
        Intent intent = new Intent();
        intent.putExtra(IntentKey.FROM, 0);
        CommonUtils.goToActivity(getContext(), LoginActivity.class, intent);
        finish();
    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }
}