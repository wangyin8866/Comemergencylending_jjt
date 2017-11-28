package com.two.emergencylending.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.WebViewUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：开户页面
 * 创建人：szx
 * 创建时间：2016/8/29.
 * 修改人：szx
 * 修改时间：2016/8/29.
 * 修改备注：
 */
public class OpenAccountActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.topbar)
    Topbar topbar;
    WebView mWebView;
    ProgressBar mProgressBar;

    @Override
    public int setContent() {
        return R.layout.activity_banner_detail;
    }

    @Override
    public int setStatusColor() {
        return R.color.white;
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        mWebView = (WebView) findViewById(R.id.webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        String data = getIntent().getStringExtra("data");
        WebViewUtil.getInstance().loadWebview(mWebView,data);
//        String url = getIntent().getStringExtra("url");
//        WebViewUtil.getInstance().initWebview(this, mWebView, mProgressBar, url);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WebViewUtil.getInstance().onActivityResult(requestCode, resultCode, data);
    }

}
