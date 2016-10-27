package com.timestudio.mynews.activity;


import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.timestudio.mynews.BaseActivity;
import com.timestudio.mynews.R;

public class MenuBarActivity extends BaseActivity implements View.OnClickListener{

    //目录控件
    LinearLayout ll_menuBar_news;
    LinearLayout ll_menuBar_read;
    LinearLayout ll_menuBar_local;
    LinearLayout ll_menuBar_reply;
    LinearLayout ll_menuBar_pics;

    @Override
    protected int setContent() {
        return R.layout.activity_menu_bar;
    }

    @Override
    protected void initView() {
        ll_menuBar_news = (LinearLayout) findViewById(R.id.ll_menuBar_news);
        ll_menuBar_read = (LinearLayout) findViewById(R.id.ll_menuBar_read);
        ll_menuBar_local = (LinearLayout) findViewById(R.id.ll_menuBar_local);
        ll_menuBar_reply = (LinearLayout) findViewById(R.id.ll_menuBar_reply);
        ll_menuBar_pics = (LinearLayout) findViewById(R.id.ll_menuBar_pics);
    }

    @Override
    protected void setListener() {
        ll_menuBar_news.setOnClickListener(this);
        ll_menuBar_read.setOnClickListener(this);
        ll_menuBar_local.setOnClickListener(this);
        ll_menuBar_reply.setOnClickListener(this);
        ll_menuBar_pics.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent;
        switch (v.getId()) {
            case R.id.ll_menuBar_news:
                mIntent = new Intent(this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.ll_menuBar_read:
                break;
            case R.id.ll_menuBar_local:
                break;
            case R.id.ll_menuBar_reply:
                break;
            case R.id.ll_menuBar_pics:
                mIntent = new Intent(this, TestActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }
}
