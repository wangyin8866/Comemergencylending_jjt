package com.two.emergencylending.controller;

import android.app.Activity;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.BorrowInfoBean;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.ErrorCode;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.Arithmetic;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.LogUtil;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：确认借款接口
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class ConfirmationOfLoanCotroller {
    private static final String TAG = ConfirmationOfLoanCotroller.class.getSimpleName();
    Activity mActivity;
    Map<String, String> parameter;
    ControllerCallBack mController;

    public ConfirmationOfLoanCotroller(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mController = controller;
        parameter = new HashMap<>();
    }

    public void getConfirmationInfo() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.GET_CONFIRM_INFO, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(response)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        String borrow = json.getString("result");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            success(CallBackType.GET_CONFIRM_INFO, borrow);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.GET_CONFIRM_INFO, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA + e.getMessage());
                        fail(CallBackType.GET_CONFIRM_INFO, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.GET_CONFIRM_INFO, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                fail(CallBackType.COMMIT_BORROW_INFO, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void getConfirmationInfo(String custId) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("cust_id", custId);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.GET_CONFIRM_INFO, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(response)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        String borrow = json.getString("result");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            success(CallBackType.GET_CONFIRM_INFO, borrow);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.GET_CONFIRM_INFO, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA + e.getMessage());
                        fail(CallBackType.GET_CONFIRM_INFO, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.GET_CONFIRM_INFO, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                fail(CallBackType.COMMIT_BORROW_INFO, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    //预检
    public void prepare_audit() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.PREPARE_AUDIT, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(response)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            success(CallBackType.PREPARE_AUDIT, "");
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.PREPARE_AUDIT, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA + e.getMessage());
                        fail(CallBackType.PREPARE_AUDIT, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.PREPARE_AUDIT, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                fail(CallBackType.PREPARE_AUDIT, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }


    //业务员预检
    public void prepare_audit(String custId) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("cust_id", custId);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.PREPARE_AUDIT, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(response)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            success(CallBackType.PREPARE_AUDIT, "");
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.PREPARE_AUDIT, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA + e.getMessage());
                        fail(CallBackType.PREPARE_AUDIT, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.PREPARE_AUDIT, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                fail(CallBackType.PREPARE_AUDIT, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    //提交
    public void confirmation(BorrowInfoBean borrow) {
        confirmation(borrow, "");
    }

    //提交
    public void confirmation(BorrowInfoBean borrow, String storeCode) {
        if (!CommonUtils.isNetAvailable()) return;
        if (StringUtil.isNullOrEmpty(borrow.getProduct_id())) {
            borrow.setProduct_id("1");
        }
        parameter.put("product_id", borrow.getProduct_id());
        parameter.put("borrow_limit", borrow.getBorrow_limit());
        parameter.put("borrow_periods", borrow.getBorrow_periods());
        if (StringUtil.isNullOrEmpty(storeCode)) {
            parameter.put("store", borrow.getStore());
        } else {
            parameter.put("store", storeCode);
        }
        if (StringUtil.isNullOrEmpty(borrow.getManagement_cost())) {
            borrow.setManagement_cost(String.valueOf(Arithmetic.expenseByManager(Integer.valueOf(borrow.getBorrow_limit()), Integer.valueOf(borrow.getBorrow_periods()))));
        }
        parameter.put("management_cost", borrow.getManagement_cost());
        parameter.put("borrow_use", "525");
        parameter.put("refund_way", "按周还款");
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.COMMIT_BORROW_INFO, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(response)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            success(CallBackType.COMMIT_BORROW_INFO, "");
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.COMMIT_BORROW_INFO, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA + e.getMessage());
                        fail(CallBackType.COMMIT_BORROW_INFO, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.COMMIT_BORROW_INFO, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                fail(CallBackType.COMMIT_BORROW_INFO, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    //业务员提交
    public void customerConfirmation(BorrowInfoBean borrow, String custId) {
        customerConfirmation(borrow, custId, "");
    }

    //业务员提交
    public void customerConfirmation(BorrowInfoBean borrow, String custId, String storeCode) {
        if (!CommonUtils.isNetAvailable()) return;
        if (StringUtil.isNullOrEmpty(borrow.getProduct_id())) {
            borrow.setProduct_id("1");
        }
        parameter.put("cust_id", custId);
        parameter.put("product_id", borrow.getProduct_id());
        parameter.put("borrow_limit", borrow.getBorrow_limit());
        parameter.put("borrow_periods", borrow.getBorrow_periods());
        if (StringUtil.isNullOrEmpty(storeCode)) {
            parameter.put("store", borrow.getStore());
        } else {
            parameter.put("store", storeCode);
        }

        if (StringUtil.isNullOrEmpty(borrow.getManagement_cost())) {
            borrow.setManagement_cost(String.valueOf(Arithmetic.expenseByManager(Integer.valueOf(borrow.getBorrow_limit()), Integer.valueOf(borrow.getBorrow_periods()))));
        }
        parameter.put("management_cost", borrow.getManagement_cost());
        parameter.put("borrow_use", "525");
        parameter.put("refund_way", "按周还款");
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.COMMIT_BORROW_INFO, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(response)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            success(CallBackType.CUSTOMER_COMMIT_BORROW_INFO, "");
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.CUSTOMER_COMMIT_BORROW_INFO, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA + e.getMessage());
                        fail(CallBackType.CUSTOMER_COMMIT_BORROW_INFO, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.CUSTOMER_COMMIT_BORROW_INFO, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK + e.getMessage());
                fail(CallBackType.CUSTOMER_COMMIT_BORROW_INFO, ErrorCode.FAIL_NETWORK);
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
