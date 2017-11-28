package com.two.emergencylending.activity;

import android.view.View;
import android.widget.Button;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.AppConfig;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

/**
 * 项目名称：急借通
 * 类描述：赚取金币页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class EarnABonusActivity extends BaseActivity implements Topbar.topbarClickListener {
    Topbar topbar;
    CustomerDialog dialog;
    Button btn_share;

    @Override
    public int setContent() {
        return R.layout.layout_earning_bonusmoney;
    }

    @Override
    public int setStatusColor() {
        return R.color.red_share;
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        btn_share = (Button) findViewById(R.id.btn_share);
        topbar = (Topbar) findViewById(R.id.earn_topbar);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back_white);
        dialog = new CustomerDialog(this);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showShareInviteDialog(AppConfig.SHARE_BONUS, SharedPreferencesUtil.getInstance(EarnABonusActivity.this).getString(SPKey.SHARE_CODE), true);
                dialog.show();
            }
        });
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


}
