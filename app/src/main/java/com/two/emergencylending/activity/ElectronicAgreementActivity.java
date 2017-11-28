package com.two.emergencylending.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ProgressBar;

import com.chinazyjr.lib.util.ToastUtils;
import com.example.getlimtlibrary.builder.utils.MyLog;
import com.zyjr.emergencylending.R;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.ElectronicAgreementController;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.X5WebView;
import com.two.emergencylending.utils.X5WebViewUtil;
import com.two.emergencylending.view.Topbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

/**
 * 项目名称：急借通
 * 类描述：签订电子合同
 * 创建人：szx
 * 创建时间：2016/8/29.
 * 修改人：szx
 * 修改时间：2016/8/29.
 * 修改备注：
 */
public class ElectronicAgreementActivity extends BaseActivity implements Topbar.topbarClickListener, ControllerCallBack {
    @Bind(R.id.topbar)
    Topbar topbar;
    X5WebView mWebView;
    ProgressBar mProgressBar;
    ElectronicAgreementController electronicAgreementController;

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
        electronicAgreementController = new ElectronicAgreementController(this, this);
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
//        topbar.getLeftIco().setVisibility(View.VISIBLE);
//        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        mWebView = (X5WebView) findViewById(R.id.webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
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
        if (electronicAgreementController == null) {
            electronicAgreementController = new ElectronicAgreementController(this, this);
        }
        electronicAgreementController.getElectronicAgreementUrl();
    }

    @Override
    public void destroy() {
        if (mWebView != null) {//销毁掉webView
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.setVisibility(View.GONE);
            long timeout = ViewConfiguration.getZoomControlsTimeout();//timeout ==3000
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    MyLog.i("keey", "销毁");
                    try {
                        mWebView.destroy();
                    } catch (Exception e) {
                    }
                }
            }, timeout);
        }
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

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.ELECTRONIC_AGREEMENT) {
            try {
                JSONObject json = new JSONObject(value);
                String url = json.get("sign_link").toString();
                X5WebViewUtil.getInstance().initWebview(this, mWebView, mProgressBar, url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = errorMessage;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
}
