package com.two.emergencylending.controller;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.two.emergencylending.activity.PersonalDataActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.PerSonalBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 获取产品id
 */

public class GetProductIDController {
    private Activity mActivity;
    private Map parameter;
    private ControllerCallBack mIController;

    public GetProductIDController(Activity activity, ControllerCallBack iController) {
        this.mActivity = activity;
        this.mIController = iController;
        parameter = new HashMap();
    }

    public void getProductID(String phone, PerSonalBean perSonalBean) {
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("idcard", perSonalBean.getIdcard());

        parameter.put("huji_province", perSonalBean.getHuji_province());
        parameter.put("huji_city", perSonalBean.getHuji_city());
        parameter.put("huji_county", perSonalBean.getHuji_county());
        parameter.put("huji_adr_detail", perSonalBean.getHuji_adr_detail());

        parameter.put("live_province", perSonalBean.getLive_province());
        parameter.put("live_city", perSonalBean.getLive_city());
        parameter.put("live_county", perSonalBean.getLive_county());
        parameter.put("live_adr_detail", perSonalBean.getLive_adr_detail());

        parameter.put("unit_province", perSonalBean.getUnit_province());
        parameter.put("unit_city", perSonalBean.getUnit_city());
        parameter.put("unit_county", perSonalBean.getUnit_county());
        parameter.put("unit_adr_detail", perSonalBean.getUnit_adr_detail());
        parameter.put("phone", phone);
        if (!PersonalDataActivity.isShowAuthor) {
            parameter.put("route", "0");
        } else {
            parameter.put("route", "1");
        }
        Log.d("parameter", parameter.toString());

        OKManager.getInstance().sendComplexForm(NetContants.GET_PRODUCCT_ID, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                Log.d("onResponse", result.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result.toString());
                    String flag = jsonObject.optString("flag");
                    String msg = jsonObject.optString("msg");
                    if (flag.equals("0000")) {
                        JSONObject re = jsonObject.optJSONObject("result");
                        String product = re.optString("product_id");
                        if (!TextUtils.isEmpty(product)) {
                            if (UserInfoManager.getInstance().getPerSonalBean() != null
                                    && !TextUtils.isEmpty(UserInfoManager.getInstance().getPerSonalBean().getProduct_id())) {
                                if (!UserInfoManager.getInstance().getPerSonalBean().getProduct_id().equals(product)) {
                                    UserInfoManager.getInstance().getPerSonalBean().setProduct_id(product);
                                    success(CallBackType.GET_PRODUCT_ID, product);
                                } else {
                                    UserInfoManager.getInstance().getPerSonalBean().setProduct_id(product);
                                    success(CallBackType.GET_PRODUCT_ID_SAME, product);//与之前产品类型相同
                                }
                            } else {
                                UserInfoManager.getInstance().getPerSonalBean().setProduct_id(product);
                                success(CallBackType.GET_PRODUCT_ID, product);
                            }
                        }
                        if (re.has("result")) {
                            String resultString = re.optString("result");
                            JSONObject resultJson = new JSONObject(resultString);
                            if (resultJson.has("online_msg")) {
                                PersonalDataActivity.offline_calp_msg = resultJson.getString("online_msg");
                            } else {
                                PersonalDataActivity.offline_calp_msg = null;
                            }
                            if (resultJson.has("org_no")) {
                                PersonalDataActivity.org_no = resultJson.getString("org_no");
                            } else {
                                PersonalDataActivity.org_no = null;
                            }
                            if (resultJson.has("sall_emp_no")) {
                                PersonalDataActivity.sall_emp_no = resultJson.getString("sall_emp_no");
                            } else {
                                PersonalDataActivity.sall_emp_no = null;
                            }
                            if (resultJson.has("org_name")) {
                                PersonalDataActivity.org_name = resultJson.getString("org_name");
                            } else {
                                PersonalDataActivity.org_name = null;
                            }
                        }
                    } else if (flag.equals("0006")) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.GET_PRODUCT_ID, msg);
                    }
                } catch (JSONException e) {
                    fail(CallBackType.GET_PRODUCT_ID, "数据异常");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(CallBackType.GET_PRODUCT_ID, "网络连接异常!");
            }
        });
    }

    public void getCustomerProductID(String custId, String phone, PerSonalBean perSonalBean) {
        parameter.clear();
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        parameter.put("cust_id", custId);
        parameter.put("idcard", perSonalBean.getIdcard());
        parameter.put("huji_province", perSonalBean.getHuji_province());
        parameter.put("huji_city", perSonalBean.getHuji_city());
        parameter.put("huji_county", perSonalBean.getHuji_county());
        parameter.put("huji_adr_detail", perSonalBean.getHuji_adr_detail());
        parameter.put("live_province", perSonalBean.getLive_province());
        parameter.put("live_city", perSonalBean.getLive_city());
        parameter.put("live_county", perSonalBean.getLive_county());
        parameter.put("live_adr_detail", perSonalBean.getLive_adr_detail());
        parameter.put("unit_province", perSonalBean.getUnit_province());
        parameter.put("unit_city", perSonalBean.getUnit_city());
        parameter.put("unit_county", perSonalBean.getUnit_county());
        parameter.put("unit_adr_detail", perSonalBean.getUnit_adr_detail());
        parameter.put("phone", phone);
        parameter.put("route", "1");
        Log.d("parameter", parameter.toString());

        OKManager.getInstance().sendComplexForm(NetContants.GET_CUSTOMER_PRODUCCT_ID, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                Log.d("onResponse", result.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result.toString());
                    String flag = jsonObject.optString("flag");
                    String msg = jsonObject.optString("msg");
                    if (flag.equals("0000")) {
                        JSONObject re = jsonObject.optJSONObject("result");
                        String product = re.optString("product_id");
                        if (!TextUtils.isEmpty(product)) {
                            if (UserInfoManager.getInstance().getPerSonalBean() != null
                                    && !TextUtils.isEmpty(UserInfoManager.getInstance().getPerSonalBean().getProduct_id())) {
                                if (!UserInfoManager.getInstance().getPerSonalBean().getProduct_id().equals(product)) {
                                    UserInfoManager.getInstance().getPerSonalBean().setProduct_id(product);
                                    success(CallBackType.GET_PRODUCT_ID, product);
                                } else {
                                    UserInfoManager.getInstance().getPerSonalBean().setProduct_id(product);
                                    success(CallBackType.GET_PRODUCT_ID_SAME, product);//与之前产品类型相同
                                }
                            } else {
                                UserInfoManager.getInstance().getPerSonalBean().setProduct_id(product);
                                success(CallBackType.GET_PRODUCT_ID, product);
                            }
                        }
                        if (re.has("result")) {
                            String resultString = re.optString("result");
                            JSONObject resultJson = new JSONObject(resultString);
                            if (resultJson.has("online_msg")) {
                                PersonalDataActivity.offline_calp_msg = resultJson.getString("online_msg");
                            } else {
                                PersonalDataActivity.offline_calp_msg = null;
                            }
                            if (resultJson.has("org_no")) {
                                PersonalDataActivity.org_no = resultJson.getString("org_no");
                            } else {
                                PersonalDataActivity.org_no = null;
                            }
                            if (resultJson.has("sall_emp_no")) {
                                PersonalDataActivity.sall_emp_no = resultJson.getString("sall_emp_no");
                            } else {
                                PersonalDataActivity.sall_emp_no = null;
                            }
                            if (resultJson.has("org_name")) {
                                PersonalDataActivity.org_name = resultJson.getString("org_name");
                            } else {
                                PersonalDataActivity.org_name = null;
                            }
                        }
                    } else if (flag.equals("0006")) {
                        IApplication.getInstance().backToLogin(mActivity);
                    } else {
                        fail(CallBackType.GET_PRODUCT_ID, msg);
                    }
                } catch (JSONException e) {
                    fail(CallBackType.GET_PRODUCT_ID, "数据异常");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                fail(CallBackType.GET_PRODUCT_ID, "网络连接异常!");
            }
        });
    }


    private void fail(int returnCode, String errorMessage) {
        if (mIController != null) {
            mIController.onFail(returnCode, errorMessage);
        }
    }

    private void success(int returnCode, String value) {
        if (mIController != null) {
            mIController.onSuccess(returnCode, value);
        }
    }
}
