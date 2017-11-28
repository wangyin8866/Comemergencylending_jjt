package com.two.emergencylending.activity;

import android.os.Bundle;

import com.zyjr.emergencylending.R;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;

/**
 * 项目名称：急借通
 * 类描述：关于页面
 * 创建人：wyp
 * 创建时间：2016/8/11.
 * 修改人：wyp
 * 修改时间：2016/8/11.
 * 修改备注：
 */
public class AboutActivity extends BaseActivity {
    @Override
    public int setContent() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
    }

    @Override
    public void setData() {

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
}
