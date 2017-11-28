package com.two.emergencylending.bean;

import com.two.emergencylending.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2016/12/7.
 */

public class PerSonalBean implements Serializable {
    private String idcard_z;
    private String idCardFId;
    private String bank_code;
    private String bankcard_no;
    private String bankcard_f;
    private String education;

    private String huji_province;
    private String huji_city;
    private String huji_county;
    private String huji_adr_detail;
    private String huji_adr;

    private String live_province;
    private String live_city;
    private String live_county;
    private String live_adr_detail;
    private String live_adr;


    private String unit_province;
    private String unit_city;
    private String unit_county;
    private String unit_adr_detail;
    private String unit_adr;


    private String bankcard_must;
    private String week_max_repment;
    private String bank_phone;
    private String username;
    private String bankCardFId;
    private String idcard_must;
    private String live_status;
    private String wechat_no;
    private String customer_source;
    private String child_num;
    private String idcard;
    private String bankCardZId;
    private String marriage;
    private String idCardZId;
    private String idcard_f;
    private String qq_no;
    private String bankcard_z;

    private String idcard_hand;
    private String idCardHandId;
    private String bank_name;
    private List<OpenBank> offline_bank_list;
    private List<OpenBank> online_bank_list;
    private String last_product_id;
    private String product_id;
    private String result;

    public String getIdcard_z() {
        return idcard_z;
    }

    public void setIdcard_z(String idcard_z) {
        this.idcard_z = idcard_z;
    }

    public String getLive_adr_detail() {
        return live_adr_detail;
    }

    public void setLive_adr_detail(String live_adr_detail) {
        this.live_adr_detail = live_adr_detail;
    }

    public String getIdCardFId() {
        return idCardFId;
    }

    public void setIdCardFId(String idCardFId) {
        this.idCardFId = idCardFId;
    }

    public String getLive_province() {
        return live_province;
    }

    public void setLive_province(String live_province) {
        this.live_province = live_province;
    }


    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }


    public String getBankcard_no() {
        return bankcard_no;
    }

    public void setBankcard_no(String bankcard_no) {
        this.bankcard_no = bankcard_no;
    }

    public String getBankcard_f() {
        return bankcard_f;
    }

    public void setBankcard_f(String bankcard_f) {
        this.bankcard_f = bankcard_f;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getHuji_city() {
        return huji_city;
    }

    public void setHuji_city(String huji_city) {
        this.huji_city = huji_city;
    }

    public String getHuji_province() {
        return huji_province;
    }

    public void setHuji_province(String huji_province) {
        this.huji_province = huji_province;
    }

    public String getBankcard_must() {
        return bankcard_must;
    }

    public void setBankcard_must(String bankcard_must) {
        this.bankcard_must = bankcard_must;
    }

    public String getBank_phone() {
        return bank_phone;
    }

    public void setBank_phone(String bank_phone) {
        this.bank_phone = bank_phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHuji_adr_detail() {
        return huji_adr_detail;
    }

    public void setHuji_adr_detail(String huji_adr_detail) {
        this.huji_adr_detail = huji_adr_detail;
    }

    public String getBankCardFId() {
        return bankCardFId;
    }

    public void setBankCardFId(String bankCardFId) {
        this.bankCardFId = bankCardFId;
    }

    public String getIdcard_must() {
        return idcard_must;
    }

    public void setIdcard_must(String idcard_must) {
        this.idcard_must = idcard_must;
    }

    public String getLive_status() {
        return live_status;
    }

    public void setLive_status(String live_status) {
        this.live_status = live_status;
    }

    public String getLive_city() {
        return live_city;
    }

    public void setLive_city(String live_city) {
        this.live_city = live_city;
    }

    public String getWechat_no() {
        return wechat_no;
    }

    public void setWechat_no(String wechat_no) {
        this.wechat_no = wechat_no;
    }

    public String getCustomer_source() {
        return customer_source;
    }

    public void setCustomer_source(String customer_source) {
        this.customer_source = customer_source;
    }

    public String getLive_county() {
        return live_county;
    }

    public void setLive_county(String live_county) {
        this.live_county = live_county;
    }

    public String getChild_num() {
        return child_num;
    }

    public void setChild_num(String child_num) {
        this.child_num = child_num;
    }

    public String getHuji_adr() {
        return huji_adr;
    }

    public void setHuji_adr(String huji_adr) {
        this.huji_adr = huji_adr;
    }

    public String getLive_adr() {
        return live_adr;
    }

    public void setLive_adr(String live_adr) {
        this.live_adr = live_adr;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getBankCardZId() {
        return bankCardZId;
    }

    public void setBankCardZId(String bankCardZId) {
        this.bankCardZId = bankCardZId;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getIdCardZId() {
        return idCardZId;
    }

    public void setIdCardZId(String idCardZId) {
        this.idCardZId = idCardZId;
    }

    public String getIdcard_f() {
        return idcard_f;
    }

    public void setIdcard_f(String idcard_f) {
        this.idcard_f = idcard_f;
    }

    public String getQq_no() {
        return qq_no;
    }

    public void setQq_no(String qq_no) {
        this.qq_no = qq_no;
    }

    public String getBankcard_z() {
        return bankcard_z;
    }

    public void setBankcard_z(String bankcard_z) {
        this.bankcard_z = bankcard_z;
    }

    public String getHuji_county() {
        return huji_county;
    }

    public void setHuji_county(String huji_county) {
        this.huji_county = huji_county;
    }

    public String getWeek_max_repment() {
        return week_max_repment;
    }

    public void setWeek_max_repment(String week_max_repment) {
        this.week_max_repment = week_max_repment;
    }

    public String getIdcard_hand() {
        return idcard_hand;
    }

    public void setIdcard_hand(String idcard_hand) {
        this.idcard_hand = idcard_hand;
    }

    public String getIdCardHandId() {
        return idCardHandId;
    }

    public void setIdCardHandId(String idCardHandId) {
        this.idCardHandId = idCardHandId;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public List<OpenBank> getOffline_bank_list() {
        return offline_bank_list;
    }

    public void setOffline_bank_list(List<OpenBank> offline_bank_list) {
        this.offline_bank_list = offline_bank_list;
    }

    public List<OpenBank> getOnline_bank_list() {
        return online_bank_list;
    }

    public void setOnline_bank_list(List<OpenBank> online_bank_list) {
        this.online_bank_list = online_bank_list;
    }

    public String getLast_product_id() {
        return last_product_id;
    }

    public void setLast_product_id(String last_product_id) {
        this.last_product_id = last_product_id;
    }

    public String getUnit_adr() {
        return unit_adr;
    }

    public void setUnit_adr(String unit_adr) {
        this.unit_adr = unit_adr;
    }

    public String getUnit_adr_detail() {
        return unit_adr_detail;
    }

    public void setUnit_adr_detail(String unit_adr_detail) {
        this.unit_adr_detail = unit_adr_detail;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getResult() {
        return StringUtil.isEmpty(result)?"":result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUnit_province() {
        return unit_province;
    }

    public void setUnit_province(String unit_province) {
        this.unit_province = unit_province;
    }

    public String getUnit_city() {
        return unit_city;
    }

    public void setUnit_city(String unit_city) {
        this.unit_city = unit_city;
    }

    public String getUnit_county() {
        return unit_county;
    }

    public void setUnit_county(String unit_county) {
        this.unit_county = unit_county;
    }

    @Override
    public String toString() {
        return "PerSonalBean{" +
                "idcard_z='" + idcard_z + '\'' +
                ", idCardFId='" + idCardFId + '\'' +
                ", bank_code='" + bank_code + '\'' +
                ", bankcard_no='" + bankcard_no + '\'' +
                ", bankcard_f='" + bankcard_f + '\'' +
                ", education='" + education + '\'' +
                ", huji_province='" + huji_province + '\'' +
                ", huji_city='" + huji_city + '\'' +
                ", huji_county='" + huji_county + '\'' +
                ", huji_adr_detail='" + huji_adr_detail + '\'' +
                ", huji_adr='" + huji_adr + '\'' +
                ", live_province='" + live_province + '\'' +
                ", live_city='" + live_city + '\'' +
                ", live_county='" + live_county + '\'' +
                ", live_adr_detail='" + live_adr_detail + '\'' +
                ", live_adr='" + live_adr + '\'' +
                ", unit_province='" + unit_province + '\'' +
                ", unit_city='" + unit_city + '\'' +
                ", unit_county='" + unit_county + '\'' +
                ", unit_adr_detail='" + unit_adr_detail + '\'' +
                ", unit_adr='" + unit_adr + '\'' +
                ", bankcard_must='" + bankcard_must + '\'' +
                ", week_max_repment='" + week_max_repment + '\'' +
                ", bank_phone='" + bank_phone + '\'' +
                ", username='" + username + '\'' +
                ", bankCardFId='" + bankCardFId + '\'' +
                ", idcard_must='" + idcard_must + '\'' +
                ", live_status='" + live_status + '\'' +
                ", wechat_no='" + wechat_no + '\'' +
                ", customer_source='" + customer_source + '\'' +
                ", child_num='" + child_num + '\'' +
                ", idcard='" + idcard + '\'' +
                ", bankCardZId='" + bankCardZId + '\'' +
                ", marriage='" + marriage + '\'' +
                ", idCardZId='" + idCardZId + '\'' +
                ", idcard_f='" + idcard_f + '\'' +
                ", qq_no='" + qq_no + '\'' +
                ", bankcard_z='" + bankcard_z + '\'' +
                ", idcard_hand='" + idcard_hand + '\'' +
                ", idCardHandId='" + idCardHandId + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", offline_bank_list=" + offline_bank_list +
                ", online_bank_list=" + online_bank_list +
                ", last_product_id='" + last_product_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
