package com.two.emergencylending.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zyjr.emergencylending.R;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.view.Topbar;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：三方借款协议
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class LoanProtocolActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.loan_protocol_topbar)
    Topbar loan_protocol_topbar;
    @Bind(R.id.webview)
    WebView webview;

    @Override
    public int setContent() {
        return R.layout.layout_loan_protocol;
    }

    @Override
    public int setStatusColor() {
        return R.color.white;
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        loan_protocol_topbar.getLeftIco().setVisibility(View.VISIBLE);
        loan_protocol_topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        webview = (WebView) findViewById(R.id.webview);
        loadWeb();
    }

    public void loadWeb() {
        String url = "file:///android_asset/loanagreement.html";
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
        loan_protocol_topbar.setOnTopbarClickListener(this);
    }

    @Override
    public void resume() {

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
