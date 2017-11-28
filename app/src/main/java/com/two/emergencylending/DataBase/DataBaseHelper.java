package com.two.emergencylending.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：User on 2015/9/11 09:50
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static String dbName = "loanUser.db";
    public static int version = 1;
    public static String tableName = "loanUser";

    public DataBaseHelper(Context context) {
        super(context, dbName, null, version);
    }

    // 只在初次使用数据库时会自动调用一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库，对数据库进行操作
        String sql = "create table loanUser("+ "realName varchar(50)," + "createDate datetime," + "loanAmount varchar(100)" + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public String insertLoanUser(String realName, String createDate, String loanAmount) {
        final String insertUserDeal = "insert into loanUser values(\"" + realName + "\"," + "\"" + createDate + "\"," + "\"" + loanAmount + "\");";
        return insertUserDeal;
    }

    public String deleteTable(){
        final String table = "delete from loanUser";
        return table;
    }

    public String selectAll(){
        final  String table = "select * from loanUser";
        return table;
    }
}
