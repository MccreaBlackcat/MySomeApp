package com.timestudio.mynews.entity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by thinkpad on 2016/10/18.
 */

public class NewsTitle implements Serializable{

    private int nid; //新闻编号
    private String stamp; //新闻时间戳
    private String icon; //图标路径
    private String title; //新闻标题
    private String summary; //新闻摘要
    private String link; //新闻链接
    private int subid;

    public NewsTitle(int nid, String stamp, String icon, String title, String summary, String link) {
        this.nid = nid;
        this.stamp = stamp;
        this.icon = icon;
        this.title = title;
        this.summary = summary;
        this.link = link;
    }

    public NewsTitle(int nid, String stamp, String icon, String title, String summary, String link, int subid) {
        this.nid = nid;
        this.stamp = stamp;
        this.icon = icon;
        this.title  =title;
        this.summary = summary;
        this.link = link;
        this.subid = subid;
    }


    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getSubid() {
        return subid;
    }

    public void setSubid(int subid) {
        this.subid = subid;
    }


}
