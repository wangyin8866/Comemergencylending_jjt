package com.two.emergencylending.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.two.emergencylending.bean.CenterMessage;
import com.two.emergencylending.constant.DBCode;
import com.two.emergencylending.utils.DataBase;
import com.two.emergencylending.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageManagerDao {
    private Context mContext;
    private DataBase db;
    private SQLiteDatabase sqlite;

    public MessageManagerDao(Context mContext) {
        this.mContext = mContext;
    }

    public void createMessageCacheTable() {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        StringBuffer createSql = new StringBuffer();
        createSql.append("create table if not exists orderMessage(")
                .append("userId varchar(30)")
                .append(",msgId varchar(30)")
                .append(",newsTitle varchar(30)")
                .append(",status varchar(30)")
                .append(",newsDetail varchar(80)")
                .append(",createDate varchar(30));");
        sqlite.execSQL(createSql.toString());
        sqlite.close();
        db.close();
    }

    public void putMessageDate(MessageManagerDao mDao, List<CenterMessage> messages) {
        if (messages != null) {
            for (CenterMessage bean : messages) {
                putMessageDate(mDao, bean);
            }
        }
    }

    public void putMessageDate(MessageManagerDao mDao, CenterMessage message) {
        if (!isExist(message.getId())) {
            HashMap<String, Object> pushData = new HashMap<String, Object>();
            pushData.put("userId", message.getUserId());
            pushData.put("msgId", message.getId());
            pushData.put("newsTitle", message.getNewsTitle());
            pushData.put("status", message.getStatus());
            pushData.put("newsDetail", message.getNewsDetail());
            pushData.put("createDate", message.getCreateDate());
            mDao.insertMessageDateTable(pushData);
        } else {
            deleteMessageById(message.getId());
            HashMap<String, Object> pushData = new HashMap<String, Object>();
            pushData.put("userId", message.getUserId());
            pushData.put("msgId", message.getId());
            pushData.put("newsTitle", message.getNewsTitle());
            pushData.put("status", message.getStatus());
            pushData.put("newsDetail", message.getNewsDetail());
            pushData.put("createDate", message.getCreateDate());
            mDao.insertMessageDateTable(pushData);
        }
    }

    public void updateMessageDate(MessageManagerDao mDao, CenterMessage message) {
        message.setStatus(DBCode.IS_READ);
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String updateMessageSql = "update orderMessage set userId=\""
                + message.getUserId() + "\",newsTitle=\""
                + message.getNewsTitle() + "\",status=\""
                + message.getStatus() + "\",newsDetail=\""
                + message.getNewsDetail() + "\",createDate=\""
                + message.getCreateDate() + "\"where msgId=\""
                + message.getId() + "\";";
        LogUtil.e("更新表", updateMessageSql);
        sqlite.execSQL(updateMessageSql);
        sqlite.close();
        db.close();
    }

    public List<CenterMessage> queryMessages() {
        List<CenterMessage> MessageList = new ArrayList<CenterMessage>();
        try {
            db = new DataBase(mContext);
            sqlite = db.getWritableDatabase();
            Cursor cur = sqlite.query("orderMessage", null, null, null, null, null,
                    "msgId desc");
            while (cur.moveToNext()) {
                CenterMessage map = new CenterMessage();
                map.setUserId(cur.getString(cur.getColumnIndex("userId")));
                map.setId(cur.getString(cur.getColumnIndex("msgId")));
                map.setNewsTitle(cur.getString(cur.getColumnIndex("newsTitle")));
                map.setStatus(cur.getString(cur.getColumnIndex("status")));
                map.setNewsDetail(cur.getString(cur.getColumnIndex("newsDetail")));
                map.setCreateDate(cur.getString(cur.getColumnIndex("createDate")));
                MessageList.add(map);
            }
            sqlite.close();
            db.close();
            return MessageList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageList;
    }

    @SuppressWarnings("unused")
    private void putMessageDate(MessageManagerDao mDao, String userId, String msgId, String newsTitle, String status, String newsDetail, String createDate) {
        if (!isExist(msgId)) {
            HashMap<String, Object> pushData = new HashMap<String, Object>();
            pushData.put("userId", userId);
            pushData.put("msgId", msgId);
            pushData.put("newsTitle", newsTitle);
            pushData.put("status", status);
            pushData.put("newsDetail", newsDetail);
            pushData.put("createDate", createDate);
            mDao.insertMessageDateTable(pushData);
        }
    }

    @SuppressWarnings("unused")
    private void insertMessageDateTable(Map<String, Object> info) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String insertData = "insert into orderMessage values(\""
                + info.get("userId") + "\",\""
                + info.get("msgId") + "\",\""
                + info.get("newsTitle") + "\",\""
                + info.get("status") + "\",\""
                + info.get("newsDetail") + "\",\""
                + info.get("createDate") + "\");";
        sqlite.execSQL(insertData);
        sqlite.close();
        db.close();
    }

    public List<HashMap<String, Object>> queryMessageInfo() {
        List<HashMap<String, Object>> retData = new ArrayList<HashMap<String, Object>>();
        try {
            db = new DataBase(mContext);
            sqlite = db.getWritableDatabase();
            Cursor cur = sqlite.query("orderMessage", null, null, null, null, null,
                    "msgId desc");
            while (cur.moveToNext()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("userId",
                        cur.getString(cur.getColumnIndex("userId")));
                map.put("msgId",
                        cur.getString(cur.getColumnIndex("msgId")));
                map.put("newsTitle",
                        cur.getString(cur.getColumnIndex("newsTitle")));
                map.put("status",
                        cur.getString(cur.getColumnIndex("status")));
                map.put("newsDetail", cur.getString(cur.getColumnIndex("newsDetail")));
                map.put("createDate", cur.getString(cur.getColumnIndex("createDate")));
                retData.add(map);
            }
            sqlite.close();
            db.close();
            return retData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retData;
    }

    public boolean isExist(String msgId) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String messageSql = "select COUNT(*) from orderMessage where msgId=\""
                + msgId + "\";";
        Cursor cur = sqlite.rawQuery(messageSql, null);
        if (cur.moveToNext()) {
            int count = cur.getInt(0);
            if (count > 0) {
                return true;
            }
        }
        return false;
    }

    private HashMap<String, Object> queryMessageInfoById(String[] str) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Cursor cur = sqlite.query("orderMessage", null, "msgId=?", str,
                null, null, null, null);
        while (cur.moveToNext()) {
            map.put("msgId",
                    cur.getString(cur.getColumnIndex("msgId")));
            map.put("userId",
                    cur.getString(cur.getColumnIndex("userId")));
            map.put("newsTitle",
                    cur.getString(cur.getColumnIndex("newsTitle")));
            map.put("status",
                    cur.getString(cur.getColumnIndex("status")));
            map.put("newsDetail", cur.getString(cur.getColumnIndex("newsDetail")));
            map.put("createDate", cur.getString(cur.getColumnIndex("createDate")));
        }
        sqlite.close();
        db.close();
        return map;
    }

    public void deleteMessageById(String msgId) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String deleteMessageSql = "delete from orderMessage where msgId=\""
                + msgId + "\";";
        LogUtil.d("Dao",deleteMessageSql);
        sqlite.execSQL(deleteMessageSql);
        sqlite.close();
        db.close();
    }

    public void deleteMessageByUserId(String userId) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String deleteMessageSql = "delete from orderMessage where userId=\""
                + userId + "\";";
        LogUtil.d("Dao",deleteMessageSql);
        sqlite.execSQL(deleteMessageSql);
        sqlite.close();
        db.close();
    }

    public void deleteMessageByTime(String time) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String deleteMessageSql = "delete from orderMessage where createDate=\""
                + time + "\";";
        LogUtil.d("Dao",deleteMessageSql);
        sqlite.execSQL(deleteMessageSql);
        sqlite.close();
        db.close();
    }

    public void deleteAllMessage() {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        // sqlite.beginTransaction();
        String deleteAllMessageSql = "delete from orderMessage;";
        sqlite.execSQL(deleteAllMessageSql);
        // sqlite.endTransaction();
        sqlite.close();
        db.close();
    }

    public void updateMessageStatus(String msgId) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String updateMessageSql = "update orderMessage set status=\"" + DBCode.IS_READ + "\"where msgId=\""
                + msgId + "\";";
        LogUtil.e("更新表", updateMessageSql);
        sqlite.execSQL(updateMessageSql);
        sqlite.close();
        db.close();
    }

    public void deleteThisTable() {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String deleteThisTableSql = "drop table orderMessage;";
        sqlite.execSQL(deleteThisTableSql);
        sqlite.close();
        db.close();
    }
}
