package com.two.emergencylending.activity.LoginAndRegisterModule;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.BuryAction;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.IntentKey;
import com.two.emergencylending.constant.ManagerKey;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.IControllerCallBack;
import com.two.emergencylending.controller.LoginController;
import com.two.emergencylending.controller.MyQrCodeController;
import com.two.emergencylending.interfaces.FocusListener;
import com.two.emergencylending.manager.ActivityManager;
import com.two.emergencylending.manager.FocusManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.RegularExpUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.LockPassWordUtil;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：登录页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class LoginActivity extends BaseActivity implements Topbar.topbarClickListener, View.OnClickListener, IControllerCallBack, ControllerCallBack {

    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.et_userid)
    EditText etUserid;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.iv_show_pwd)
    ImageView iv_show_pwd;
    @Bind(R.id.tv_register)
    TextView register;
    @Bind(R.id.btn_login)
    Button btnLogin;
    private int from = 0;
    /**
     * 用户名和密码
     */
    private String _ed_userid;
    private String _ed_password;
    LoginController login;

    @Override
    public int setContent() {
        return R.layout.activity_login;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        try {
            from = getIntent().getIntExtra(IntentKey.FROM, 0);
            if (from == 1) {
                register.setVisibility(View.INVISIBLE);
            } else {
                register.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        login = new LoginController(this, this);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        etUserid.setOnFocusChangeListener(new FocusListener(BuryAction.MD_INPUT_LOGIN_USERID));
        etPassword.setOnFocusChangeListener(new FocusListener(BuryAction.MD_INPUT_LOGIN_PASSWORD));

    }

    @Override
    public void setData() {
        int GuestLogin =getIntent().getIntExtra(IntentKey.GuestLoginActivity,0);
        if (GuestLogin==3){
            IApplication.getInstance().clearUserInfo(getContext());
        }
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


    @OnClick({R.id.iv_show_pwd, R.id.btn_login, R.id.tv_forgetpwd, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_show_pwd:
                CommonUtils.changeEditeTextInputType(etPassword, iv_show_pwd);
                break;
            case R.id.btn_login:
                FocusManager.getFocus(btnLogin);
                login();
                break;
            case R.id.tv_forgetpwd:
                CommonUtils.goToActivity(getContext(), UpDatepwdActivity.class);
                break;
            case R.id.tv_register:
                CommonUtils.goToActivity(getContext(), RegisterActivity.class);
                break;
        }
    }

    public void login() {
        _ed_userid = etUserid.getText().toString().trim();
        _ed_password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(_ed_userid)) {
            ToastAlone.showLongToast(getContext(), "手机号不能为空!");
            return;
        }
        if (_ed_userid.length() < 11) {
            ToastAlone.showLongToast(getContext(), "手机号码有误!");
            return;
        }
        if (TextUtils.isEmpty(_ed_password)) {
            ToastAlone.showLongToast(getContext(), "密码不能为空!");
            return;
        }
        if (_ed_password.length() < 6) {
            ToastAlone.showLongToast(getContext(), "密码至少6位!");
            return;
        }
        if (!RegularExpUtil.pwd(_ed_password)) {
            ToastAlone.showLongToast(getContext(), "请输入正确的密码!");
            return;
        }
        if (CommonUtils.isNetAvailable()) {
            CommonUtils.showDialog(LoginActivity.this, "正在努力加载中....");
                login.login(_ed_userid, _ed_password , true);
        }
    }

    @Override
    public void leftClick() {
        SharedPreferencesUtil.getInstance(getContext()).setBoolean("isLoginCancel", true);
        finish();
    }

    @Override
    public void rightClick() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SharedPreferencesUtil.getInstance(getContext()).setBoolean("isLoginCancel", true);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                case 2:
                    login.login(_ed_userid, _ed_password, true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.LOGIN) {
            CommonUtils.closeDialog();
            if (getIntent().getIntExtra("login", 1) == 0)
                IApplication.isFinish = true;
            new MyQrCodeController().getMyCode(this,null);
            UserInfoManager.getInstance().setLogin(true);
            if (getIntent().getIntExtra(IntentKey.FROM, 0) == 1) {
//                CommonUtils.goToActivity(LoginActivity.this, PerfectInformationActivity.class);
                ActivityManager.getInstance().finishAllActivitys(ManagerKey.ACTIVITY_TEMP);
            }
            LockPassWordUtil.setLogin(getContext(), false);
            LockPassWordUtil.clearPassWork(getContext());
            iApplication.isRefresh = true;
            finish();
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.LOGIN) {
            CommonUtils.closeDialog();
        }
    }

    @Override
    public void onFail(String errorMessage) {
        CommonUtils.closeDialog();
        Message msg = new Message();
        msg.what = 1;
        msg.obj = errorMessage;
        handler.sendMessage(msg);
    }
}
