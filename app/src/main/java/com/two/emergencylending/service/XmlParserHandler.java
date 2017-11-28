package com.two.emergencylending.service;


import com.two.emergencylending.bean.CityModel1;
import com.two.emergencylending.bean.DistrictModel;
import com.two.emergencylending.bean.ProvinceModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlParserHandler extends DefaultHandler {

    /**
     * 存储所有的城市列表的解析对象
     */
    private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();

    public XmlParserHandler() {

    }

    public List<ProvinceModel> getDataList() {
        return provinceList;
    }

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一个开始标签的时候，会触发这个方法
    }

    ProvinceModel provinceModel = new ProvinceModel();
    CityModel1 cityModel = new CityModel1();
    DistrictModel districtModel = new DistrictModel();
/*
  通过getName判断读到哪个标签，然后通过nextText()获取文本节点值，或通过getAttributeValue(i)
 */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // 当遇到开始标记的时候，调用这个方法
        if (qName.equals("province")) {
            provinceModel = new ProvinceModel();
            provinceModel.setProvinceCode(attributes.getValue(0));
            provinceModel.setName(attributes.getValue(1));//获取属性节点值  
            provinceModel.setCityList(new ArrayList<CityModel1>());
        } else if (qName.equals("city")) {
            cityModel = new CityModel1();
            cityModel.setCityCode(attributes.getValue(0));
            cityModel.setName(attributes.getValue(1));
            cityModel.setDistrictList(new ArrayList<DistrictModel>());
        } else if (qName.equals("district")) {
            districtModel = new DistrictModel();
            districtModel.setZipcode(attributes.getValue(0));
            districtModel.setName(attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if (qName.equals("district")) {
            cityModel.getDistrictList().add(districtModel);
        } else if (qName.equals("city")) {
            provinceModel.getCityList().add(cityModel);
        } else if (qName.equals("province")) {
            provinceList.add(provinceModel);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }

}

