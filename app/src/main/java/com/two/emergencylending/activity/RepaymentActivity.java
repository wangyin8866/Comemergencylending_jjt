package com.two.emergencylending.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.H5PageKey;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.MyWebChromeClient;
import com.two.emergencylending.utils.MyWebViewClient;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：我的还款页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class RepaymentActivity extends BaseActivity implements Topbar.topbarClickListener {
    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.mine_instant_loan_topbar)
    Topbar topbar;
    private MyWebChromeClient myWebChromeClient;

    private int webPage = 0;

    @Override
    public int setContent() {
        return R.layout.view_web_progress;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        String title = "";
        String url = "";
        String token = getIntent().getStringExtra("token");
//        String productID = getIntent().getStringExtra("FXDPRODUCTID");
//        String fxdrepayment = getIntent().getStringExtra("FXDREPAYMENT");
        String hide = getIntent().getStringExtra("repayment");
//        if (!TextUtils.isEmpty(fxdrepayment)) {
//            title = "我的还款";
//            url = NetContants.FXD_REPAYMENT_HISTORY + "?case_no_=" + fxdrepayment;
//        }
//        if (CommonConstant.PRODUCT_FXDONLINE.equals(productID)) {
//            title = "我的还款";
//            url = NetContants.H5_FXDREPAYMETT + "&mobile_phone_=" + SharedPreferencesUtil.getInstance(getContext()).getString(SPKey.USERNAME);
//        } else {
            if (H5PageKey.REPAYMENT.equals(getIntent().getStringExtra("page"))) {
                title = "我的还款";
                url = getUrl(token, H5PageKey.REPAYMENT);
            } else if (H5PageKey.BILL.equals(getIntent().getStringExtra("page"))) {
                title = "我的还款";
                url = getUrl(token, H5PageKey.BILL, getIntent().getStringExtra("id"));
            }
//        }
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        if ("hide".equals(hide)) {
            topbar.getLeftIco().setVisibility(View.GONE);
            topbar.getTvTitle().setText(title);
        } else {
            topbar.getLeftIco().setVisibility(View.VISIBLE);
            topbar.getLeftIco().setImageResource(R.drawable.icon_back);
            topbar.getTvTitle().setText(title);
        }
        myWebChromeClient = new MyWebChromeClient(this, mProgressBar);
        initWebview(url);
    }

    public String getUrl(String token, String page) {
        String append = "?token=" + token + "&page=" + page;
        return NetContants.REPAYMENT + append;
    }

    public String getUrl(String token, String page, String id) {
        return getUrl(token, page) + "&applyId=" + id;
    }

    @SuppressLint("JavascriptInterface")
    private void initWebview(String url) {
        mWebView.loadUrl(url);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        mWebView.setWebViewClient(new MyWebViewClient(mProgressBar));
        mWebView.setWebChromeClient(myWebChromeClient);
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) { // 表示按返回键
                        mWebView.goBack(); // 后退
//                        getContext().finish();
                        return true; // 已处理
                    }
                }
                return false;
            }
        });
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
        myWebChromeClient.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
