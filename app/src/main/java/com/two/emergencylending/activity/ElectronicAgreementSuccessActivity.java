package com.two.emergencylending.activity;

import android.view.View;

import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：签订协议成功
 * 创建人：szx
 * 创建时间：2016/9/26 10:11
 * 修改人：szx
 * 修改时间：2016/9/26 10:11
 * 修改备注：
 */
public class ElectronicAgreementSuccessActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.topbar)
    Topbar topbar;

    @Override
    public int setContent() {
        return R.layout.activity_electronic_agreement_success;
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
}
