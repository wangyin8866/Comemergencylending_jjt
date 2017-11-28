package com.two.emergencylending.bean;

/**
 * Created by User on 2017/3/21.
 */

public class LocationBean {
    public String mCurrentProvince;
    public String mCurrentDistrict;
    public String mCurrentCity;
    public Double mCurrentLatitude;
    public Double mCurrentLongitude;
    public String mCurrentStreet;
    public String mCurrentStreetNumber;
    public String mCurrentAddrStr;

    public LocationBean() {
    }

    public String getmCurrentProvince() {
        return mCurrentProvince;
    }

    public void setmCurrentProvince(String mCurrentProvince) {
        this.mCurrentProvince = mCurrentProvince;
    }

    public String getmCurrentDistrict() {
        return mCurrentDistrict;
    }

    public void setmCurrentDistrict(String mCurrentDistrict) {
        this.mCurrentDistrict = mCurrentDistrict;
    }

    public String getmCurrentCity() {
        return mCurrentCity;
    }

    public void setmCurrentCity(String mCurrentCity) {
        this.mCurrentCity = mCurrentCity;
    }

    public Double getmCurrentLatitude() {
        return mCurrentLatitude;
    }

    public void setmCurrentLatitude(Double mCurrentLatitude) {
        this.mCurrentLatitude = mCurrentLatitude;
    }

    public Double getmCurrentLongitude() {
        return mCurrentLongitude;
    }

    public void setmCurrentLongitude(Double mCurrentLongitude) {
        this.mCurrentLongitude = mCurrentLongitude;
    }

    public String getmCurrentStreet() {
        return mCurrentStreet;
    }

    public void setmCurrentStreet(String mCurrentStreet) {
        this.mCurrentStreet = mCurrentStreet;
    }

    public String getmCurrentStreetNumber() {
        return mCurrentStreetNumber;
    }

    public void setmCurrentStreetNumber(String mCurrentStreetNumber) {
        this.mCurrentStreetNumber = mCurrentStreetNumber;
    }

    public String getmCurrentAddrStr() {
        return mCurrentAddrStr;
    }

    public void setmCurrentAddrStr(String mCurrentAddrStr) {
        this.mCurrentAddrStr = mCurrentAddrStr;
    }

    @Override
    public String toString() {
        return "LocationBean{" +
                "mCurrentProvince='" + mCurrentProvince + '\'' +
                ", mCurrentDistrict='" + mCurrentDistrict + '\'' +
                ", mCurrentCity='" + mCurrentCity + '\'' +
                ", mCurrentLatitude=" + mCurrentLatitude +
                ", mCurrentLongitude=" + mCurrentLongitude +
                ", mCurrentStreet='" + mCurrentStreet + '\'' +
                ", mCurrentStreetNumber='" + mCurrentStreetNumber + '\'' +
                ", mCurrentAddrStr='" + mCurrentAddrStr + '\'' +
                '}';
    }
}
