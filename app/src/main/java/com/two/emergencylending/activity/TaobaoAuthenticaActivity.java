package com.two.emergencylending.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.RegularExpUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：淘宝认证
 * 创建人：wyp
 * 创建时间：2016/8/18. 10:11
 * 修改人：wyp
 * 修改时间：2016/8/18. 10:11
 * 修改备注：
 */
public class TaobaoAuthenticaActivity extends BaseActivity implements Topbar.topbarClickListener, CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.tv_agreement_register)
    TextView tv_agreement_register;
    @Bind(R.id.tb_credentials_topbar)
    Topbar tb_credentials_topbar;
    @Bind(R.id.tb_account)
    EditText tbAccount;
    @Bind(R.id.tb_pwd)
    EditText tbPwd;
    @Bind(R.id.cb_check)
    CheckBox cbCheck;
    private boolean isSelected = false;
    private String tb_account, tb_pwd;

    @Override
    public int setContent() {
        return R.layout.layout_taobao_authentica;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        tb_credentials_topbar.getLeftIco().setImageResource(View.VISIBLE);
        tb_credentials_topbar.getLeftIco().setImageResource(R.drawable.icon_back);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        tb_credentials_topbar.setOnTopbarClickListener(this);
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


    @OnClick({R.id.tv_agreement_register, R.id.btn_tb_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement_register:
                CommonUtils.goToActivity(getContext(), TaobaoProtocolActivity.class);
                break;
            case R.id.btn_tb_confirm:
                tb_confirm();
                break;
        }
    }

    private void tb_confirm() {
        tb_account = tbAccount.getText().toString().trim();
        tb_pwd = tbPwd.getText().toString().trim();
        if (TextUtils.isEmpty(tb_account)) {
            ToastAlone.showLongToast(getContext(), "淘宝账号不能为空!");
            return;
        }
        if (!RegularExpUtil.isMobile(tb_account)) {
            ToastAlone.showLongToast(getContext(), "请填写正确的淘宝账号!");
            return;
        }
        if (TextUtils.isEmpty(tb_pwd)) {
            ToastAlone.showLongToast(getContext(), "淘宝认证密码不能为空!");
            return;
        }
        if (!RegularExpUtil.pwd(tb_pwd)) {
            ToastAlone.showLongToast(getContext(), "请填写正确的淘宝认证密码!");
            return;
        }
        if (!isSelected) {
            ToastAlone.showToast(getContext(), "请同意协议！", Toast.LENGTH_SHORT);
            return;
        }
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
