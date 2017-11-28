package com.two.emergencylending.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：我的二维码页面
 * 创建人：szx
 * 创建时间：2016/9/9 9:50
 * 修改人：szx
 * 修改时间：2016/9/9 9:50
 * 修改备注：
 */
public class MyCodeActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.code)
    Topbar topbar;
    @Bind(R.id.web_view)
    WebView webView;
    String code;

    @Override
    public int setContent() {
        return R.layout.activity_code;
    }

    @Override
    public int setStatusColor() {
        return R.color.white;
    }

    @Override
    public void init() {
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        code = SharedPreferencesUtil.getInstance(getContext()).getString(SPKey.SHARE_CODE);
        loadWeb();
    }

    public void loadWeb() {
        String url = "http://m.jijietong.com:8680/h5-static/mobile/personQR.html?inviteCode=" + code;
        webView.setWebViewClient(new WebViewClient());
        loadLocalHtml(url);
    }

    public void loadLocalHtml(String url) {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);//开启JavaScript支持
        ws.setDatabaseEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAppCacheEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl(url);
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
