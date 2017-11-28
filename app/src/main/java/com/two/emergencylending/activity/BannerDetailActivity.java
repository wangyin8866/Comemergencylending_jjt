package com.two.emergencylending.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.ValueCallback;
import android.widget.ProgressBar;

import com.example.getlimtlibrary.builder.utils.MyLog;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.X5WebView;
import com.two.emergencylending.utils.X5WebViewUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：轮播图详情
 * 创建人：szx
 * 创建时间：2016/8/29.
 * 修改人：szx
 * 修改时间：2016/8/29.
 * 修改备注：
 */
public class BannerDetailActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.topbar)
    Topbar topbar;
    X5WebView mWebView;
    ProgressBar mProgressBar;
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;

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
        mWebView = (X5WebView) findViewById(R.id.webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        String url = getIntent().getStringExtra("url");
        X5WebViewUtil.getInstance().initWebview(this, mWebView, mProgressBar, url);
    }

    @Override
    public void setData() {
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {//销毁掉webView
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.setVisibility(View.GONE);
            long timeout = ViewConfiguration.getZoomControlsTimeout();//timeout ==3000
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        mWebView.destroy();
                    } catch (Exception e) {
                        MyLog.i("keey", "销毁:" + e.toString());
                    }

                }
            }, timeout);
        }
        super.onDestroy();
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
        X5WebViewUtil.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
