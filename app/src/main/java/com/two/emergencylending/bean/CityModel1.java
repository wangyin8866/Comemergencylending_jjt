package com.two.emergencylending.bean;

import java.util.List;

public class CityModel1 {
    private String name;
    private List<DistrictModel> districtList;
    private String cityCode;

    public CityModel1() {
        super();
    }

    public CityModel1(String cityCode) {
        this.cityCode = cityCode;
    }

    public CityModel1(String name, List<DistrictModel> districtList) {
        super();
        this.name = name;
        this.districtList = districtList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public List<DistrictModel> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<DistrictModel> districtList) {
        this.districtList = districtList;
    }

    @Override
    public boolean equals(Object o) {
        return cityCode.equals(((CityModel1) o).cityCode);
    }

    @Override
    public String toString() {
        return "CityModel1{" +
                "name='" + name + '\'' +
                ", districtList=" + districtList +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }

}
