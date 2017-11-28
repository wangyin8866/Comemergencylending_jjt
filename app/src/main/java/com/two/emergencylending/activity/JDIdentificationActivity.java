package com.two.emergencylending.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.RegularExpUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：京东认证页面
 * 创建人：wyp
 * 创建时间：2016/9/6 16:09
 * 修改人：wyp
 * 修改时间：2016/9/6 16:09
 * 修改备注：
 */
public class JDIdentificationActivity extends BaseActivity implements Topbar.topbarClickListener, CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.jd_credentials_topbar)
    Topbar topbar;
    @Bind(R.id.jd_account)
    EditText jdAccount;
    @Bind(R.id.jd_pwd)
    EditText jdPwd;
    @Bind(R.id.cb_check)
    CheckBox cbCheck;
    @Bind(R.id.ll_code)
    LinearLayout ll_code;
    private String account, pwd, code;
    private boolean isSelected = true;
    Map parameter;
    private boolean isCAPTCHA = false;
    @Bind(R.id.jing_dong_code)
    EditText jing_dong_code;

    @Override
    public int setContent() {
        return R.layout.edit_certification_jd;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        parameter = new HashMap();
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        cbCheck.setOnCheckedChangeListener(this);
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


    @OnClick({R.id.tv_agreement_register, R.id.btn_jd_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement_register:
                CommonUtils.goToActivity(getContext(), JingDongProtocolActivity.class);
                break;
            case R.id.btn_jd_confirm:
                btn_jd_confirm();
                break;
        }
    }

    public void btn_jd_confirm() {
        account = jdAccount.getText().toString().trim();
        pwd = jdPwd.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastAlone.showLongToast(getContext(), "京东账号不能为空!");
            return;
        }
        if (!RegularExpUtil.isMobile(account)) {
            ToastAlone.showLongToast(getContext(), "请填写正确的京东账号!");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastAlone.showLongToast(getContext(), "京东认证密码不能为空!");
            return;
        }
        if (!RegularExpUtil.pwd(pwd)) {
            ToastAlone.showLongToast(getContext(), "请填写正确的京东认证密码!");
            return;
        }
        if (!isSelected) {
            ToastAlone.showToast(getContext(), "请同意协议！", Toast.LENGTH_SHORT);
            return;
        }
        if (isCAPTCHA) {
            code = jing_dong_code.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                ToastAlone.showToast(this, "请输入您的验证码!", 0);
                return;
            }
            if (code.length() < 6) {
                ToastAlone.showLongToast(this, "验证码不正确!");
                return;
            }
        }
        authenTicate();
    }

    public void authenTicate() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("account", account);
        parameter.put("password", pwd);
        if (!TextUtils.isEmpty(code)) {
            parameter.put("captcha", code);
            parameter.put("type", 2);
        } else {
            parameter.put("type", 1);
        }
        parameter.put("auth_type", 2);
        if (!TextUtils.isEmpty(UserInfoManager.getInstance().getJunxinlin())) {
            parameter.put("huizhijilu", UserInfoManager.getInstance().getJunxinlin());
        }
        LogUtil.d("JDRequest", parameter.toString().trim());
        CommonUtils.showDialog(JDIdentificationActivity.this, "正在努力加载....");


        OKManager.getInstance().sendComplexForm(NetContants.JUXINLI_AUTH, parameter, new OKManager.Func1() {


            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                LogUtil.d("JDResponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.optString("msg");
                    String flag = jsonObject.optString("flag");
                    if (flag.equals("0000")) {
                        ToastAlone.showLongToast(JDIdentificationActivity.this, msg);
                        iApplication.isRefresh = true;
                        finish();
                    } else if (flag.equals("00040004")) {
                        String junxinlin = jsonObject.optString("result");
//                        UserInfoManager.getInstance().setJunxinlin(junxinlin);
                        ToastAlone.showLongToast(JDIdentificationActivity.this, msg);
                        isCAPTCHA = true;
                        ll_code.setVisibility(View.VISIBLE);

                    } else if (flag.equals("0004")) {
                        ToastAlone.showLongToast(JDIdentificationActivity.this, msg);
                    } else if (flag.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(JDIdentificationActivity.this);
                    } else {
                        ToastAlone.showLongToast(JDIdentificationActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                CommonUtils.closeDialog();
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            isSelected = true;
        } else {
            isSelected = false;
        }
    }
}
