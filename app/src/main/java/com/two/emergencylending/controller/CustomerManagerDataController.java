package com.two.emergencylending.controller;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.BorrowInfoBean;
import com.two.emergencylending.bean.ContactListModel;
import com.two.emergencylending.bean.ContactModel;
import com.two.emergencylending.bean.JobInforBean;
import com.two.emergencylending.bean.PerSonalBean;
import com.two.emergencylending.bean.PresentStore;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.Des3;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;
import com.two.emergencylending.utils.ToastAlone;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：客户经理接口
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class CustomerManagerDataController {
    private String TAG = CustomerManagerDataController.class.getSimpleName();
    private Activity mActivity;
    private ControllerCallBack mController;
    private Map parameter;
    private String msg = "";

    public CustomerManagerDataController(Activity mActivity, ControllerCallBack iController) {
        this.mActivity = mActivity;
        this.mController = iController;
        parameter = new HashMap();
    }

    public void getCustomeerData(String cust_id) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("route", "1");
        parameter.put("borrow_limit", UserInfoManager.getInstance().getBorrowInfo().getBorrow_limit());
        parameter.put("cust_id", cust_id);
        CommonUtils.showDialog(mActivity, "正在努力加载....");
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.GET_CUSTOMER_DATA, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                LogUtil.d(TAG, response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    String code = object.getString("flag");

                    if (code.equals(ErrorCode.SUCCESS)) {
                        if (response.indexOf("result") != -1) {
                            String jsonObject = object.optString("result");
                            msg = object.optString("msg");
                            PerSonalBean perSonalBean = new Gson().fromJson(jsonObject, PerSonalBean.class);
                            if (!StringUtil.isNullOrEmpty(perSonalBean.getResult())) {
                                JSONObject res = new JSONObject(perSonalBean.getResult());
                                if (res.has("online_msg")) {
                                    PersonalDataActivity.offline_calp_msg = res.getString("online_msg");
                                } else {
                                    PersonalDataActivity.offline_calp_msg = null;
                                }
                                if (res.has("org_no")) {
                                    PersonalDataActivity.org_no = res.getString("org_no");
                                } else {
                                    PersonalDataActivity.org_no = null;
                                }
                                if (res.has("sall_emp_no")) {
                                    PersonalDataActivity.sall_emp_no = res.getString("sall_emp_no");
                                } else {
                                    PersonalDataActivity.sall_emp_no = null;
                                }
                                if (res.has("org_name")) {
                                    PersonalDataActivity.org_name = res.getString("org_name");
                                } else {
                                    PersonalDataActivity.org_name = null;
                                }
                            }
                            UserInfoManager.getInstance().setPerSonalBean(perSonalBean);
                            success(CallBackType.GET_CUSTOMER_DATA, msg);
                        } else {
                            UserInfoManager.getInstance().setPerSonalBean(new PerSonalBean());
                            success(CallBackType.GET_CUSTOMER_DATA, msg);
                        }
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        UserInfoManager.getInstance().setPerSonalBean(new PerSonalBean());
                        fail(CallBackType.GET_CUSTOMER_DATA, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.GET_CUSTOMER_DATA, ErrorCode.FAIL_DATA);
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.GET_CUSTOMER_DATA, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();

            }
        });
    }


    public void saveCustomeerData(PerSonalBean perSonalBean, String custId) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        CommonUtils.showDialog(mActivity, "正在努力加载....");
        parameter.put("cust_id", custId);
        parameter.put("username", perSonalBean.getUsername());
        parameter.put("idcard", perSonalBean.getIdcard());
        parameter.put("education", "6402");
        parameter.put("marriage", perSonalBean.getMarriage());
        parameter.put("live_status", perSonalBean.getLive_status());

        parameter.put("huji_adr", perSonalBean.getHuji_adr());
        parameter.put("huji_adr_detail", perSonalBean.getHuji_adr_detail());
        parameter.put("live_adr", perSonalBean.getLive_adr());
        parameter.put("live_adr_detail", perSonalBean.getLive_adr_detail());
        parameter.put("bank_code", perSonalBean.getBank_code());
        parameter.put("bankcard_no", perSonalBean.getBankcard_no());
        parameter.put("bank_phone", perSonalBean.getBank_phone());

        parameter.put("idcard_z", perSonalBean.getIdCardZId());
        parameter.put("idcard_f", perSonalBean.getIdCardFId());
        parameter.put("idcard_hand", perSonalBean.getIdCardHandId());
        parameter.put("product_id", SharedPreferencesUtil.getInstance(IApplication.gainContext()).getString(SPKey.PRODUCT_ID));
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("unit_adr", perSonalBean.getUnit_adr());
        parameter.put("unit_adr_detail", perSonalBean.getUnit_adr_detail());
        parameter.put("product_id", perSonalBean.getProduct_id());
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.SAVE_CUSTOMER_DATA, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                LogUtil.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("flag");
                    if (code.equals(ErrorCode.SUCCESS)) {
//                        String res = Des3.decode(jsonObject.getString("result"));
//                        LogUtil.d(TAG, res);
                        success(CallBackType.SAVE_CUSTOMER_DATA, "");
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(CallBackType.SAVE_CUSTOMER_DATA, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.SAVE_CUSTOMER_DATA, ErrorCode.FAIL_DATA);
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.SAVE_CUSTOMER_DATA, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();

            }
        });
    }

    public void getCustomeerJob(String custId) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        CommonUtils.showDialog(mActivity, "正在努力加载....");
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("cust_id", custId);
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.GET_CUSTOMER_JOB, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                LogUtil.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("flag");

                    if (code.equals(ErrorCode.SUCCESS)) {
                        String res = Des3.decode(jsonObject.getString("result"));
                        JobInforBean jobinfo = new Gson().fromJson(res, JobInforBean.class);
                        UserInfoManager.getInstance().setJobBean(jobinfo);
                        LogUtil.d(TAG, res);
                        success(CallBackType.GET_CUSTOMER_JOB, res);
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(CallBackType.GET_CUSTOMER_JOB, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.GET_CUSTOMER_JOB, ErrorCode.FAIL_DATA);
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.GET_CUSTOMER_JOB, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();

            }
        });
    }


    public void saveCustomeerJob(JobInforBean jobInforBean, String custId) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        CommonUtils.showDialog(mActivity, "正在努力加载....");
        parameter.put("cust_id", custId);
        parameter.put("professional", jobInforBean.getProfessional());
        parameter.put("month_pay", jobInforBean.getMonth_pay());//月收入
        parameter.put("unit_name", jobInforBean.getUnit_name());//单位名字
        parameter.put("unit_phone", jobInforBean.getUnit_phone());
        parameter.put("unit_adr", jobInforBean.getUnit_adr());
        parameter.put("unit_adr_detail", jobInforBean.getUnit_adr_detail());
        parameter.put("unit_department", jobInforBean.getUnit_department());
        parameter.put("unit_industry", jobInforBean.getUnit_industry());
        parameter.put("title", jobInforBean.getTitle());
        parameter.put("product_id", UserInfoManager.getInstance().getPerSonalBean().getProduct_id());
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.SAVE_CUSTOMER_JOB, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                LogUtil.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("flag");
                    if (code.equals(ErrorCode.SUCCESS)) {
//                        String res = Des3.decode(jsonObject.getString("result"));
//                        LogUtil.d(TAG, res);
                        success(CallBackType.SAVE_CUSTOMER_JOB, "");
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(CallBackType.SAVE_CUSTOMER_JOB, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.SAVE_CUSTOMER_JOB, ErrorCode.FAIL_DATA);
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.SAVE_CUSTOMER_JOB, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();

            }
        });
    }


    public void getCustomeerContact(String custId) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("cust_id", custId);
        CommonUtils.showDialog(mActivity, "正在努力加载....");
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.GET_CUSTOMER_CONTACT, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                LogUtil.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("flag");

                    if (code.equals(ErrorCode.SUCCESS)) {
                        String res = Des3.decode(jsonObject.getString("result"));
                        LogUtil.d(TAG, res);
                        String count = jsonObject.optString("count");
                        JSONObject jsonObject1 = new JSONObject(res);
                        List<PresentStore> stores = new Gson().fromJson(jsonObject1.optString("storeList"),
                                new TypeToken<List<PresentStore>>() {
                                }.getType());
                        UserInfoManager.getInstance().setStores(stores);
                        String contact = jsonObject1.optString("contactList");
                        if (res.indexOf("contactList") != -1) {
                            List<ContactListModel> contactList = new Gson().fromJson(contact,
                                    new TypeToken<List<ContactListModel>>() {
                                    }.getType());
                            UserInfoManager.getInstance().setContactListModels(contactList);
                        }
                        success(CallBackType.GET_CUSTOMER_CONTACT, res);
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(CallBackType.GET_CUSTOMER_CONTACT, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.GET_CUSTOMER_CONTACT, ErrorCode.FAIL_DATA);
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.GET_CUSTOMER_CONTACT, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();

            }
        });
    }

    public void saveCustomeerContact(List<ContactModel> contactModels, BorrowInfoBean borrowInfoBean, String custId, String offline_calp_msg, String org_no, String org_name, String sall_emp_no) {
        if (!CommonUtils.isNetAvailable()) {
            ToastAlone.showLongToast(mActivity, ErrorCode.FAIL_NETWORK);
        }
        parameter.clear();
        CommonUtils.showDialog(mActivity, "正在努力加载....");
        String appleinfo = SharedPreferencesUtil.getInstance(IApplication.gainContext()).getString(SPKey.APPLY_INFO);
        String renew_loan_type = SharedPreferencesUtil.getInstance(IApplication.gainContext()).getString(SPKey.IS_RENEW_LOANS);
        parameter.put("cust_id", custId);
        parameter.put("offline_calp_msg", offline_calp_msg);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("contactList", new Gson().toJson(contactModels));
        parameter.put("renew_loan_type", renew_loan_type);
        parameter.put("borrow_use", "525");
        parameter.put("sales_name", "");
        parameter.put("sales_no", sall_emp_no);
        parameter.put("store", org_no);
        parameter.put("store_name", org_name);

        if (PersonalDataActivity.isShowAuthor) {
            parameter.put("route", "1");
            parameter.put("product_id", borrowInfoBean.getProduct_id());
            parameter.put("borrow_limit", borrowInfoBean.getBorrow_limit());
            parameter.put("borrow_periods", borrowInfoBean.getBorrow_periods());

        } else {
            parameter.put("route", "0");
            parameter.put("product_id", UserInfoManager.getInstance().getPerSonalBean().getProduct_id());
            parameter.put("borrow_limit", "2000");
            parameter.put("borrow_periods", "10");
        }
        LogUtil.d(TAG, parameter.toString().trim());
        OKManager.getInstance().sendComplexForm(NetContants.SAVE_CUSTOMER_CONTACT, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                CommonUtils.closeDialog();
                LogUtil.d(TAG, response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("flag");
                    if (code.equals(ErrorCode.SUCCESS)) {
                        String res = Des3.decode(jsonObject.getString("result"));
                        LogUtil.d(TAG, res);
                        success(CallBackType.SAVE_CUSTOMER_CONTACT, res);
                    } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        String msg = jsonObject.optString("msg");
                        fail(CallBackType.SAVE_CUSTOMER_CONTACT, msg);
                    }
                } catch (Exception e) {
                    fail(CallBackType.SAVE_CUSTOMER_CONTACT, ErrorCode.FAIL_DATA);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                fail(CallBackType.SAVE_CUSTOMER_CONTACT, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();

            }
        });
    }


    public void success(int returnCode, String value) {
        if (mController != null) {
            mController.onSuccess(returnCode, value);
        }
    }

    public void fail(int returnCode, String errorMessage) {
        if (mController != null) {
            mController.onFail(returnCode, errorMessage);
        }
    }

}
