package com.two.emergencylending.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.two.emergencylending.activity.GestureLockModule.SetGuestActivity;
import com.two.emergencylending.activity.GestureLockModule.UpdateGuestActivity;
import com.two.emergencylending.activity.LoginAndRegisterModule.UpDatepwdActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.controller.LogoutController;
import com.two.emergencylending.controller.UpdateVersionController;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.view.LockPassWordUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：设置页面
 * 创建人：wyp
 * 创建时间：2016/8/10 10:11
 * 修改人：wyp
 * 修改时间：2016/8/10 10:11
 * 修改备注：
 */
public class SettingActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.setting_topbar)
    Topbar topbar;
    @Bind(R.id.rl_update_gespwd)
    RelativeLayout updateGespwd;
    @Bind(R.id.rl_service_phone)
    RelativeLayout rl_service_phone;
    CustomerDialog dialog;
    @Bind(R.id.rl_update_pwd)
    RelativeLayout rl_update_pwd;
    @Bind(R.id.rl_feedback)
    RelativeLayout rl_feedback;
    @Bind(R.id.btn_signout)
    TextView btn_signout;
    @Bind(R.id.tv_update_guest)
    TextView tv_update_guest;
    //    LogoutController logout;
    @Bind(R.id.rl_common_question)
    RelativeLayout rl_common_question;
    @Bind(R.id.tv_edition)
    TextView tv_edition;
    @Bind(R.id.rl_code)
    RelativeLayout rl_code;
    private static boolean isDownload = false;
    private static SettingActivity setting;
    private LogoutController logout;

    public static SettingActivity getInstance() {
        if (null == setting) {
            setting = new SettingActivity();
        }
        return setting;
    }

    public static boolean isDownload() {
        return isDownload;
    }

    public static void setDownload(boolean isDownload) {
        SettingActivity.isDownload = isDownload;
    }

    @Override
    public int setContent() {
        return R.layout.activity_setting;
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
        if (logout == null)
            logout = new LogoutController(this, null);
        tv_edition.setText(CommonUtils.getVersionName(getContext(), getContext().getPackageName()));
    }


    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
    }


    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
        if (LockPassWordUtil.isLogin(this)) {//获取手势开头状态
            tv_update_guest.setText("修改手势密码");
        } else {
            tv_update_guest.setText("设置手势密码");
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

    @OnClick({R.id.rl_code, R.id.rl_update_version, R.id.rl_conmment, R.id.rl_about_us, R.id.rl_common_question, R.id.rl_update_gespwd, R.id.rl_service_phone, R.id.rl_update_pwd, R.id.rl_feedback, R.id.btn_signout, R.id.ges_on_off})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.rl_code:
                CommonUtils.goToActivity(getContext(), MyCodeActivity.class);
                break;
            case R.id.rl_update_version:
//                SharedPreferencesUtil.getInstance(getContext()).setString(SPKey.AppVersionNo,"0");
                new UpdateVersionController(getContext(), null).getAppVersion();
                break;
            case R.id.rl_conmment:
                Intent viewIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse("market://details?id=com.wwx.tvmaster"));
                startActivity(viewIntent);
                break;
            case R.id.rl_about_us:
                CommonUtils.goToActivity(getContext(), AboutUsActivity.class);
                break;
            case R.id.rl_update_pwd:
                CommonUtils.goToActivity(getContext(), UpDatepwdActivity.class, new Intent().putExtra("toLogin", 0));
                break;
            case R.id.rl_service_phone:
                phoneDialog();
                break;
            case R.id.rl_feedback:
                CommonUtils.goToActivity(getContext(), FeedBackActivity.class);
                break;
            case R.id.btn_signout:
                isExitDialog();
                break;
            case R.id.rl_common_question:
                CommonUtils.goToActivity(getContext(), CommonQuestionActivity.class);
                break;
            case R.id.rl_update_gespwd:
                if (LockPassWordUtil.isLogin(this)) {//获取手势开头状态
                    CommonUtils.goToActivity(getContext(), UpdateGuestActivity.class);
                } else {
                    CommonUtils.goToActivity(getContext(), SetGuestActivity.class, new Intent().putExtra("skipt", 1));
                }
                break;
        }
    }

    private void isExitDialog() {
        dialog = new CustomerDialog(getContext());
        dialog.showChoiceDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.ll_cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.ll_sure) {
                    logout.loginout();
                    IApplication.getInstance().clearUserInfo(getContext());
                    IApplication.isToHome = true;
                    finish();
                    dialog.dismiss();
                }
            }
        }, new String[]{"您确认要退出登录吗？", "", "取消", "确定"});
        dialog.show();
    }

    private void phoneDialog() {
        dialog = new CustomerDialog(getContext());
        dialog.showHotLineDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.tv_title) {
                    CommonUtils.callPhone(getContext(), "400-077-6667");
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

}
