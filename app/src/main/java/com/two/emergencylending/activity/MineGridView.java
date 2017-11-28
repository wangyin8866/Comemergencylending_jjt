package com.two.emergencylending.activity;

import com.zyjr.emergencylending.R;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;

public class MineGridView extends BaseActivity{
    @Override
    public int setContent() {
        return R.layout.activity_mine_gridview;
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
