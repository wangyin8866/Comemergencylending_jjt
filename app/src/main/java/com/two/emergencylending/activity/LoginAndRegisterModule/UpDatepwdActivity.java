package com.two.emergencylending.activity.LoginAndRegisterModule;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.controller.AuthCodeController;
import com.two.emergencylending.controller.IControllerCallBack;
import com.two.emergencylending.controller.UpdatePwdController;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.RegularExpUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：验证并设置新密码
 * 创建人：wyp
 * 创建时间：2016/8/11 14:49
 * 修改人：wyp
 * 修改时间：2016/8/11 14:49
 * 修改备注：
 */
public class UpDatepwdActivity extends BaseActivity implements Topbar.topbarClickListener, View.OnClickListener {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.et_mobile)
    EditText etMobile;
    @Bind(R.id.et_figure_code)
    EditText et_figure_code;
    @Bind(R.id.btn_figure_code)
    ImageView btn_figure_code;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_code)
    Button btnCode;
    @Bind(R.id.et_setpwd)
    EditText et_setpwd;
    @Bind(R.id.et_enternewpwdagain)
    EditText et_enternewpwdagain;
    AuthCodeController authCodeController;
    String mobile, code, pwd, againpwd;
    UpdatePwdController updatePwd;
    private String uuid;
    private DisplayImageOptions options;

    @Override
    public int setContent() {
        return R.layout.activity_updatepwd;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    IControllerCallBack passwordCall = new IControllerCallBack() {
        @Override
        public void onSuccess(int returnCode, String value) {
            if (getIntent().getIntExtra("toLogin", 1) == 0) {
                CommonUtils.goToActivity(getContext(), LoginActivity.class);
            }
            finish();
            ToastAlone.showLongToast(IApplication.globleContext, "密码修改成功");

        }

        @Override
        public void onFail(final String errorMessage) {
            getContext().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastAlone.showLongToast(getContext(), errorMessage);
                }
            });

        }
    };

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        refreshGraphicalVerificationCode();
        if (updatePwd == null)
            updatePwd = new UpdatePwdController(this, passwordCall);
        authCodeController = new AuthCodeController(getContext(), this, btnCode);
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

    private void updatePwd() {
        mobile = etMobile.getText().toString().trim();
        code = etCode.getText().toString().trim();
        pwd = et_setpwd.getText().toString().trim();
        againpwd = et_enternewpwdagain.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastAlone.showLongToast(getContext(), "手机号不能为空!");
            return;
        }
        if (mobile.length() < 11) {
            ToastAlone.showLongToast(getContext(), "手机号码有误!");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastAlone.showToast(getContext(), "验证码不能为空!", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastAlone.showToast(getContext(), "新的密码不能空！", Toast.LENGTH_SHORT);
            return;
        }

        if (pwd.length() < 6) {
            ToastAlone.showToast(getContext(), "请设置长度6-16位密码!", Toast.LENGTH_SHORT);
            return;
        }
        if (!RegularExpUtil.pwd(pwd)) {
            ToastAlone.showLongToast(getContext(), "密码格式有误!");
            return;
        }
        if (TextUtils.isEmpty(againpwd)) {
            ToastAlone.showToast(getContext(), "密码不能空！", Toast.LENGTH_SHORT);
            return;
        }

        if (againpwd.length() < 6) {
            ToastAlone.showToast(getContext(), "请设置长度6-16位密码!", Toast.LENGTH_SHORT);
            return;
        }
        if (!(pwd.equals(againpwd))) {
            ToastAlone.showLongToast(getContext(), "两次输入密码不一致!");
            return;
        }
        updatePwd.updatePassword(mobile, code, againpwd);
    }

    @OnClick({R.id.btn_code, R.id.btn_sure, R.id.btn_figure_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
//                if (StringUtil.isNullOrEmpty(et_figure_code.getText().toString())) {
//                    ToastAlone.showToast(getContext(), "请填写随机码!", Toast.LENGTH_LONG);
//                } else {
//                    sendCode(et_figure_code.getText().toString());
//                }
                sendCode("");
                break;
            case R.id.btn_sure:
                updatePwd();
                break;
            case R.id.btn_figure_code:
                refreshGraphicalVerificationCode();
                break;
        }
    }


    private void sendCode(String verifyCode) {
        mobile = etMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastAlone.showLongToast(getContext(), "手机号不能为空!");
            return;
        }
        if (mobile.length() < 11) {
            ToastAlone.showLongToast(getContext(), "请填写正确的手机号码!");
            return;
        }
        authCodeController.getAuthCode(authCodeController.MSG_FIND_PASSWORD_, mobile);
    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

    public void refreshGraphicalVerificationCode() {
        uuid = UUID.randomUUID().toString();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_banner)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_banner) // 设置图片Uri为空的时候显示的图片
                .showImageOnFail(R.drawable.default_banner) // 设置图片失败的时候显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();
        String url = NetContants.GRAPHICAL_VERIFICATION_CODE + "?length=4&uuid=" + uuid + "&fontSize=28&characters=abcdetx2345678&random=0.6286913563047576";
        ImageLoader.getInstance().displayImage(url, btn_figure_code, options);
    }
}
