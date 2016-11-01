package com.timestudio.mynews.util.parser;

import android.util.Log;

import com.timestudio.mynews.entity.CommentEntity;
import com.timestudio.mynews.entity.NewsTitle;
import com.timestudio.mynews.entity.NewsTitleHorizontal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/10/26.
 * 解析新闻的类
 */

public class ParserNews {
    /**
     * 解析新闻的类型
     */
    public static ArrayList<NewsTitleHorizontal> parserNewsType(String json) {
        ArrayList<NewsTitleHorizontal> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            String message = object.getString("message");
            if (message.equals("OK")) {
                JSONArray dataArray = object.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject object1 = dataArray.getJSONObject(i);
                    JSONArray subgrpArray = object1.getJSONArray("subgrp");
                    for (int j = 0; j < subgrpArray.length(); j++) {
                        JSONObject object2 = subgrpArray.getJSONObject(j);
                        list.add(new NewsTitleHorizontal(object2.getString("subgroup"),
                                object2.getInt("subid")));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 解析新闻的列表
     */
    public static ArrayList<NewsTitle> parserNewsList(String json) {
        ArrayList<NewsTitle> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            String message = object.getString("message");
            String status = object.getString("status");
            if (message.equals("OK") && status.equals("0")) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String summary = obj.getString("summary");
                    String title = obj.getString("title");
                    String icon = obj.getString("icon");
                    String stamp = obj.getString("stamp");
                    int nid = obj.getInt("nid");
                    String link = obj.getString("link");
                    list.add(new NewsTitle(nid, stamp, icon, title, summary, link));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 解析评论
     */
    public static ArrayList<CommentEntity> parserCommentList(String json) {
        ArrayList<CommentEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            if (object.getString("message").equals("OK") &&
                    object.getString("status").equals("0")) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject ob = array.getJSONObject(i);
                    String uid = ob.getString("uid");
                    String content = ob.getString("content");
                    String stamp = ob.getString("stamp");
                    int cid = ob.getInt("cid");
                    String portrait = ob.getString("portrait");
                    list.add(new CommentEntity(uid, content, stamp, cid, portrait));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
