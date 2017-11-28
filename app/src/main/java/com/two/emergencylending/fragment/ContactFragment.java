package com.two.emergencylending.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinazyjr.lib.util.ToastUtils;
import com.example.getlimtlibrary.builder.utils.MyLog;
import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseFragment;
import com.two.emergencylending.bean.BorrowInfoBean;
import com.two.emergencylending.bean.CodeBean;
import com.two.emergencylending.bean.ContactBean;
import com.two.emergencylending.bean.ContactListModel;
import com.two.emergencylending.bean.ContactModel;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.AppConfig;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.CommonConstant;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.ConfirmationOfLoanCotroller;
import com.two.emergencylending.controller.ContactController;
import com.two.emergencylending.controller.ControllerCallBack;
import com.two.emergencylending.controller.CustomerManagerDataController;
import com.two.emergencylending.controller.GetContactListController;
import com.two.emergencylending.controller.GetUserContactList;
import com.two.emergencylending.controller.OffLinewSubJXLController;
import com.two.emergencylending.manager.CustomerManagerManager;
import com.two.emergencylending.permission.AppOpsManagerUtil;
import com.two.emergencylending.permission.ToolPermission;
import com.two.emergencylending.popupwindow.OneSelectPopupWindow;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.CustomerDialog;
import com.two.emergencylending.utils.RegularExpUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToastAlone;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.two.emergencylending.constant.AppConfig.contactRelation1;
import static com.two.emergencylending.constant.AppConfig.contactRelation2;
import static com.zyjr.emergencylending.R.id.tv_relation2;


/**
 * 联系人
 */

public class ContactFragment extends BaseFragment implements View.OnClickListener, ControllerCallBack {
    @Bind(R.id.tv_relation1)
    TextView tv_relation1;
    @Bind(R.id.et_contact_name1)
    EditText etContactName1;
    @Bind(R.id.ll_contact1)
    LinearLayout llContact1;
    @Bind(R.id.iv_phone1)
    ImageView iv_phone1;
    @Bind(R.id.tv_job1_phone)
    EditText tv_job1_phone;
    @Bind(tv_relation2)
    TextView tvRelation2;
    @Bind(R.id.ll_contact_relation2)
    LinearLayout llContactRelation2;
    @Bind(R.id.et_contact_name2)
    EditText etContactName2;
    @Bind(R.id.tv_job2_phone)
    EditText tv_job2_phone;
    @Bind(R.id.iv_phone2)
    ImageView ivPhone2;
    @Bind(R.id.ll_contact2)
    LinearLayout llContact2;
    @Bind(R.id.ll_contact3)
    LinearLayout llContact3;
    @Bind(R.id.tv_relation3)
    TextView tvRelation3;
    @Bind(R.id.iv_relation3)
    ImageView ivRelation3;
    @Bind(R.id.et_contact_name3)
    EditText et_contact_name3;
    @Bind(R.id.tv_job3_phone)
    TextView tv_job3_phone;
    @Bind(R.id.iv_phone3)
    ImageView iv_phone3;
    @Bind(R.id.tv_relation4)
    TextView tvRelation4;
    @Bind(R.id.iv_relation4)
    ImageView ivRelation4;
    @Bind(R.id.ll_contact_relation4)
    LinearLayout llContactRelation4;
    @Bind(R.id.et_contact_name4)
    EditText etContactName4;
    @Bind(R.id.tv_job4_phone)
    TextView tv_job4_phone;
    @Bind(R.id.iv_phone4)
    ImageView ivPhone4;
    @Bind(R.id.ll_contact_phone4)
    LinearLayout llContactPhone4;
    @Bind(R.id.ll_contact4)
    LinearLayout llContact4;
    @Bind(R.id.view3)
    View view3;
    @Bind(R.id.view4)
    View view4;
    @Bind(R.id.btn_contact_previous)
    Button contact_previous;
    @Bind(R.id.btn_contact_sava)
    Button btn_contact_sava;
    @Bind(R.id.ll_contact_relation1)
    LinearLayout ll_contact_relation1;


    private int relationIndex = -1;
    String productId;
    String limit;
    String period;

    boolean isMarried;
    private List<ContactModel> contactModels = new ArrayList<>();
    private static ContactBean contactBean = new ContactBean();
    CustomerDialog dialog;

    ContactController contactController;
    ConfirmationOfLoanCotroller confirmationOfLoanCotroller;
    GetUserContactList getContactList;
    GetContactListController contactList;
    private CustomerManagerDataController customerManagerDataController;
    OffLinewSubJXLController offLinewSub;

    @Override
    protected void lazyLoad() {

    }

    @Override
    public int setContent() {
        return R.layout.layout_contact;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(IApplication.globleContext, msg.obj.toString());
                    break;
                case 2:
                    if (PersonalDataActivity.isShowAuthor)
                        CommonUtils.closeDialog();
                    ToastAlone.showLongToast(getActivity(), msg.obj.toString());
                    break;
                case 3:
                    getContactList.getContactList();
                    ToastAlone.showLongToast(getActivity(), msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void init() {
        productId = SharedPreferencesUtil.getInstance(IApplication.gainContext()).getString(SPKey.PRODUCT_ID);
        isMarried = SharedPreferencesUtil.getInstance(IApplication.gainContext()).getBoolean(SPKey.IS_MARRIED, false);
        if (PersonalDataActivity.isShowAuthor) {
            btn_contact_sava.setText("下一步");
        } else {
            btn_contact_sava.setText("提交");
        }
        dialog = new CustomerDialog(getActivity());
        customerManagerDataController = new CustomerManagerDataController(getActivity(), this);
        getContactList = new GetUserContactList(getActivity(), new ControllerCallBack() {
            @Override
            public void onSuccess(int returnCode, String value) {
                initContactList();
            }

            @Override
            public void onFail(int returnCode, final String errorMessage) {
                if (returnCode == CallBackType.GET_CONTACT_LIST) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = errorMessage;
                    handler.sendMessage(msg);
                }
            }
        });

//        UserInfoManager.getInstance().setStores(null);
        if (CustomerManagerManager.isCustomerManager()) {
            customerManagerDataController.getCustomeerContact(PersonalDataActivity.custId);
            tv_job1_phone.setEnabled(true);
            tv_job2_phone.setEnabled(true);
            iv_phone1.setVisibility(View.GONE);
            ivPhone2.setVisibility(View.GONE);
        } else {
            getContactList.getContactList();
            tv_job1_phone.setEnabled(false);
            tv_job2_phone.setEnabled(false);
            iv_phone1.setVisibility(View.VISIBLE);
            ivPhone2.setVisibility(View.VISIBLE);
        }
        contactList = new GetContactListController(getActivity());
        contactList.setCallBack(new GetContactListController.CallBack() {
            @Override
            public void success() {
                submit();
            }

            @Override
            public void error(String msg) {
                Log.e("wy", msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        contactController = new ContactController(getActivity(), this);
        confirmationOfLoanCotroller = new ConfirmationOfLoanCotroller(getActivity(), this);
        offLinewSub = new OffLinewSubJXLController(getActivity(), this);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void resume() {
//        getContactList.getContactList();
    }

    @Override
    public void destroy() {
    }

    private void submit() {
        cache();
        if (TextUtils.isEmpty(contactBean.getRelation1())) {
            ToastAlone.showLongToast(getContext(), "关系1不能为空!");
            return;
        }
        if (TextUtils.isEmpty(contactBean.getContact_name1())) {
            ToastAlone.showLongToast(getContext(), "联系人不能为空!");
            return;
        }
        if (TextUtils.isEmpty(contactBean.getContact_phone1())) {
            ToastAlone.showLongToast(getContext(), "联系电话不能为空!");
            return;
        }
        if (TextUtils.isEmpty(contactBean.getRelation2())) {
            ToastAlone.showLongToast(getContext(), "关系2不能为空!");
            return;
        }
        if (TextUtils.isEmpty(contactBean.getContact_name2())) {
            ToastAlone.showLongToast(getContext(), "联系人不能为空!");
            return;
        }
        if (TextUtils.isEmpty(contactBean.getContact_phone2())) {
            ToastAlone.showLongToast(getContext(), "联系电话不能为空!");
            return;
        }
        if (PersonalDataActivity.isShowAuthor) {
            if (CustomerManagerManager.isCustomerManager() && !productId.equals(CommonConstant.PRODUCT_OFFLINE)) {
                if (dialog == null) {
                    dialog = new CustomerDialog(getActivity());
                }
                dialog.confirmFailDialog();
                dialog.show();
            } else {
                if (dialog == null) {
                    dialog = new CustomerDialog(getActivity());
                }
                if (StringUtil.isNullOrEmpty(UserInfoManager.getInstance().getBorrowInfo().getBorrow_limit())) {//控制金额为空的时候，不能去提交确定
                    ToastAlone.showLongToast(getActivity(), "当前金额获取失败，请重试");
                    return;
                }
                dialog.confirmLoanDialog(getActivity(), this, productId);
                dialog.show();
            }
        } else {
            //我的页面提交
            contactController.savaContact(contactModels, null, PersonalDataActivity.offline_calp_msg, PersonalDataActivity.org_no, PersonalDataActivity.org_name, PersonalDataActivity.sall_emp_no);
        }
    }

    private void cache() {
        contactModels.clear();
        ContactModel contactModel = new ContactModel();
        //联系人
        String name = etContactName1.getText() == null ? "" : etContactName1.getText().toString().trim();
        //联系人电话
        String phone = tv_job1_phone.getText() == null ? "" : tv_job1_phone.getText().toString().trim();

        //根据联系人名称获取对应的Code
        String relationCode = AppConfig.getContCode(tv_relation1.getText().toString().trim());
        MyLog.i("keey", "relationCode:" + relationCode);
        contactBean.setContact_name1(name);
        contactModel.setContact_name(name);
        contactBean.setContact_phone1(phone);
        contactModel.setContact_phone(phone);
        contactBean.setRelation1(relationCode);
        contactModel.setRelation(relationCode);
        contactModel.setFill_location(1);
        contactModels.add(contactModel);
        contactModel = new ContactModel();
        name = etContactName2.getText() == null ? "" : etContactName2.getText().toString().trim();
        phone = tv_job2_phone.getText() == null ? "" : tv_job2_phone.getText().toString().trim();
        String relationCode2 = AppConfig.getContCode(tvRelation2.getText().toString().trim());
        MyLog.i("keey", "relationCode2:" + relationCode2);


        contactBean.setContact_name2(name);//联系人2
        contactModel.setContact_name(name);
        contactBean.setContact_phone2(phone);//联系人电话
        contactModel.setContact_phone(phone);
        contactBean.setRelation2(relationCode2);
        contactModel.setRelation(relationCode2);
        contactModel.setFill_location(2);
        contactModels.add(contactModel);
    }

    @OnClick({
            R.id.btn_contact_sava,
            R.id.tv_job1_phone,
            R.id.tv_job2_phone,
            R.id.tv_job3_phone,
            R.id.tv_job4_phone,
            R.id.iv_phone1,
            R.id.iv_phone2,
            R.id.iv_phone3,
            R.id.iv_phone4,
            R.id.ll_contact_phone1,
            R.id.ll_contact_phone2,
            R.id.ll_contact_phone3,
            R.id.ll_contact_phone4,
            R.id.tv_relation1,
            R.id.ll_contact_relation1,
            R.id.tv_relation2,
            R.id.ll_contact_relation2,
            R.id.tv_relation3,
            R.id.ll_contact_relation3,
            R.id.tv_relation4,
            R.id.ll_contact_relation4,
            R.id.btn_contact_previous
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_contact_sava:
                if (CustomerManagerManager.isCustomerManager()) {
                    submit();
                } else {
                    if (ToolPermission.checkSelfPermission(getActivity(), this, Manifest.permission.READ_CONTACTS, "从电话薄中选择联系人,请允许读取权限!", 124)) {
                        if (CommonUtils.queryContactPhoneNumber(mContext).size() > 0) {
                            if (contactList != null)
                                contactList.contactList();
//                            submit();
                        } else
                            ToastAlone.showLongToast(getActivity(), "请添加联系人或检查联系人权限");
                    } else {
                        if (AppOpsManagerUtil.isCheck(getActivity(), AppOpsManagerUtil.OP_READ_CONTACTS) == AppOpsManager.MODE_IGNORED) {
                            ToastAlone.showLongToast(getActivity(), "请打开读取联系人权限!");
                        }
                    }
                }
                break;
            case R.id.btn_contact_previous:
                ((PersonalDataActivity) getActivity()).swithFragment(JobFragment.class);
                break;
            case R.id.tv_relation1:
            case R.id.ll_contact_relation1:
                OneSelectPopupWindow relation;
                String marriedCode = SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.MARRIDE_CODE);
                String oneName = tvRelation2.getText().toString().trim();
                relation = new OneSelectPopupWindow(getActivity(), removeSlectCodeBean(AppConfig.getOneContacts(marriedCode), oneName));

                relation.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        contactBean.setRelation1(select.getCode());
                        tv_relation1.setText(select.getName());
                    }
                });
                relation.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case tv_relation2:
            case R.id.ll_contact_relation2:
                String marriedCode2 = SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.MARRIDE_CODE);
                List<CodeBean> towContacts = AppConfig.getTowContacts(marriedCode2);
                String towName = tv_relation1.getText().toString().trim();

                OneSelectPopupWindow relation1 = new OneSelectPopupWindow(getActivity(), removeSlectCodeBean(towContacts, towName));
//                if (isMarried && productId.equals(CommonConstant.PRODUCT_ONLINE_LOW_QUALITY)) {
//                    relation1 = new OneSelectPopupWindow(getActivity(), yihun2());
//                } else {
//                    relation1 = new OneSelectPopupWindow(getActivity(), youzhi());
//                }
                relation1.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        contactBean.setRelation2(select.getCode());
                        tvRelation2.setText(select.getName());
                    }
                });
                relation1.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_relation3:
            case R.id.ll_contact_relation3:
                OneSelectPopupWindow relation3 = new OneSelectPopupWindow(getActivity(), contactRelation1());
                relation3.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        contactBean.setRelation3(select.getCode());
                        tvRelation3.setText(select.getName());
                    }
                });
                relation3.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_relation4:
            case R.id.ll_contact_relation4:
                OneSelectPopupWindow relation4 = new OneSelectPopupWindow(getActivity(), contactRelation2());
                relation4.setOnSelectPopupWindow(new OneSelectPopupWindow.onSelectPopupWindow() {
                    @Override
                    public void onSelectClick(int index, CodeBean select) {
                        contactBean.setRelation4(select.getCode());
                        tvRelation4.setText(select.getName());
                    }
                });
                relation4.showAtLocation(root, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_contact_phone1:
            case R.id.tv_job1_phone:
            case R.id.iv_phone1:
                if (!CustomerManagerManager.isCustomerManager()) {
                    goToSelectPhone(1);
                }
                break;
            case R.id.ll_contact_phone2:
            case R.id.tv_job2_phone:
            case R.id.iv_phone2:
                if (!CustomerManagerManager.isCustomerManager()) {
                    goToSelectPhone(2);
                }
                break;
            case R.id.ll_contact_phone3:
            case R.id.tv_job3_phone:
            case R.id.iv_phone3:
                if (!CustomerManagerManager.isCustomerManager()) {
                    goToSelectPhone(3);
                }
                break;
            case R.id.ll_contact_phone4:
            case R.id.tv_job4_phone:
            case R.id.iv_phone4:
                if (!CustomerManagerManager.isCustomerManager()) {
                    goToSelectPhone(4);
                }
                break;
            case R.id.confirm:
                Log.e("wy", "confirm");
                if (dialog.periodIsFill) {
                    if (!dialog.isChecked) {
                        ToastAlone.showToast(getContext(), "请同意协议！", Toast.LENGTH_SHORT);
                        return;
                    }
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    limit = dialog.borrowConfirmInfo.getBorrow_limit();
                    period = dialog.borrowConfirmInfo.getBorrow_periods();
                    if (CustomerManagerManager.isCustomerManager()) {
                        if (productId.equals("3")) {//线下
                            //如果是线下，要检查业务员信息。为空的时候不能去保存
                            if (StringUtil.isNullOrEmpty(PersonalDataActivity.offline_calp_msg) || StringUtil.isNullOrEmpty(PersonalDataActivity.org_no)
                                    || StringUtil.isNullOrEmpty(PersonalDataActivity.sall_emp_no) || StringUtil.isNullOrEmpty(PersonalDataActivity.org_name)) {
                                ToastAlone.showLongToast(getActivity(), "业务员信息为空，请从新获取");
                                return;
                            }
                        }
                        customerManagerDataController.saveCustomeerContact(contactModels, dialog.borrowConfirmInfo, PersonalDataActivity.custId, PersonalDataActivity.offline_calp_msg, PersonalDataActivity.org_no, PersonalDataActivity.org_name, PersonalDataActivity.sall_emp_no);
                    } else {
                        if (contactController == null) {
                            contactController = new ContactController(getActivity(), this);
                        }
                        contactController.savaContact(contactModels, dialog.borrowConfirmInfo, PersonalDataActivity.offline_calp_msg, PersonalDataActivity.org_no, PersonalDataActivity.org_name, PersonalDataActivity.sall_emp_no);
                    }
                } else {
                    ToastAlone.showLongToast(getActivity(), "请选择周期!");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 清除选择的
     *
     * @param msg
     * @return
     */
    private List<CodeBean> removeSlectCodeBean(List<CodeBean> list, String msg) {
        List<CodeBean> empCodeBeans = new ArrayList<>();
        for (CodeBean mCodeBean : list) {
            if (!mCodeBean.getName().equals(msg)) {
                empCodeBeans.add(mCodeBean);
            }
        }

        return empCodeBeans;
    }

    private static int selectPhoneType;

    private void goToSelectPhone(int i) {
        selectPhoneType = i;
        if (ToolPermission.checkSelfPermission(getActivity(), this, Manifest.permission.READ_CONTACTS, "从电话薄中选择联系人,请允许读取权限!", 123)) {
            if (CommonUtils.queryContactPhoneNumber(mContext).size() > 0)
                startActivityForResult(CommonUtils.goToSelectPhone(), 1234);
            else
                ToastAlone.showLongToast(getActivity(), "请添加联系人或检查权限");
            if (contactList != null) {

//                contactList.contactList();
            }
        } else {
            if (AppOpsManagerUtil.isCheck(getActivity(), AppOpsManagerUtil.OP_READ_CONTACTS) == AppOpsManager.MODE_IGNORED) {
                ToastAlone.showLongToast(getActivity(), "请打开读取联系人权限!");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (ToolPermission.checkPermission(permissions, grantResults)) {
                goToSelectPhone(selectPhoneType);
            }
        }
        if (requestCode == 124) {
            if (ToolPermission.checkPermission(permissions, grantResults)) {
                if (CommonUtils.queryContactPhoneNumber(mContext).size() > 0) {
                    if (contactList != null)
                        contactList.contactList();
//                    submit();
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1234 == requestCode && resultCode == Activity.RESULT_OK) {
            String phoneNo = CommonUtils.getPhoneNo(getActivity(), resultCode, data);
            if (TextUtils.isEmpty(phone(phoneNo)))
                return;
            switch (selectPhoneType) {
                case 1:
                    tv_job1_phone.setText(phone(phoneNo));
                    break;
                case 2:
                    tv_job2_phone.setText(phone(phoneNo));
                    break;
                case 3:
                    tv_job3_phone.setText(phone(phoneNo));
                    break;
                case 4:
                    tv_job4_phone.setText(phone(phoneNo));
                    break;
            }
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
                ToastAlone.showShortToast(getActivity(), "联系人电话不合法!");
                phone = "";
            }
            if (!(RegularExpUtil.isMobile(phone))) {
                ToastAlone.showShortToast(getActivity(), "联系人电话不合法!");
                phone = "";
            }
            return phone;
        } else {
            return "";
        }
    }

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.SAVA_CONTACT || returnCode == CallBackType.SAVE_CUSTOMER_CONTACT) {
            if (PersonalDataActivity.isShowAuthor)
                CommonUtils.closeDialog();
            if (PersonalDataActivity.isShowAuthor) {
                if (confirmationOfLoanCotroller == null) {
                    confirmationOfLoanCotroller = new ConfirmationOfLoanCotroller(getActivity(), this);
                }
                if (productId.equals(CommonConstant.PRODUCT_OFFLINE)) {
                    if (confirmationOfLoanCotroller == null) {
                        confirmationOfLoanCotroller = new ConfirmationOfLoanCotroller(getActivity(), this);
                    }
                    BorrowInfoBean bean = new BorrowInfoBean();
                    bean.setBorrow_limit(limit);
                    bean.setProduct_id(productId);
                    bean.setBorrow_periods(period);
                    if (CustomerManagerManager.isCustomerManager()) {
                        confirmationOfLoanCotroller.customerConfirmation(bean, PersonalDataActivity.custId, PersonalDataActivity.org_no);
                    } else {
                        confirmationOfLoanCotroller.confirmation(bean, PersonalDataActivity.org_no);
                    }
                } else {
                    confirmationOfLoanCotroller.prepare_audit();
                }
            }
        } else if (returnCode == CallBackType.PREPARE_AUDIT) {
            if (productId.equals(CommonConstant.PRODUCT_OFFLINE)) {
                if (CustomerManagerManager.isCustomerManager()) {
                    offLinewSub.offlineSubMitCustomer(PersonalDataActivity.custId);
                } else {
                    offLinewSub.offlineSubMit();
                }
            } else {
                ((PersonalDataActivity) getActivity()).setComplete(3);
                if (PersonalDataActivity.isShowAuthor) {
                    ((PersonalDataActivity) getActivity()).showArrow(true);
                    ((PersonalDataActivity) getActivity()).swithFragment(AuthenticateFragment.class);
                }
            }
        } else if (returnCode == CallBackType.GET_CUSTOMER_CONTACT) {
            initContactList();
        } else if (returnCode == CallBackType.OFFLINE_SKIP_JXL) {
            if (confirmationOfLoanCotroller == null) {
                confirmationOfLoanCotroller = new ConfirmationOfLoanCotroller(getActivity(), this);
            }
            BorrowInfoBean bean = new BorrowInfoBean();
            bean.setBorrow_limit(limit);
            bean.setProduct_id(productId);
            bean.setBorrow_periods(period);
            confirmationOfLoanCotroller.confirmation(bean, PersonalDataActivity.org_no);
        } else if (returnCode == CallBackType.CUSTOMER_OFFLINE_SKIP_JXL) {
            if (confirmationOfLoanCotroller == null) {
                confirmationOfLoanCotroller = new ConfirmationOfLoanCotroller(getActivity(), this);
            }
            BorrowInfoBean bean = new BorrowInfoBean();
            bean.setBorrow_limit(limit);
            bean.setProduct_id(productId);
            bean.setBorrow_periods(period);
            confirmationOfLoanCotroller.confirmation(bean, PersonalDataActivity.org_no);
        } else if (returnCode == CallBackType.COMMIT_BORROW_INFO || returnCode == CallBackType.CUSTOMER_COMMIT_BORROW_INFO) {
            ((PersonalDataActivity) getActivity()).setComplete(4);
            if (PersonalDataActivity.isShowAuthor) {
                ((PersonalDataActivity) getActivity()).showArrow(true);
                ((PersonalDataActivity) getActivity()).swithFragment(ApplyEndFragment.class);
            }
        }
    }

    @Override
    public void onFail(int returnCode, final String errorMessage) {
        if (returnCode == CallBackType.SAVA_CONTACT || returnCode == CallBackType.SAVE_CUSTOMER_CONTACT || returnCode == CallBackType.COMMIT_BORROW_INFO) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.PREPARE_AUDIT) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else if (returnCode == CallBackType.GET_CUSTOMER_CONTACT || returnCode == CallBackType.OFFLINE_SKIP_JXL || returnCode == CallBackType.COMMIT_BORROW_INFO) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = errorMessage;
            handler.sendMessage(msg);
        }
    }

    public void initContactList() {
        String marriedCode = SharedPreferencesUtil.getInstance(getActivity()).getString(SPKey.MARRIDE_CODE);
        List<ContactListModel> listModels = UserInfoManager.getInstance().getContactListModels();
        for (int i = 0; i < listModels.size(); i++) {
            ContactListModel item = listModels.get(i);
            Log.e("Fill_location", "" + item.getFill_location());
            switch (item.getFill_location()) {
                case 1:
                    if (!TextUtils.isEmpty(item.getContactName())) {
                        etContactName1.setText(item.getContactName());
                    }
                    if (!TextUtils.isEmpty(item.getContactPhone())) {
                        tv_job1_phone.setText(item.getContactPhone());
                    }
                    if (!TextUtils.isEmpty(item.getRelation())) {
                        String cont1 = AppConfig.getCodeName(1, item.getRelation(), marriedCode);
                        if (TextUtils.isEmpty(cont1)) {
                            tv_relation1.setText("");
                            item.setRelation("");
                        } else {
                            tv_relation1.setText(cont1);
                        }
//                        int index;
//                        if (isMarried) {
//                            index = yihun1().indexOf(new CodeBean(0, item.getRelation(), ""));
//                        } else {
//                            index = youzhi().indexOf(new CodeBean(0, item.getRelation(), ""));
//                        }
//                        if (index != -1) {
//                            if (isMarried) {
//                                tv_relation1.setText(yihun1().get(index).getName());
//                            } else {
//                                tv_relation1.setText(youzhi().get(index).getName());
//                            }
//                        } else {
//                            tv_relation1.setText("");
//                            item.setRelation("");
//                        }
                    }
                    break;

                case 2:
                    if (!TextUtils.isEmpty(item.getContactName())) {
                        etContactName2.setText(item.getContactName());
                    }
                    if (!TextUtils.isEmpty(item.getContactPhone())) {
                        tv_job2_phone.setText(item.getContactPhone());
                    }
                    if (!TextUtils.isEmpty(item.getRelation())) {
                        String cont2 = AppConfig.getCodeName(2, item.getRelation(), marriedCode);
                        if (TextUtils.isEmpty(cont2)) {
                            tvRelation2.setText("");
                            item.setRelation("");
                        } else {
                            tvRelation2.setText(cont2);
                        }
//                        int index;
//                        if (isMarried && productId.equals(CommonConstant.PRODUCT_ONLINE_LOW_QUALITY)) {
//                            index = yihun2().indexOf(new CodeBean(0, item.getRelation(), ""));
//                        } else {
//                            index = youzhi().indexOf(new CodeBean(0, item.getRelation(), ""));
//                        }
//                        if (index != -1) {
//                            if (isMarried && productId.equals(CommonConstant.PRODUCT_ONLINE_LOW_QUALITY)) {
//                                tvRelation2.setText(yihun2().get(index).getName());
//                            } else {
//                                tvRelation2.setText(youzhi().get(index).getName());
//                            }
//                        } else {
//                            tvRelation2.setText("");
//                            item.setRelation("");
                        //}
                    }
//                            }
                    break;
                case 3:
                    if (!TextUtils.isEmpty(item.getContactName())) {
                        et_contact_name3.setText(item.getContactName());
                    }
                    if (!TextUtils.isEmpty(item.getContactPhone())) {
                        tv_job3_phone.setText(item.getContactPhone());
                    }
                    if (!TextUtils.isEmpty(item.getRelation())) {
                        int index = contactRelation1().indexOf(new CodeBean(0, item.getRelation(), ""));
                        if (index != -1)
                            tvRelation3.setText(contactRelation1().get(index).getName());
                    }
                    break;
                case 4:
                    if (!TextUtils.isEmpty(item.getContactName())) {
                        etContactName4.setText(item.getContactName());
                    }
                    if (!TextUtils.isEmpty(item.getContactPhone())) {
                        tv_job4_phone.setText(item.getContactPhone());
                    }
                    if (!TextUtils.isEmpty(item.getRelation())) {
                        int index = contactRelation2().indexOf(new CodeBean(0, item.getRelation(), ""));
                        if (index != -1)
                            tvRelation4.setText(contactRelation2().get(index).getName());
                    }
                    break;
            }
        }
    }
}
