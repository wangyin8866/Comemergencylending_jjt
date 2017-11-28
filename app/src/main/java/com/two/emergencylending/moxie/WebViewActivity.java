package com.two.emergencylending.moxie;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zyjr.emergencylending.R;

/**
 * Created by taoweisong on 16/4/1.
 */
public class WebViewActivity extends Activity implements
        View.OnClickListener {

    private WebView webView;
    private TitleLayout mTitleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        //获取用户传过来的参数
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            init(bundle.getString("openUrl"));
        } else {
            finish();
        }
        mTitleLayout = (TitleLayout) findViewById(R.id.TitleLayout);
        mTitleLayout.getLeftImage().setOnClickListener(this);
        mTitleLayout.getRightImage().setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        try {
            if (id == R.id.TextView_Back) {
                if(webView.canGoBack()){
                    webView.goBack();
                } else {
                    finish();
                }
            } else if(id == R.id.TextView_Refresh) {
                webView.reload();
            }
        } catch (Exception e) {

        }
    }

    private void init(String openUrl){

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new WebMailJavaScriptInterface(), "android");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
                handler.proceed();
            }
        });
        //WebView加载web资源
        webView.loadUrl(openUrl);
    }

    /**
     * js交互
     */
    class WebMailJavaScriptInterface {
        @JavascriptInterface
        public void mxBack(){
            finish();
        }
    }

}
