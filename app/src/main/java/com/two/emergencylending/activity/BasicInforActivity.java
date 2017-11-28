package com.two.emergencylending.activity;

import android.content.Intent;
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
import com.two.emergencylending.activity.LoginAndRegisterModule.LoginActivity;
import com.two.emergencylending.activity.LoginAndRegisterModule.RegisterProtocolActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.BuryAction;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.IntentKey;
import com.two.emergencylending.constant.ManagerKey;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.controller.AuthCodeController;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.LoginController;
import com.two.emergencylending.controller.RegisterController;
import com.two.emergencylending.interfaces.FocusListener;
import com.two.emergencylending.manager.ActivityManager;
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
 * 类描述：首页注册页面
 * 创建人：szx
 * 创建时间：2016/8/8 14:49
 * 修改人：szx
 * 修改时间：2016/8/8 14:49
 * 修改备注：
 */
public class BasicInforActivity extends BaseActivity implements Topbar.topbarClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, ControllerCallBack {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.ed_basic_idcard)
    EditText edBasicIdcard;
    @Bind(R.id.ed_basic_num)
    EditText edBasicNum;
    @Bind(R.id.et_figure_code)
    EditText et_figure_code;
    @Bind(R.id.btn_figure_code)
    ImageView btn_figure_code;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.et_basin_password)
    EditText etBasinPassword;
    @Bind(R.id.ed_invitation_code)
    EditText edInvitationCode;
    @Bind(R.id.img_pwd_encrypt)
    ImageView imgPwdEncrypt;
    @Bind(R.id.cb_check)
    CheckBox cbCheck;
    @Bind(R.id.btn_basic_code)
    Button btnCode;
    @Bind(R.id.ll_basic_idcard)
    LinearLayout ll_basic_idcard;
    @Bind(R.id.ll_invitation_code)
    LinearLayout ll_invitation_code;
    @Bind(R.id.staff)
    RadioGroup radioGroup;
    private boolean isSelected = true;
    private String basic_idcard;
    private String basic_num;
    private String code;
    private String basinPassword;
    private String invitationCode;
    RegisterController registerController;
    LoginController loginController;
    AuthCodeController authCodeController;
    private static final int REQUEST_REGISTER = 11;
    private static final int REQUEST_LOGIN = 12;
    private int requestType = 0;

    private String uuid;
    private DisplayImageOptions options;

    private void basicMD() {
        edBasicNum.setOnFocusChangeListener(new FocusListener(BuryAction.MD_INPUT_REGISTER_USERID));//手机码
        etCode.setOnFocusChangeListener(new FocusListener(BuryAction.MD_INPUT_REGISTER_VERIFY_CPDE));//验证码
        etBasinPassword.setOnFocusChangeListener(new FocusListener(BuryAction.MD_INPUT_REGISTER_PASSWORD));//密码
        edInvitationCode.setOnFocusChangeListener(new FocusListener(BuryAction.MD_INPUT_REGISTER_INVITE));//邀请码
    }

    @Override
    public int setContent() {
        return R.layout.activity_basic_infor;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        ActivityManager.getInstance().inputActivity(ManagerKey.ACTIVITY_TEMP, this);
        registerController = new RegisterController(this, this);
        authCodeController = new AuthCodeController(this, this, btnCode);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        cbCheck.setChecked(isSelected);
        radioGroup.check(R.id.staff_ordinary);
        refreshGraphicalVerificationCode();
//        if (!SharedPreferencesUtil.getInstance(BasicInforActivity.this).getBoolean(SPKey.IS_FIRST_BASIC_INFO, false)) {
//            SharedPreferencesUtil.getInstance(BasicInforActivity.this).setBoolean(SPKey.IS_FIRST_BASIC_INFO, true);
//            CommonUtils.goToActivity(BasicInforActivity.this, BasicInfoGuideActivity.class);
//        }
        initStaff();
        basicMD();
    }

    @Override
    public void setData() {
    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        cbCheck.setOnCheckedChangeListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                initStaff();
            }
        });
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
        BuryPointManager.buryActivityBegin(BuryAction.MD_AVTIVITY_REGISTER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BuryPointManager.buryActivityEnd(BuryAction.MD_AVTIVITY_REGISTER);
    }

    @Override
    public void destroy() {

        ActivityManager.getInstance().removeActivity(ManagerKey.ACTIVITY_TEMP, this);
    }


    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }


    @OnClick({R.id.btn_basic_code, R.id.img_pwd_encrypt, R.id.tv_agreement_register, R.id.btn_save, R.id.tv_existingaccount, R.id.tv_agreement_infomation, R.id.btn_figure_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_basic_code:
//                if (StringUtil.isNullOrEmpty(et_figure_code.getText().toString())) {
//                    ToastAlone.showToast(getContext(), "请填写随机码!", Toast.LENGTH_LONG);
//                } else {
//                    codeVerfity(et_figure_code.getText().toString());
//                }
                codeVerfity(et_figure_code.getText().toString());
                break;
            case R.id.btn_figure_code:
                refreshGraphicalVerificationCode();
                break;
            case R.id.img_pwd_encrypt:
                CommonUtils.changeEditeTextInputType(etBasinPassword, imgPwdEncrypt);
                break;
            case R.id.tv_agreement_register:
                CommonUtils.goToActivity(getContext(), RegisterProtocolActivity.class);
                break;
            case R.id.btn_save:
                nextStep();
                break;
            case R.id.tv_agreement_infomation:
                CommonUtils.goToActivity(getContext(), AuthorizationAgreementActivity.class);
                break;
            case R.id.tv_existingaccount: {
                Intent intent = new Intent();
                intent.putExtra(IntentKey.FROM, 1);
                CommonUtils.goToActivity(getContext(), LoginActivity.class, intent);
                break;
            }
        }
    }

    public void nextStep() {
        basic_idcard = edBasicIdcard.getText().toString().trim();
        basic_num = edBasicNum.getText().toString().trim();
        code = etCode.getText().toString().trim();
        basinPassword = etBasinPassword.getText().toString().trim();
        invitationCode = edInvitationCode.getText().toString().trim();
        if (TextUtils.isEmpty(basic_num)) {
            ToastAlone.showLongToast(getContext(), "请输入手机号码!");
            return;
        }
        if (basic_num.length() < 11) {
            ToastAlone.showLongToast(getContext(), "请输入正确的手机号码!");
            return;
        }
        if (TextUtils.isEmpty(basic_idcard) && radioGroup.getCheckedRadioButtonId() == R.id.staff_offline) {
            ToastAlone.showToast(getContext(), "请输入身份证号码!", Toast.LENGTH_LONG);
            return;
        }
        if (radioGroup.getCheckedRadioButtonId() == R.id.staff_offline && !RegularExpUtil.isIDCard(getContext(), basic_idcard)) {
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastAlone.showToast(getContext(), "请输入验证码!", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(basinPassword)) {
            ToastAlone.showToast(getContext(), "请输入密码！", Toast.LENGTH_SHORT);
            return;
        }
        if (basinPassword.length() < 6) {
            ToastAlone.showToast(getContext(), "请设置长度6-16位密码!", Toast.LENGTH_SHORT);
            return;
        }
        if (!RegularExpUtil.pwd(basinPassword)) {
            ToastAlone.showLongToast(getContext(), "请输入只含字母和数字的密码格式!");
            return;
        }
        if (!isSelected) {
            ToastAlone.showToast(getContext(), "请同意协议！", Toast.LENGTH_SHORT);
            return;
        }
        requestType = REQUEST_REGISTER;
        CommonUtils.showDialog(BasicInforActivity.this, "努力加载中...");
        if (radioGroup.getCheckedRadioButtonId() == R.id.staff_ordinary) {
            registerController.register(true, basic_num, code, basinPassword, invitationCode);
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.staff_offline) {
            registerController.registerRenew(true, basic_num, basic_idcard, code, basinPassword);
        }
    }

    private void codeVerfity(String verifyCode) {
        basic_num = edBasicNum.getText().toString().trim();
        if (TextUtils.isEmpty(basic_num)) {
            ToastAlone.showLongToast(getContext(), "请输入手机号码!");
            return;
        }
        if (basic_num.length() < 11) {
            ToastAlone.showLongToast(getContext(), "请输入正确的手机号码!");
            return;
        }
        authCodeController.getAuthCode(authCodeController.MSG_REG_, basic_num);
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
            finish();
//            CommonUtils.goToActivity(getContext(), PerfectInformationActivity.class);
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
            ll_basic_idcard.setVisibility(View.GONE);
            ll_invitation_code.setVisibility(View.VISIBLE);
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.staff_offline) {
            ll_basic_idcard.setVisibility(View.VISIBLE);
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
        ImageLoader.getInstance().displayImage(url, btn_figure_code, options);
    }
}
