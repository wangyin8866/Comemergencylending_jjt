package com.two.emergencylending.activity.GestureLockModule;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.two.emergencylending.activity.LoginAndRegisterModule.LoginActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.AppConfig;
import com.two.emergencylending.constant.IntentKey;
import com.two.emergencylending.manager.ActivityManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToastAlone;
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
public class GuestLoginActivity extends BaseActivity {

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
    private long mExitTime = 0;


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
        GestureHandler.init();//手势初始化
        topbar.getTvTitle().setText("请输入手势");
        topbar.getLeftIco().setVisibility(View.INVISIBLE);
        tv_gesture.setText("请输入手势密码");
        GestureHandler.setIsGone(sg);
    }

    @Override
    public void setData() {
        if (LockPassWordUtil.isLogin(this)) {//手势是否打开
            if (LockPassWordUtil.getPasswordStatus(this)) {//获取手势密码的状态
                topbar.getTvTitle().setText("请输入手势密码");
                tvSkip.setText("其他登录方式");
                LogUtil.e("Key", LockPassWordUtil.getPassword(this));
                gestureLockView.setKey(LockPassWordUtil.getPassword(this));//获取设置密码 
//            } else {
//                //设置密码
//                topbar.getTvTitle().setText("设置手势密码");
//                tvSkip.setText("跳过");
//                gestureLockView.setKey("");
            }
        } else {
            //跳过，到主页
//            CommonUtils.goToActivity(getContext(), MainActivity.class);
//            finish();
        }
//        gestureLockView.setAnamition();
    }

    @Override
    public void setListener() {
        gestureLockView.setOnGestureFinishListener(
                new GestureLockView.OnGestureFinishListener() {
                    @Override
                    public void OnGestureFinish(boolean success, String key) {
                        //判断手势密码个数
                        if (GestureHandler.islimitNum(gestureLockView, key)) {
                            return;
                        }
                        //判断状态
                        if (LockPassWordUtil.getPasswordStatus(GuestLoginActivity.this)) {
                            //登录
                            if (GestureHandler.loginGestureLock(gestureLockView, null, tv_gesture, key)) {
                                //登陆成功
                                LockPassWordUtil.setLogin(getContext(), true);
                                LogUtil.e("key", "OK");
                                finish();
                            }
                            if (GestureHandler.getNum() >= AppConfig.limitloginPwdNum) {
                                GestureHandler.setNumOf0();//置零
                                //登录失败，跳到用户登录页面
                                LockPassWordUtil.setLogin(getContext(), false);
                                LockPassWordUtil.savePassWord(getContext(), key, false);
                                CommonUtils.goToActivity(getContext(), LoginActivity.class,new Intent().putExtra(IntentKey.GuestLoginActivity, 3));
                                finish();
                            }
                            return;
//                        } else {
//                            //设置手势
//                            if (GestureHandler.setGestureLock(gestureLockView, null, tv_gesture, key)) {
//                                //设置手势成功
//                                LockPassWordUtil.setLogin(getContext(), true);
//                                LockPassWordUtil.savePassWord(getContext(), key, true);
//                            }
                        }

                    }
                });
    }

    @Override
    public void resume() {
         IApplication.currClass = this.getClass();
        if (IApplication.isFinish) {
            finish();
            IApplication.isFinish = false;
        }
        SharedPreferencesUtil.getInstance(getContext()).setBoolean("isShowGestureView", true);
    }

    @Override
    public void destroy() {
        SharedPreferencesUtil.getInstance(getContext()).setBoolean("isShowGestureView", false);
    }

    @OnClick(R.id.tv_skip)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
                CommonUtils.goToActivity(getContext(), LoginActivity.class, new Intent().putExtra("login", 0));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                mExitTime = System.currentTimeMillis();
                ToastAlone.showToast(this, "再按一次退出程序", 1);
            } else {
                IApplication.isBack = true;
                IApplication.getInstance().removeAll();
                IApplication.getInstance().finishAllActivity();
                ActivityManager.getInstance().finishAllActivitys();
//                System.exit(0);
                finish();
            }
            return true;
        }
        return false;
    }



}