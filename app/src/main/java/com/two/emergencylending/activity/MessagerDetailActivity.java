package com.two.emergencylending.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.CenterMessage;
import com.two.emergencylending.constant.IntentKey;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;


/**
 * 项目名称：急借通
 * 类描述：消息详情页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class MessagerDetailActivity extends BaseActivity implements Topbar.topbarClickListener {

    @Bind(R.id.tv_title)
    TextView title;
    @Bind(R.id.tv_time)
    TextView time;
    @Bind(R.id.tv_content)
    TextView content;
    @Bind(R.id.center_message_topbar)
    Topbar topbar;
    CenterMessage centerMessage;

    @Override
    public int setContent() {
        return R.layout.activity_messager_detail;
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
        Intent intent = getIntent();
        centerMessage = (CenterMessage) intent.getSerializableExtra("data");
        title.setText(centerMessage.getNewsTitle());
        time.setText(centerMessage.getCreateDate());
        content.setText(centerMessage.getNewsDetail());
    }

    /*
     * 初始化数据
     */
    private void initData() {

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
            return true;
        }
        return false;
    }

    public void back() {
        Intent intent = new Intent();
        intent.putExtra(IntentKey.DATA, centerMessage);
        setResult(RESULT_OK, intent);
        finish();
    }
}
