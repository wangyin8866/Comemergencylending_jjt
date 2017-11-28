package com.two.emergencylending.dao;

import android.database.sqlite.SQLiteDatabase;

import com.chinazyjr.lib.base.BaseSqlDao;
import com.two.emergencylending.bean.CityBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityDao {
    private SQLiteDatabase mDb;
    private static String dbName = "citys.db";
    private static String table = "REGIONS";
    private BaseSqlDao baseDao;

    public CityDao() {
        baseDao = BaseSqlDao.getInstance();
        mDb = baseDao.getDb(dbName);
    }


    public CityBean getCity(String id) {
        Map<String, String> wheres = new HashMap<String, String>();
        wheres.put("_id", id);
        String[] coulums = {"_id", "parent", "name", "type", "firstName"};
        Map<String, String> dicMap = (Map<String, String>) baseDao.selectMaps(mDb, table, coulums, wheres);
        CityBean bean = new CityBean();
        bean.setId(dicMap.get("_id"));
        bean.setName(dicMap.get("name"));
        bean.setParent(dicMap.get("parent"));
        bean.setType(dicMap.get("type"));
        bean.setFirstName(dicMap.get("firstName"));
        return bean;
    }

    public List<CityBean> getAllCitys() {
        String key = "_id";
        String[] coulums = {"_id", "parent", "name", "type", "firstName"};
        List<Map<String, String>> cityList = (List<Map<String, String>>) baseDao.selectAllMaps(mDb, table, coulums, key);
        List<CityBean> cityBeans = new ArrayList<CityBean>();
        for (Map<String, String> dicMap : cityList) {
            CityBean bean = new CityBean();
            bean.setId(dicMap.get("_id"));
            bean.setName(dicMap.get("name"));
            bean.setParent(dicMap.get("parent"));
            bean.setType(dicMap.get("type"));
            bean.setFirstName(dicMap.get("firstName"));
            cityBeans.add(bean);
        }
        return cityBeans;
    }

    public List<CityBean> getAllProvince() {
        String key = "_id";
        String[] coulums = {"_id", "parent", "name", "type", "firstName"};
        List<Map<String, String>> cityList = (List<Map<String, String>>) baseDao.selectAllMaps(mDb, table, coulums, key);
        List<CityBean> cityBeans = new ArrayList<CityBean>();
        for (Map<String, String> dicMap : cityList) {
            CityBean bean = new CityBean();
            bean.setId(dicMap.get("_id"));
            bean.setName(dicMap.get("name"));
            bean.setParent(dicMap.get("parent"));
            bean.setType(dicMap.get("type"));
            bean.setFirstName(dicMap.get("firstName"));
            if (bean.getType().equals("2")) {
                cityBeans.add(bean);
            }
        }
        return cityBeans;
    }


    public boolean deleteDic(String id) {
        Map<String, String> wheres = new HashMap<String, String>();
        wheres.put("_id", id);
        return baseDao.delData(mDb, table, wheres);
    }
}
