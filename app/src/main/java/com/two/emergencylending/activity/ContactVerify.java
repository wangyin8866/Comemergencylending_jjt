package com.two.emergencylending.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinazyjr.lib.util.ToastUtils;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.ContactListModel;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.controller.ContactController;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.GetContactListController;
import com.two.emergencylending.controller.GetUserContactList;
import com.two.emergencylending.permission.AppOpsManagerUtil;
import com.two.emergencylending.permission.ToolPermission;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.RegularExpUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 项目名称：jijietong1.18
 * 类描述：
 * 创建人：szx
 * 创建时间：2017/6/19 17:14
 * 修改人：szx
 * 修改时间：2017/6/19 17:14
 * 修改备注：
 */
public class ContactVerify extends BaseActivity implements Topbar.topbarClickListener, ControllerCallBack, View.OnClickListener {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.tv_contact_1)
    TextView tv_contact_1;
    @Bind(R.id.tv_contact_2)
    TextView tv_contact_2;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.ll_phone)
    RelativeLayout ll_phone;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.rg_contacts)
    RadioGroup radioGroup;
    ContactController contactController;
    GetContactListController pullContactList;
    GetUserContactList getContactList;
    List<ContactListModel> contactList;

    private boolean isFirstContact = true;

    @Override
    public int setContent() {
        return R.layout.activity_contact_verify;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        radioGroup.check(R.id.rb_contacts_1);
        contactList = new ArrayList<ContactListModel>();
        pullContactList = new GetContactListController(this);
        getContactList = new GetUserContactList(this, this);
        contactController = new ContactController(this, this);
        getContactList.getContactList();
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
        btn_next.setOnClickListener(this);
        ll_phone.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_contacts_1) {
//                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rb_contacts_2) {
//                }
            }
        });
    }

    @Override
    public void resume() {

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

    private void goToSelectPhone() {
        if (ToolPermission.checkSelfPermission(this, null, Manifest.permission.READ_CONTACTS, "从电话薄中选择联系人,请允许读取权限!", 123)) {
            if (CommonUtils.queryContactPhoneNumber(IApplication.gainContext()).size() > 0)
                startActivityForResult(CommonUtils.goToSelectPhone(), 1234);
            else
                ToastAlone.showLongToast(this, "请添加联系人或检查权限");
            if (contactList != null)
                pullContactList.contactList();
        } else {
            if (AppOpsManagerUtil.isCheck(this, AppOpsManagerUtil.OP_READ_CONTACTS) == AppOpsManager.MODE_IGNORED) {
                ToastAlone.showLongToast(this, "请打开读取联系人权限!");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1234 == requestCode && resultCode == Activity.RESULT_OK) {
            String phoneNo = CommonUtils.getPhoneNo(this, resultCode, data);
            if (TextUtils.isEmpty(phone(phoneNo)))
                return;
            phone.setText(phone(phoneNo));
        }
    }

    private String phone(String phone) {
        if (StringUtil.isNotEmpty(phone)) {
            if (phone.contains(" ")) {
                phone = phone.replace(" ", "").toString();
            }
            if (phone.contains("-")) {
                phone = phone.replace("-", "").toString();
            }
            if (phone.length() > 11) {
                phone = phone.substring(phone.length() - 11);
            }
            if (phone.length() != 11) {
                ToastAlone.showShortToast(this, "联系人电话不合法!");
                phone = "";
                return phone;
            } else if (!(RegularExpUtil.isMobile(phone))) {
                ToastAlone.showShortToast(this, "联系人电话不合法!");
                phone = "";
            }
            return phone;
        } else {
            return "";
        }
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.GET_CONTACT_LIST) {
            contactList = UserInfoManager.getInstance().getContactListModels();
            if (contactList.size() >= 2) {
                tv_contact_1.setText(contactList.get(0).getContactName());
                tv_contact_2.setText(contactList.get(1).getContactName());
            }
        } else if (returnCode == CallBackType.CHECK_CONTACTS_SAVE) {
            CommonUtils.goToActivity(this, ElectronicAgreementActivity.class);
            finish();
        }
    }

    @Override
    public void onFail(int returnCode, String errorMessage) {
        if (returnCode == CallBackType.GET_CONTACT_LIST || returnCode == CallBackType.CHECK_CONTACTS_SAVE) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_phone:
                goToSelectPhone();
                break;
            case R.id.btn_next:
                if (StringUtil.isNullOrEmpty(phone.getText().toString())) {
                    ToastAlone.showShortToast(this, "请选择对应的联系人手机号!");
                    return;
                }
                String phoneNum = "";
                if (contactList.size() >= 2) {
                    if (radioGroup.getCheckedRadioButtonId() == R.id.rb_contacts_1) {
                        phoneNum = contactList.get(0).getContactPhone().trim();
                    } else if (radioGroup.getCheckedRadioButtonId() == R.id.rb_contacts_2) {
                        phoneNum = contactList.get(1).getContactPhone().trim();
                    }
                    if (phoneNum.equals(phone.getText().toString().trim())) {
                        contactController.checkContactsSave();
                    } else {
                        ToastAlone.showShortToast(this, "联系人验证未通过，请选择正确的手机号!");
                    }
                } else {
                    ToastAlone.showShortToast(this, "联系人获取异常，请重新获取!");
                }
                break;
        }
    }
}
