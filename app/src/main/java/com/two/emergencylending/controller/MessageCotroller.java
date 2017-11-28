package com.two.emergencylending.controller;

import android.app.Activity;

import com.two.emergencylending.application.IApplication;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 项目名称：急借通
 * 类描述：消息接口
 * 创建人：szx
 * 创建时间：2016/8/25 9:50
 * 修改人：szx
 * 修改时间：2016/8/25 9:50
 * 修改备注：
 */
public class MessageCotroller {
    private static final String TAG = MessageCotroller.class.getSimpleName();
    Activity mActivity;
    Map<String, String> parameter;
    ControllerCallBack mController;

    public MessageCotroller(Activity activity, ControllerCallBack controller) {
        this.mActivity = activity;
        this.mController = controller;
        parameter = new HashMap<>();
    }

    public void getMessageReadCount(String pageNo, String pageSize) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("pageNo", pageNo);
        parameter.put("pageSize", pageSize);
        parameter.put("status", "1");
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        OKManager.getInstance().sendComplexForm(NetContants.GET_MESSAGE, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            String res = Des3.decode(json.getString("result"));
                            success(CallBackType.MESSAGE_READ_COUNT, res);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.MESSAGE_READ_COUNT, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.MESSAGE_READ_COUNT, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.MESSAGE_READ_COUNT, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.MESSAGE_READ_COUNT, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }


    public void getMessage(String pageNo, String pageSize) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("pageNo", pageNo);
        parameter.put("pageSize", pageSize);
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "加载中...");
        OKManager.getInstance().sendComplexForm(NetContants.GET_MESSAGE, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            String res = Des3.decode(json.getString("result"));
                            success(CallBackType.MESSAGE_GET, res);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.MESSAGE_GET, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.MESSAGE_GET, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.MESSAGE_GET, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.MESSAGE_GET, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void readMessage(String id) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("id", id);
        parameter.put("status", "2");
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        OKManager.getInstance().sendComplexForm(NetContants.UPDATE_MESSAGE, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            String res = "";
                            success(CallBackType.MESSAGE_READ, res);
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.MESSAGE_READ, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.MESSAGE_READ, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.MESSAGE_READ, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.MESSAGE_READ, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void deleteMessageById(String id) {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("id", id);
        parameter.put("delFlag", "1");
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "删除选中消息...");
        OKManager.getInstance().sendComplexForm(NetContants.UPDATE_MESSAGE, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            success(CallBackType.MESSAGE_DEL, "");
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.MESSAGE_DEL, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.MESSAGE_DEL, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.MESSAGE_DEL, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.MESSAGE_DEL, ErrorCode.FAIL_NETWORK);
                e.printStackTrace();
            }
        });
    }

    public void deleteAllMessage() {
        if (!CommonUtils.isNetAvailable()) return;
        parameter.put("delFlag", "1");
        parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
        parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
        CommonUtils.showDialog(mActivity, "清空消息...");
        OKManager.getInstance().sendComplexForm(NetContants.UPDATE_MESSAGE, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                CommonUtils.closeDialog();
                String response = result.toString().trim();
                if (!StringUtil.isNullOrEmpty(result)) {
                    LogUtil.d(TAG, response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String code = json.getString("flag");
                        String msg = json.getString("msg");
                        if (code.equals(ErrorCode.SUCCESS)) {
                            success(CallBackType.MESSAGE_DEL_ALL, "");
                        } else if (code.equals(ErrorCode.EQUIPMENT_KICKED_OUT)) {
                            IApplication.getInstance().backToLogin(mActivity);
                        } else {
                            LogUtil.d(TAG, msg);
                            fail(CallBackType.MESSAGE_DEL_ALL, msg);
                        }
                    } catch (Exception e) {
                        LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                        fail(CallBackType.MESSAGE_DEL_ALL, ErrorCode.FAIL_DATA);
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d(TAG, ErrorCode.FAIL_DATA);
                    fail(CallBackType.MESSAGE_DEL_ALL, ErrorCode.FAIL_DATA);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                CommonUtils.closeDialog();
                LogUtil.d(TAG, ErrorCode.FAIL_NETWORK);
                fail(CallBackType.MESSAGE_DEL_ALL, ErrorCode.FAIL_NETWORK);
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
