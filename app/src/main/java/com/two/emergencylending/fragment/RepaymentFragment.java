package com.two.emergencylending.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.chinazyjr.lib.util.ToastUtils;
import com.two.emergencylending.activity.LoginAndRegisterModule.LoginActivity;
import com.two.emergencylending.activity.MainActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseFragment;
import com.two.emergencylending.bean.RepayBorrowInfoBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.H5PageKey;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.GetMyStatu;
import com.two.emergencylending.controller.MineBorrowCotroller;
import com.two.emergencylending.controller.RepayBorrowInfoController;
import com.two.emergencylending.controller.RepaymentControl;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.MyWebChromeClient;
import com.two.emergencylending.utils.MyWebViewClient;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by User on 2017/1/15.
 */

public class RepaymentFragment extends BaseFragment implements ControllerCallBack {
    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.ll_no_data)
    LinearLayout noData;
    @Bind(R.id.ll_repayment)
    LinearLayout repayment;

    private MyWebChromeClient myWebChromeClient;
    private MineBorrowCotroller mineBorrow;
    private String H5Token;
    private String H5Status;
    GetMyStatu getMyStatu;
    private RepayBorrowInfoController mRepayBorrow;
    private boolean isShow;//控制当前fragment是否显示

    @Override
    protected void lazyLoad() {

    }

    @Override
    public int setContent() {
        return R.layout.fragement_repayment;
    }

    @Override
    public void init() {
        topbar.getLeftIco().setVisibility(View.GONE);
        CommonUtils.setMiuiStatusBarDarkMode(getActivity(), true);
        getMyStatu = new GetMyStatu(getActivity(), this);
        myWebChromeClient = new MyWebChromeClient(getActivity(), mProgressBar);
        mineBorrow = new MineBorrowCotroller(getActivity(), this);
        mRepayBorrow = new RepayBorrowInfoController(getActivity(), this);
    }

    @Override
    public void setData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        isShow = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isShow = false;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void resume() {
        boolean isloginCancel = SharedPreferencesUtil.getInstance(getActivity()).getBoolean("isLoginCancel", false);
        if (isloginCancel) {
            SharedPreferencesUtil.getInstance(getActivity()).setBoolean("isLoginCancel", false);
            ((MainActivity) getActivity()).changeToHome();
        }
        if (IApplication.isToHome) {
            IApplication.isToHome = false;
            ((MainActivity) getActivity()).changeToHome();
        }
        if (IApplication.getInstance().isLogin()) {
            CommonUtils.goToActivity(getActivity(),
                    LoginActivity.class, new Intent().putExtra("from", 0));
        } else {
            if (StringUtil.isNullOrEmpty(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.ID_CARD)) || StringUtil.isNullOrEmpty(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME))) {
                if(getMyStatu == null){
                    getMyStatu = new GetMyStatu(getActivity(), this);
                }
                getMyStatu.getMystatu();
            } else {
                if (mineBorrow == null) {
                    mineBorrow = new MineBorrowCotroller(getActivity(), this);
                }
                if (mRepayBorrow == null) {
                    mRepayBorrow = new RepayBorrowInfoController(getActivity(), this);
                }
                mineBorrow.getMineBorrow("1", "20");
                mRepayBorrow.getBorrowInfo();
            }

        }
    }

    @Override
    public void destroy() {

    }

    public void repayment(String idCard, String phone, final String id) {
        RepaymentControl.getInstance(getActivity()).setOnCompleteListener(new RepaymentControl.OnCompleteListener() {
            @Override
            public void complete(int status, String msg, String token, String result) {
                try {
                    if (status == 0) {
                        JSONObject json = new JSONObject(result);
                        H5Token = token;
                        H5Status = json.get("userState").toString();
                        if (checkStatus(H5Status) && !StringUtil.isNullOrEmpty(H5Token)) {
                            showRepayment(id);
                        }
                    } else {
                        ToastUtils.showShort(getActivity(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        RepaymentControl.getInstance(getActivity()).requestRepayment(idCard, phone);
    }

    @Override
    public void onSuccess(int returnCode, String returnValue) {

        if (!isShow) {//防止fragment已经没有显示，但网络请求也回调了
            return;
        }
        try {
           if (returnCode == CallBackType.GET_USER_STATU) {
                mineBorrow.getMineBorrow("1", "20");
                mRepayBorrow.getBorrowInfo();
            } else if (returnCode == CallBackType.REPAYBORROW_INFO) {

                RepayBorrowInfoBean repayBorrowInfoBean= UserInfoManager.getInstance().getRepayBorrowInfo();

                if (repayBorrowInfoBean==null) {
                    showNodata();
                } else {
                    if (repayBorrowInfoBean.getBorrow_status()==4) {
//                        if (!CommonConstant.PRODUCT_FXDONLINE.equals(loan.getProduct_id())) {
                        if (StringUtil.isNullOrEmpty(H5Token)) {
                            if (StringUtil.isNullOrEmpty(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.ID_CARD))) {
                                ToastUtils.showShort(getActivity(), "加载失败,请重试！");
                                showNodata();
                                return;
                            }
                            if (StringUtil.isNullOrEmpty(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME))) {
                                ToastUtils.showShort(getActivity(), "加载失败,请重试！");
                                return;
                            }
                            repayment(SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.ID_CARD), SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME), repayBorrowInfoBean.getContract_code());
                        } else {
                            if (checkStatus(H5Status) && !StringUtil.isNullOrEmpty(H5Token)) {
                                showRepayment(repayBorrowInfoBean.getContract_code());
                            }
                        }
//                        } else {
////                            Intent i = new Intent();
////                            i.putExtra("FXDPRODUCTID", loan.getProduct_id());
////                            i.putExtra("repayment","hide");
////                            CommonUtils.goToActivity(getActivity(), RepaymentActivity.class, i);//我的还款
//                            showFXDRepayment("4");
//                        }
                    } else {
                        showNodata();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (!isShow) {//防止fragment已经没有显示，但网络请求也回调了
            return;
        }
        if (returnCode == CallBackType.GET_USER_STATU) {
            ToastAlone.showShortToast(IApplication.globleContext, "加载失败,请重试！");
        } else if (returnCode == CallBackType.MINE_BORROW) {
            ToastAlone.showShortToast(IApplication.globleContext, "加载失败,请重试！");
        }
    }

    public boolean checkStatus(String status) {
        switch (Integer.valueOf(status)) {
            case 0:
                ToastUtils.showShort(getActivity(), "借款仍在审核中");
                return false;
            case 1:
                return true;
            case 2:
                ToastUtils.showShort(getActivity(), "借款已结清");
                return false;
            case 9:
                ToastUtils.showShort(getActivity(), "借款未通过，请核对资料");
                return false;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        if (mWebView != null && mWebView.getParent() != null) {//销毁掉webView
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    public void showNodata() {
        Message msg = new Message();
        msg.what = 1;
        handler.sendMessage(msg);
    }

    public void showRepayment(String id) {
        Message msg = new Message();
        msg.what = 2;
        msg.obj = id;
        handler.sendMessage(msg);
    }

    public void showFXDRepayment(String id) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = id;
        handler.sendMessage(msg);
    }

    public void fxdRepayment() {
        url = NetContants.H5_FXDREPAYMETT + "&mobile_phone_=" + SharedPreferencesUtil.getInstance(getContext()).getString(SPKey.USERNAME);
        initWebview(url);
    }

    String url;

    public void loadRepayment(String id) {
        url = getUrl(H5Token, H5PageKey.REPAYMENT, SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.USERNAME));
        initWebview(url);
    }

    public String getUrl(String token, String page, String mobile) {
//        if (UserInfoManager.getInstance().getRepayBorrowInfo().getProduct_id() != null) {
//            if (CommonConstant.PRODUCT_FXDONLINE.equals(UserInfoManager.getInstance().getRepayBorrowInfo().getProduct_id())) {
//                return NetContants.H5_FXDREPAYMETT + mobile;
//            } else {
                String append = "?token=" + token + "&page=" + page;
                return NetContants.REPAYMENT + append;
//            }
//        } else {
//            return null;
//        }
    }

//    public String getUrl(String token, String page, String id) {
//        return getUrl(token, page) + "&applyId=" + id;
//    }

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
                        return true; // 已处理
                    }
                }
                return false;
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    repayment.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    repayment.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    loadRepayment(msg.obj.toString());
                    break;
                case 3:
                    repayment.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    fxdRepayment();
                    break;
                case 4:
                    break;
                default:
                    break;
            }
        }
    };
}
