package com.timestudio.mynews.entity;

/**
 * Created by hasee on 2016/10/31.
 * 评论实体类
 */

public class CommentEntity {
    private String uid;
    private String content;
    private String stamp;
    private int cid;
    private String portrait;

    public CommentEntity(String uid, String content, String stamp, int cid, String portrait) {
            this.uid = uid;
            this.content = content;
            this.stamp = stamp;
            this.cid = cid;
            this.portrait = portrait;
    }

    public CommentEntity(String stamp, String uid, String content,String portrait) {
        this.stamp = stamp;
        this.uid = uid;
        this.content = content;
        this.portrait = portrait;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
