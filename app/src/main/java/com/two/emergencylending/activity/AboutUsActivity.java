package com.two.emergencylending.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zyjr.emergencylending.R;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.view.Topbar;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：关于我们页面
 * 创建人：wyp
 * 创建时间：2016/8/29.
 * 修改人：wyp
 * 修改时间：2016/8/29.
 * 修改备注：
 */
public class AboutUsActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.about_us_topbar)
    Topbar about_us_topbar;
    @Bind(R.id.webview)
    WebView webview;

    @Override
    public int setContent() {
        return R.layout.layout_about_us;
    }

    @Override
    public int setStatusColor() {
        return R.color.white;
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        about_us_topbar.getLeftIco().setVisibility(View.VISIBLE);
        about_us_topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        webview = (WebView) findViewById(R.id.webview);
        loadWeb();
    }

    public void loadWeb() {
        String url = "file:///android_asset/about.html";
        webview.setWebViewClient(new WebViewClient());
        loadLocalHtml(url);
    }

    public void loadLocalHtml(String url) {
        WebSettings ws = webview.getSettings();
        ws.setJavaScriptEnabled(true);//开启JavaScript支持
        ws.setDatabaseEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAppCacheEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.loadUrl(url);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        about_us_topbar.setOnTopbarClickListener(this);
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
