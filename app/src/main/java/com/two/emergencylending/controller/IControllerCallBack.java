package com.two.emergencylending.controller;

/**
 * Created by User on 2016/8/15.
 */
public interface IControllerCallBack {
    void onSuccess(int returnCode, String value);

    void onFail(String errorMessage);
}
