package com.two.emergencylending.activity;

import android.view.View;
import android.widget.TextView;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.AnnouncementBean;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：公告详情
 * 创建人：szx
 * 创建时间：2016/8/29.
 * 修改人：szx
 * 修改时间：2016/8/29.
 * 修改备注：
 */
public class AnnouncementActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.tv_content)
    TextView content;

    AnnouncementBean announcementBean;
    @Override
    public int setContent() {
        return R.layout.activity_notice_detail;
    }

    @Override
    public int setStatusColor() {
        return R.color.white;
    }

    @Override
    public void init() {
        announcementBean = (AnnouncementBean)getIntent().getSerializableExtra("data");
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        topbar.getTvTitle().setText(announcementBean.getAct_name());
        content.setText(announcementBean.getAct_content());
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



}
