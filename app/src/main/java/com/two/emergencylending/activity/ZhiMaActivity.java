package com.two.emergencylending.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.PerSonalBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.GetCredentialState;
import com.two.emergencylending.controller.GetUserDataControlller;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.utils.WyUtils;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zj on 2017/9/11.
 */

public class ZhiMaActivity extends BaseActivity implements ControllerCallBack, View.OnClickListener, Topbar.topbarClickListener {

    GetCredentialState mGetCredentialState;
    GetUserDataControlller getUserDataControlller;
    @Bind(R.id.personal_tolbar)
    Topbar personal_tolbar;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.card_name)
    EditText cardName;
    @Bind(R.id.submit)
    Button submit;
    @Bind(R.id.step1)
    LinearLayout step1;
    @Bind(R.id.webView)
    WebView webView;
//    private String url;
    private String currUrl;

    @Override
    public int setContent() {
        return R.layout.activity_zhima_step1;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        mGetCredentialState = new GetCredentialState(getContext(), this);
        getUserDataControlller = new GetUserDataControlller(getContext(), this);
        getUserDataControlller.getuserPersonalData();
        personal_tolbar.getLeftIco().setVisibility(View.VISIBLE);
        personal_tolbar.getLeftIco().setImageResource(R.drawable.icon_back);
    }

    @Override
    public void setData() {
    }

    @Override
    public void setListener() {
        personal_tolbar.setOnTopbarClickListener(this);
    }

    @Override
    public void resume() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        Log.e("returnCode", returnCode + "");
        if (returnCode == CallBackType.CREDENTIAL_ZHIMA_AUTHORIZATION) {
//            url = value;
            currUrl = value;
            step1.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

            loadWebView(webView, currUrl);
        } else if (returnCode == CallBackType.GET_PERSONAL_INFOR) {
            PerSonalBean bean = UserInfoManager.getInstance().getPerSonalBean();
            name.setText(bean.getUsername().trim());
            cardName.setText(bean.getIdcard().trim());
        } else if (returnCode == CallBackType.CREDENTIAL_ZHIMA_INTEGRAL) {
            finish();
        }
    }

    private void loadWebView(WebView webView, String url) {

        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                if (url.contains("http://m.jijietong.com:8680/h5-static/moblie_web_new/compliance/openSuccess.html")) {
                    Log.e("url", url);
                    String applyId = WyUtils.getValueByName(url, "applyId");
                    String state = WyUtils.getValueByName(url, "state");
                    String success = WyUtils.getValueByName(url, "success");
                    Log.e("applyId", applyId);
                    Log.e("state", state);
                    Log.e("success", success);
                    if (success.equals("true")) {
                        mGetCredentialState.getZhiMaData(state, applyId);
                    } else {
                        view.loadUrl(currUrl);
                    }

                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有证书
            }
        };
        webView.getSettings().setDomStorageEnabled(true);//支持所有标签
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setDrawingCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSavePassword(false);// 不弹窗浏览器是否保存密码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 自动适应屏幕尺寸
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // 触摸焦点起作用
        webView.requestFocus();
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.CREDENTIAL_ZHIMA_AUTHORIZATION) {
            ToastAlone.showLongToast(ZhiMaActivity.this, errorMessage);
        } else if (returnCode == CallBackType.CREDENTIAL_ZHIMA_INTEGRAL) {
            ToastAlone.showLongToast(ZhiMaActivity.this, errorMessage);
        } else if (returnCode == CallBackType.GET_PERSONAL_INFOR) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        if (name.getText().toString().trim().equals("")) {
            ToastAlone.showLongToast(ZhiMaActivity.this, "姓名不能为空！");
        } else if (cardName.getText().toString().trim().equals("")) {
            ToastAlone.showLongToast(ZhiMaActivity.this, "身份证不能为空！");
        } else {
            mGetCredentialState.ZhiMaAuthorization();
        }
    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {
    }
}
