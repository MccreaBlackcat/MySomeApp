package com.timestudio.mynews.newsDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hasee on 2016/10/25.
 * @description 数据库帮助类
 */

public class DBHelper extends SQLiteOpenHelper{

    private static String DB_NAME = "news.db";
    private static int DB_VERSION = 1 ;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table newsType (" +
                "_id integer primary key autoincrement" +
                ",subgroup text," +
                "subid integer)");
        db.execSQL("create table news (" +
                "_id integer primary key autoincrement," +
                "nid integer," +
                "stamp text," +
                "icon text," +
                "title text," +
                "summary text," +
                "link text"+
                "subid integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
