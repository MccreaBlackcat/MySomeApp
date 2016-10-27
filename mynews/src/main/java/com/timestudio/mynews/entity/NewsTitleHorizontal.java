package com.timestudio.mynews.entity;

/**
 * Created by hasee on 2016/10/25.
 * @description 每个新闻类型的实体类
 */

public class NewsTitleHorizontal {
    private String subgroup;
    private int subid;

    public NewsTitleHorizontal(String subgroup, int subid) {
        this.subgroup = subgroup;
        this.subid = subid;
    }



    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public int getSubid() {
        return subid;
    }

    public void setSubid(int subid) {
        this.subid = subid;
    }
}
