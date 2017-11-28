package com.two.emergencylending.activity.LoginAndRegisterModule;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chinazyjr.lib.util.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.two.emergencylending.activity.AuthorizationAgreementActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.BuryAction;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.controller.AuthCodeController;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.RegisterController;
import com.two.emergencylending.interfaces.FocusListener;
import com.two.emergencylending.manager.BuryPointManager;
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
 * 类描述：注册页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class RegisterActivity extends BaseActivity implements Topbar.topbarClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, ControllerCallBack {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.et_register_idcard)
    EditText registeriIdcard;
    @Bind(R.id.ll_register_idcard)
    LinearLayout ll_register_idcard;
    @Bind(R.id.ll_invitation_code)
    LinearLayout ll_invitation_code;
    @Bind(R.id.et_mobile)
    EditText etMobile;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_code)
    Button btnCode;
    @Bind(R.id.et_setpwd)
    EditText etSetpwd;
    @Bind(R.id.cb_check)
    CheckBox cbCheck;
    @Bind(R.id.et_invitationcode)
    EditText etinvitationcode;
    @Bind(R.id.et_figure_code)
    EditText et_figure_code;
    @Bind(R.id.btn_figure_code)
    ImageView btn_figure_code;
    @Bind(R.id.img_pwd_encrypt)
    ImageView img_pwd_encrypt;
    @Bind(R.id.staff)
    RadioGroup radioGroup;
    private boolean isSelected = true;
    RegisterController registerController;
    AuthCodeController authCodeController;
    private String idCard, mobile, code, pwd, invitationcode;

    private String uuid;
    private DisplayImageOptions options;

    private void registerMD() {
        etMobile.setOnFocusChangeListener(new FocusListener(BuryAction.MD_INPUT_REGISTER_USERID));//手机号
        etCode.setOnFocusChangeListener(new FocusListener(BuryAction.MD_INPUT_REGISTER_VERIFY_CPDE));//验证码 
        etSetpwd.setOnFocusChangeListener(new FocusListener(BuryAction.MD_INPUT_REGISTER_PASSWORD));//密码
        etinvitationcode.setOnFocusChangeListener(new FocusListener(BuryAction.MD_INPUT_REGISTER_INVITE));//邀请码
    }

    @Override
    public int setContent() {
        return R.layout.activity_register;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        registerController = new RegisterController(this, this);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        authCodeController = new AuthCodeController(this, this, btnCode);
//        refreshGraphicalVerificationCode();
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        cbCheck.setChecked(isSelected);
        radioGroup.check(R.id.staff_ordinary);
//        if (!SharedPreferencesUtil.getInstance(RegisterActivity.this).getBoolean(SPKey.IS_FIRST_REGISTER, false)) {
//            SharedPreferencesUtil.getInstance(RegisterActivity.this).setBoolean(SPKey.IS_FIRST_REGISTER, true);
//            CommonUtils.goToActivity(RegisterActivity.this, RegisterGuideActivity.class);
//        }
        initStaff();
        registerMD();
    }

    @Override
    public void setData() {
    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        cbCheck.setOnCheckedChangeListener(this);
        btn_figure_code.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                initStaff();
            }
        });
    }

    @Override
    public void resume() {
        BuryPointManager.buryActivityBegin(BuryAction.MD_AVTIVITY_REGISTER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BuryPointManager.buryActivityEnd(BuryAction.MD_AVTIVITY_REGISTER);
    }

    @Override
    public void destroy() {
    }


    @OnClick({R.id.btn_code, R.id.btn_register, R.id.img_pwd_encrypt, R.id.tv_agreement_register, R.id.tv_agreement_infomation, R.id.btn_figure_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
//                if (StringUtil.isNullOrEmpty(et_figure_code.getText().toString())) {
//                    ToastAlone.showToast(getContext(), "请填写随机码!", Toast.LENGTH_LONG);
//                } else {
//                    codeVerfity(et_figure_code.getText().toString());
//                }
                codeVerfity("");
                break;
            case R.id.btn_figure_code:
//                refreshGraphicalVerificationCode();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.img_pwd_encrypt:
                CommonUtils.changeEditeTextInputType(etSetpwd, img_pwd_encrypt);
                break;
            case R.id.tv_agreement_register:
                CommonUtils.goToActivity(getContext(), RegisterProtocolActivity.class);
                break;
            case R.id.tv_agreement_infomation:
                CommonUtils.goToActivity(getContext(), AuthorizationAgreementActivity.class);
                break;
        }
    }


    private void register() {
        idCard = registeriIdcard.getText().toString().trim();
        mobile = etMobile.getText().toString();
        code = etCode.getText().toString().trim();
        pwd = etSetpwd.getText().toString().trim();
        invitationcode = etinvitationcode.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastAlone.showToast(getContext(), "请输入手机号码!", Toast.LENGTH_LONG);
            return;
        }
        if (mobile.length() < 11) {
            ToastAlone.showLongToast(getContext(), "请输入正确的手机号码!");
            return;
        }
        if (TextUtils.isEmpty(idCard) && radioGroup.getCheckedRadioButtonId() == R.id.staff_offline) {
            ToastAlone.showToast(getContext(), "请输入身份证号码!", Toast.LENGTH_LONG);
            return;
        }
        if (radioGroup.getCheckedRadioButtonId() == R.id.staff_offline && !RegularExpUtil.isIDCard(getContext(), idCard)) {
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastAlone.showToast(getContext(), "请输入验证码!", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastAlone.showToast(getContext(), "请输入密码！", Toast.LENGTH_SHORT);
            return;
        }
        if (!RegularExpUtil.pwd(pwd)) {
            ToastAlone.showLongToast(getContext(), "请输入只含字母和数字的密码格式!");
            return;
        }
        if (pwd.length() < 6) {
            ToastAlone.showToast(getContext(), "请设置长度6-16位密码!", Toast.LENGTH_SHORT);
            return;
        }
        if (!isSelected) {
            ToastAlone.showToast(getContext(), "请同意协议！", Toast.LENGTH_SHORT);
            return;
        }
        CommonUtils.showDialog(RegisterActivity.this, "努力加载中...");
        if (radioGroup.getCheckedRadioButtonId() == R.id.staff_ordinary) {
            registerController.register(false, mobile, code, pwd, invitationcode);
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.staff_offline) {
            registerController.registerRenew(false, mobile, idCard, code, pwd);
        }
    }

    private void codeVerfity(String verifyCode) {
        mobile = etMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastAlone.showLongToast(getContext(), "请输入手机号码!");
            return;
        }
        if (mobile.length() < 11) {
            ToastAlone.showLongToast(getContext(), "请输入正确的手机号码!");
            return;
        }
        authCodeController.getAuthCode(authCodeController.MSG_REG_, mobile);
    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isSelected = true;
        } else {
            isSelected = false;
        }
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.REGISTER) {
            CommonUtils.closeDialog();
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.REGISTER) {
            CommonUtils.closeDialog();
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        }
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

    public void initStaff() {
        if (radioGroup.getCheckedRadioButtonId() == R.id.staff_ordinary) {
            ll_register_idcard.setVisibility(View.GONE);
            ll_invitation_code.setVisibility(View.VISIBLE);
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.staff_offline) {
            ll_register_idcard.setVisibility(View.VISIBLE);
            ll_invitation_code.setVisibility(View.GONE);
        }
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
        btn_figure_code.setImageBitmap(null);
        ImageLoader.getInstance().displayImage(url, btn_figure_code, options);
    }

}
