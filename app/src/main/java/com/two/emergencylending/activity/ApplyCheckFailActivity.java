package com.two.emergencylending.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;
import com.zyjr.emergencylending.R;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：申请检测失败
 * 创建人：szx
 * 创建时间：2017/6/15.
 * 修改人：szx
 * 修改时间：2017/6/15.
 * 修改备注：
 */
public class ApplyCheckFailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.view)
    LinearLayout view;
    @Bind(R.id.fail_msg)
    TextView fail_msg;

    @Override
    public int setContent() {
        return R.layout.activity_apple_check_fail;
    }


    @Override
    protected boolean getStick() {
        return true;
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
        String msg = getIntent().getStringExtra("msg");
        fail_msg.setText(msg);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        view.setOnClickListener(this);
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view:
                finish();
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }
}
