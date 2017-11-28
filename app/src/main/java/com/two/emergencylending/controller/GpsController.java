package com.two.emergencylending.controller;

import android.util.Log;

import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.bean.LocationBean;
import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.constant.NetContants;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.http.OKManager;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.utils.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by User on 2017/1/18.
 */

public class GpsController {
    private Map parameter;
    private IControllerCallBack mCall;

    public GpsController(IControllerCallBack call) {
        this.mCall = call;
        parameter = new HashMap();
    }

    public void gpsLocationMsg(LocationBean locationBean) {
        if (locationBean!=null){
            if(StringUtil.isNullOrEmpty(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID)))return;
            if(StringUtil.isNullOrEmpty(SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN)))return;
            if(StringUtil.isNullOrEmpty(UserInfoManager.getInstance().getLocation().getmCurrentProvince()))return;
            if(StringUtil.isNullOrEmpty(UserInfoManager.getInstance().getLocation().getmCurrentCity()))return;
            if(StringUtil.isNullOrEmpty(UserInfoManager.getInstance().getLocation().getmCurrentDistrict()))return;
            if(StringUtil.isNullOrEmpty(UserInfoManager.getInstance().getLocation().getmCurrentStreet()))return;
            if(StringUtil.isNullOrEmpty(UserInfoManager.getInstance().getLocation().getmCurrentStreetNumber()))return;
            if(StringUtil.isNullOrEmpty(String.valueOf( UserInfoManager.getInstance().getLocation().getmCurrentLatitude())))return;
            if(StringUtil.isNullOrEmpty(String.valueOf( UserInfoManager.getInstance().getLocation().getmCurrentLongitude())))return;
            if(StringUtil.isNullOrEmpty( UserInfoManager.getInstance().getLocation().getmCurrentAddrStr()))return;
            parameter.put("juid", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.JUID));
            parameter.put("login_token", SharedPreferencesUtil.getInstance(IApplication.globleContext).getString(SPKey.LOGIN_TOKEN));
            parameter.put("province", UserInfoManager.getInstance().getLocation().getmCurrentProvince());
            parameter.put("city",  UserInfoManager.getInstance().getLocation().getmCurrentCity());
            parameter.put("district", UserInfoManager.getInstance().getLocation().getmCurrentDistrict());
            parameter.put("street_name",  UserInfoManager.getInstance().getLocation().getmCurrentStreet());
            parameter.put("street_number",  UserInfoManager.getInstance().getLocation().getmCurrentStreetNumber());
            parameter.put("x", String.valueOf( UserInfoManager.getInstance().getLocation().getmCurrentLatitude()));
            parameter.put("y", String.valueOf( UserInfoManager.getInstance().getLocation().getmCurrentLongitude()));
            parameter.put("addr_detail",  UserInfoManager.getInstance().getLocation().getmCurrentAddrStr());
            Log.d("GPS", parameter.toString().trim());
        }
     
        OKManager.getInstance().sendComplexForm(NetContants.GET_GPSMES, parameter, new OKManager.Func1() {
            @Override
            public void onResponse(String result) {
                String respond = result.toString();
                Log.d("GPSLocationMSG", respond);
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }
}
