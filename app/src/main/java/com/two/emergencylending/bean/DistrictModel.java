package com.two.emergencylending.bean;

public class DistrictModel {
    private String name;
    private String zipcode;

    public DistrictModel() {
        super();
    }

    public DistrictModel(String zipcode) {
        this.zipcode = zipcode;
    }

    public DistrictModel(String name, String zipcode) {
        super();
        this.name = name;
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public boolean equals(Object o) {
        return zipcode.equals(((DistrictModel) o).zipcode);
    }

    @Override
    public String toString() {
        return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
    }

}
