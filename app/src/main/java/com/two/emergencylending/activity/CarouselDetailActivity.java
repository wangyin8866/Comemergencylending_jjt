package com.two.emergencylending.activity;

import android.view.View;
import android.widget.Button;

import com.zyjr.emergencylending.R;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.AppConfig;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.view.Topbar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：轮播图展示页面（废除该类）
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class CarouselDetailActivity extends BaseActivity implements Topbar.topbarClickListener {
    private final String TAG = CarouselDetailActivity.class.getSimpleName();
    @Bind(R.id.center_message_topbar)
    Topbar topbar;
    @Bind(R.id.btn_share)
    Button share;
    CustomerDialog dialog;

    @Override
    public int setContent() {
        return R.layout.activity_carouse_detail;
    }

    @Override
    public int setStatusColor() {
        return R.color.red_ad;
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back_white);
        dialog = new CustomerDialog(this);
    }

    @Override
    public void setData() {
    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);

    }

    @OnClick(R.id.btn_share)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share:
                dialog.showShareInviteDialog(AppConfig.SHARE_AD, SharedPreferencesUtil.getInstance(CarouselDetailActivity.this).getString(SPKey.SHARE_CODE), true);
                dialog.show();
                break;
        }
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

}
