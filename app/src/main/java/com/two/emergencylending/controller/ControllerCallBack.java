package com.two.emergencylending.controller;

/**
 * 项目名称：急借通
 * 类描述：网络请求回调
 * 创建人：szx
 * 创建时间：2016/7/6 11:06
 * 修改人：szx
 * 修改时间：2016/7/6 11:06
 * 修改备注：
 */
public interface ControllerCallBack {
    void onSuccess(int returnCode, String value);

    void onFail(int returnCode, String errorMessage);
}
