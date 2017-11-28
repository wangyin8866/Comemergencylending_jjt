package com.two.emergencylending.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.two.emergencylending.bean.Notice;
import com.two.emergencylending.utils.DataBase;
import com.two.emergencylending.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeDao {
    private String table = "notice";
    private Context mContext;
    private DataBase db;
    private SQLiteDatabase sqlite;

    public NoticeDao(Context mContext) {
        this.mContext = mContext;
    }

    public void createNoticeTable() {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        StringBuffer createSql = new StringBuffer();
        createSql.append("create table if not exists " + table + "(")
                .append("userId varchar(30)")
                .append(",type varchar(30)")
                .append(",hasNotice varchar(30));");
        sqlite.execSQL(createSql.toString());
        sqlite.close();
        db.close();
    }

    public void putNoticeDate(NoticeDao mDao, List<Notice> notices) {
        for (Notice bean : notices) {
            putNoticeDate(mDao, bean);
        }
    }

    public void putNoticeDate(NoticeDao mDao, Notice notice) {
        HashMap<String, Object> pushData = new HashMap<String, Object>();
        pushData.put("userId", notice.getUserId());
        pushData.put("type", notice.getType());
        pushData.put("hasNotice", notice.getHasNotice());
        mDao.insertNoticeTable(pushData);
    }

    public List<Notice> queryNoices() {
        List<Notice> noticeList = new ArrayList<Notice>();
        try {
            db = new DataBase(mContext);
            sqlite = db.getWritableDatabase();
            Cursor cur = sqlite.query(table, null, null, null, null, null,
                    "userId desc");
            while (cur.moveToNext()) {
                Notice map = new Notice();
                map.setUserId(cur.getString(cur.getColumnIndex("userId")));
                map.setType(cur.getString(cur.getColumnIndex("type")));
                map.setHasNotice(cur.getString(cur.getColumnIndex("hasNotice")));
                noticeList.add(map);
            }
            sqlite.close();
            db.close();
            return noticeList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noticeList;
    }


    public void insertNoticeTable(Map<String, Object> info) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String insertData = "insert into " + table + " values(\""
                + info.get("userId") + "\",\""
                + info.get("type") + "\",\""
                + info.get("hasNotice") + "\");";
        sqlite.execSQL(insertData);
        sqlite.close();
        db.close();
    }

    public void updateNoticeByUserIdAndType(NoticeDao mDao, Notice notice) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String updateNoticeSql = "update " + table + " set userId=\""
                + notice.getHasNotice() + "\"where userId=\"" + notice.getUserId() + "\"and type=\"" + notice.getType() + "\";";
        LogUtil.e("更新表", updateNoticeSql);
        sqlite.execSQL(updateNoticeSql);
        sqlite.close();
        db.close();
    }

    public void updateNoticeByUserIdAndType(NoticeDao mDao, String userId, String type, String hasNotice) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String updateNoticeSql = "update " + table + " set userId=\""
                + hasNotice + "\"where userId=\"" + userId + "\"and type=\"" + type + "\";";
        LogUtil.e("更新表", updateNoticeSql);
        sqlite.execSQL(updateNoticeSql);
        sqlite.close();
        db.close();
    }

    public boolean isExist(String userId, String type) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String moticeSql = "select COUNT(*) from " + table + " where userId=\"" + userId + "\"and type=\"" + type + "\";";
        Cursor cur = sqlite.rawQuery(moticeSql, null);
        if (cur.moveToNext()) {
            int count = cur.getInt(0);
            if (count > 0) {
                return true;
            }
        }
        return false;
    }

    public void deleteNoticeByUserIdAndType(String UserId, String type) {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String deleteNoticeSql = "delete from " + table + " where UserId=\""
                + UserId + "\"and type= \"" + type + "\";";
        LogUtil.d("NoticeDao",deleteNoticeSql);
        sqlite.execSQL(deleteNoticeSql);
        sqlite.close();
        db.close();
    }

    public void deleteAllNotice() {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String deleteAllNoticeSql = "delete from " + table + ";";
        sqlite.execSQL(deleteAllNoticeSql);
        sqlite.close();
        db.close();
    }

    public void deleteThisTable() {
        db = new DataBase(mContext);
        sqlite = db.getWritableDatabase();
        String deleteThisTableSql = "drop table " + table + ";";
        sqlite.execSQL(deleteThisTableSql);
        sqlite.close();
        db.close();
    }
}
