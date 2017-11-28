package com.two.emergencylending.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.ManagerKey;
import com.two.emergencylending.manager.ActivityManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：还款空态页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class NoRepaymentActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.mine_instant_loan_topbar)
    Topbar topbar;
    @Bind(R.id.apply_borrow)
    TextView applyBorrow;
    private int from = 0;

    @Override
    public int setContent() {
        return R.layout.activity_no_repayment;
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

    @OnClick(R.id.apply_borrow)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_borrow:
                ActivityManager.getInstance().finishAllActivitys(ManagerKey.ACTIVITY_REPAYMENT);
                IApplication.getInstance().isToHome = true;
                finish();
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
        back();
    }

    @Override
    public void rightClick() {

    }

    /**
     * 手机上的物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            back();
        }
        return false;
    }


    public void back() {
        if (from == 1) {
            IApplication.getInstance().isToMine = true;
        }
        finish();
    }
}
