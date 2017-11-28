package com.two.emergencylending.activity;

import com.zyjr.emergencylending.R;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;

/**
 * 项目名称：jijietong1.08
 * 类描述：
 * 创建人：szx
 * 创建时间：2017/1/10 16:16
 * 修改人：szx
 * 修改时间：2017/1/10 16:16
 * 修改备注：
 */
public class ProcessActivity  extends BaseActivity {
    @Override
    public int setContent() {
        return R.layout.activity_process;
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

    }

    @Override
    public void destroy() {

    }
}
