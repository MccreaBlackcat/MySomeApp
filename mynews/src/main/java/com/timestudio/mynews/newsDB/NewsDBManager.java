package com.timestudio.mynews.newsDB;

import com.timestudio.mynews.entity.NewsTitle;
import com.timestudio.mynews.entity.NewsTitleHorizontal;
import com.timestudio.mynews.entry.NewsTitleEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/10/25.
 * @description 新闻数据库操作管理类
 */

public class NewsDBManager {
    private DBHelper db;

    public NewsDBManager(Context context) {
        this.db = new DBHelper(context);
    }

    /**
     * 添加新闻类型函数
     */
    public void insertNewsType(NewsTitleHorizontal titleHorizontal) {
        SQLiteDatabase sqLite = null;
        sqLite = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NewsTitleEntry.COLUMNS_NAME_SUBGROUP,titleHorizontal.getSubgroup());
        values.put(NewsTitleEntry.COLUMNS_NAME_SUBID,titleHorizontal.getSubid());
        sqLite.insert(
                NewsTitleEntry.TABLE_NAME_TYPE,
                null,
                values
        );
    }

    /**
     * 查询新闻类型函数
     */
    public ArrayList<NewsTitleHorizontal> queryNewsType() {
        ArrayList<NewsTitleHorizontal> list = new ArrayList<>();
        SQLiteDatabase sql = null;
        Cursor cursor = null;
        sql = db.getReadableDatabase();
        cursor = sql.query(
                NewsTitleEntry.TABLE_NAME_TYPE,
                new String[]{"*"},
                null,null,null,null,null,null
        );
        if (cursor != null && cursor.getCount() > 1) {
            cursor.moveToFirst();
            int subgroupIndex = cursor.getColumnIndexOrThrow(NewsTitleEntry.COLUMNS_NAME_SUBGROUP);
            int subidIndex = cursor.getColumnIndexOrThrow(NewsTitleEntry.COLUMNS_NAME_SUBID);
            do {
                list.add(new NewsTitleHorizontal(cursor.getString(subgroupIndex),cursor.getInt(subidIndex)));
            } while (cursor.moveToNext());
            if (sql != null) {
                sql.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 添加新闻数据
     */
    public void inserNews(NewsTitle newsTitle) {
        SQLiteDatabase sqLite = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NewsTitleEntry.COLUMNS_NAME_NID, newsTitle.getNid());
        values.put(NewsTitleEntry.COLUMNS_NAME_STAMP, newsTitle.getStamp());
        values.put(NewsTitleEntry.COLUMNS_NAME_ICON, newsTitle.getIcon());
        values.put(NewsTitleEntry.COLUMNS_NAME_TITLE, newsTitle.getTitle());
        values.put(NewsTitleEntry.COLUMNS_NAME_SUMMARY, newsTitle.getSummary());
        values.put(NewsTitleEntry.COLUMNS_NAME_LINK, newsTitle.getLink());
        sqLite.insert(NewsTitleEntry.TABLE_NAME_LIST, null, values);
        sqLite.close();
    }
    /**
     * 查询新闻数据
     */

    public ArrayList<NewsTitle> queryNews(int count, int offset) {
        ArrayList<NewsTitle> newsList = new ArrayList<NewsTitle>();
        SQLiteDatabase sqLite=db.getWritableDatabase();
        String sql="select * from news where order by _id desc limit "+count+" offset "+offset;
        Cursor cursor=sqLite.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int nid = cursor.getInt(cursor.getColumnIndex("nid"));
                String stamp = cursor.getString(cursor.getColumnIndex("stamp"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                String link = cursor.getString(cursor.getColumnIndex("link"));
                NewsTitle news = new NewsTitle(nid, stamp, icon, title, summary, link);
                newsList.add(news);
            } while (cursor.moveToNext());
            cursor.close();
            sqLite.close();
        }
        return newsList;
    }
}
