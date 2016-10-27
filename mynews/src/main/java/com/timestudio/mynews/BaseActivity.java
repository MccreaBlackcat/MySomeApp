package com.timestudio.mynews;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(setContent());
        initView();
        setListener();

    }

    /**
     * @description 设置布局文件
     * @return  布局文件ID
     */
    protected abstract int setContent();

    /**
     * @description 加载控件
     */
    protected abstract void initView();

    /**
     * @description 设置监听
     */
    protected abstract void setListener();
}
