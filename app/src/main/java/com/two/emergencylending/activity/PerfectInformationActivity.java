package com.two.emergencylending.activity;

import android.view.View;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.IControllerCallBack;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：完善认证信息页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class PerfectInformationActivity extends BaseActivity implements Topbar.topbarClickListener{

    @Bind(R.id.perfect_information_topbar)
    Topbar topbar;

    @Override
    public int setContent() {
        return R.layout.activity_perfect_information;
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
        if (!SharedPreferencesUtil.getInstance(PerfectInformationActivity.this).getBoolean(SPKey.IS_FIRST_INFO_COMPLETE, false)) {
            SharedPreferencesUtil.getInstance(PerfectInformationActivity.this).setBoolean(SPKey.IS_FIRST_INFO_COMPLETE, true);
            CommonUtils.goToActivity(PerfectInformationActivity.this, InfoCompleteGuideActivity.class);
        }
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

    IControllerCallBack iController = new IControllerCallBack() {
        @Override
        public void onSuccess(int returnCode, String value) {
        }

        @Override
        public void onFail(final String errorMessage) {
        }

    };

}