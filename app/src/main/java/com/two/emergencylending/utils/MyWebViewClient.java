package com.two.emergencylending.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 项目名称：急借通
 * 类描述：
 * 创建人：szx
 * 创建时间：2016/8/22 11:01
 * 修改人：szx
 * 修改时间：2016/8/22 11:01
 * 修改备注：
 */
public class MyWebViewClient extends WebViewClient {
    ProgressBar mProgressBar;

    public MyWebViewClient(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mProgressBar.setVisibility(View.VISIBLE);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        mProgressBar.setVisibility(View.GONE);
        super.onPageFinished(view, url);
    }
}
