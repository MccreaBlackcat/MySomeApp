package com.timestudio.mynews.activity;



import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.timestudio.mynews.BaseActivity;
import com.timestudio.mynews.R;
import com.timestudio.mynews.fragment.ContentFragment;
import com.timestudio.mynews.fragment.LoginFragment;
import com.timestudio.mynews.fragment.MyCenterFragment;
import com.timestudio.mynews.myView.lib3.slidingmenu.SlidingMenu;
import com.timestudio.mynews.util.ConnectUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    //碎片管理器
    FragmentManager mFm;
    //碎片事务
    FragmentTransaction mFt;

    //定义一个主Fragment
    ContentFragment MainFragment;
    //定义一个Fragment
    Fragment fragment;

    //ContentFragment的控件声明
    FrameLayout fl_main;


    //声明一个第三方类SlidingMenu，界面的左右滑动效果
    SlidingMenu slidingMenu;

    //目录控件
    LinearLayout ll_menuBar_news;
    LinearLayout ll_menuBar_read;
    LinearLayout ll_menuBar_local;
    LinearLayout ll_menuBar_reply;
    LinearLayout ll_menuBar_pics;

    //登录控件
    ImageView iv_login_pic;
    TextView tv_login_test;
    LinearLayout ll_share;

    //登录状态
    SharedPreferences preferences;

    @Override
    protected int setContent() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        preferences = getSharedPreferences("login", 0);


        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        if (!ConnectUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, getString(R.string.netWork_not), Toast.LENGTH_SHORT).show();
        }
        addFragment(MainFragment = new ContentFragment());

        slidingMenu = new SlidingMenu(this);
        //设置滑动方向
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置是否可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置划出单位
        slidingMenu.setBehindOffsetRes(R.dimen.BehindOffset);
        //设置在当前的Activity上滑动
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //左面布局
        slidingMenu.setMenu(R.layout.activity_menu_bar);
        //右面布局
        slidingMenu.setSecondaryMenu(R.layout.activity_loginmenu);
        initMenuBar();
    }

    @Override
    protected void setListener() {

        ll_menuBar_news.setOnClickListener(this);
        ll_menuBar_read.setOnClickListener(this);
        ll_menuBar_local.setOnClickListener(this);
        ll_menuBar_reply.setOnClickListener(this);
        ll_menuBar_pics.setOnClickListener(this);

        iv_login_pic.setOnClickListener(this);
        tv_login_test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //目录里面的控件
            case R.id.ll_menuBar_news:
                //点击的时候，给选中的目标设置背景，拥有点击效果
                MainFragment.setTitleBarTest(getString(R.string.main_title_information));
                replaceFragment(MainFragment);
                ll_menuBar_news.setBackgroundResource(R.drawable.menubar_click);
                ll_menuBar_read.setBackground(null);
                ll_menuBar_local.setBackground(null);
                ll_menuBar_reply.setBackground(null);
                ll_menuBar_pics.setBackground(null);

                slidingMenu.showContent();
                break;
            case R.id.ll_menuBar_read:
                MainFragment.setTitleBarTest(getString(R.string.main_title_myRead));
                ll_menuBar_read.setBackgroundResource(R.drawable.menubar_click);
                ll_menuBar_news.setBackground(null);
                ll_menuBar_local.setBackground(null);
                ll_menuBar_reply.setBackground(null);
                ll_menuBar_pics.setBackground(null);

                slidingMenu.showContent();
                break;
            case R.id.ll_menuBar_local:
                MainFragment.setTitleBarTest(getString(R.string.main_title_local));
                ll_menuBar_local.setBackgroundResource(R.drawable.menubar_click);
                ll_menuBar_read.setBackground(null);
                ll_menuBar_news.setBackground(null);
                ll_menuBar_reply.setBackground(null);
                ll_menuBar_pics.setBackground(null);
                slidingMenu.showContent();
                break;
            case R.id.ll_menuBar_reply:
                MainFragment.setTitleBarTest(getString(R.string.main_title_replay));
                ll_menuBar_reply.setBackgroundResource(R.drawable.menubar_click);
                ll_menuBar_read.setBackground(null);
                ll_menuBar_local.setBackground(null);
                ll_menuBar_news.setBackground(null);
                ll_menuBar_pics.setBackground(null);
                slidingMenu.showContent();
                break;
            case R.id.ll_menuBar_pics:
                MainFragment.setTitleBarTest(getString(R.string.main_title_pic));
                slidingMenu.showContent();
                ll_menuBar_pics.setBackgroundResource(R.drawable.menubar_click);
                ll_menuBar_read.setBackground(null);
                ll_menuBar_local.setBackground(null);
                ll_menuBar_reply.setBackground(null);
                ll_menuBar_news.setBackground(null);
                break;
            //右边目录的控件
            case R.id.iv_login_pic:
                //点击头像图片和文字都可以登录，所以就不单独写了，两个控件的控件的功能就用同一个
            case R.id.tv_login_test:
                //创建一个SharedPreference 用来获取到登陆状态，true为登录，false为未登录
                boolean isLogin = preferences.getBoolean("login",false);
                if (isLogin) {
                    //登录状态，点击则进入个人中心
                    Toast.makeText(this,getString(R.string.main_personal_center),Toast.LENGTH_SHORT).show();
                    MyCenterFragment centerFragment = new MyCenterFragment();
                    replaceFragmentToStack(centerFragment);
                    slidingMenu.showContent();
                } else {
                    //未登录状态则替换为登录碎片
                    //在登录界面，设置title为“登录”
                    if (!ConnectUtil.isNetworkAvailable(this)) {
                        Toast.makeText(this,getString(R.string.netWork_not),Toast.LENGTH_SHORT).show();
                    }
                    MainFragment.setTitleBarTest(getString(R.string.fragment_login_login));
                    //替换掉原来的fragment
                    replaceFragment(new LoginFragment());
                    slidingMenu.showContent();
                }

                break;
        }
    }

    private void initMenuBar() {
        //加载左边目录的控件
        ll_menuBar_news = (LinearLayout) slidingMenu.findViewById(R.id.ll_menuBar_news);
        ll_menuBar_news.setBackgroundColor(getResources().getColor(R.color.colorMenuBarSelector));
        ll_menuBar_read = (LinearLayout) slidingMenu.findViewById(R.id.ll_menuBar_read);
        ll_menuBar_local = (LinearLayout) slidingMenu.findViewById(R.id.ll_menuBar_local);
        ll_menuBar_reply = (LinearLayout) slidingMenu.findViewById(R.id.ll_menuBar_reply);
        ll_menuBar_pics = (LinearLayout) slidingMenu.findViewById(R.id.ll_menuBar_pics);

        //加载右边目录的控件
        iv_login_pic = (ImageView) slidingMenu.findViewById(R.id.iv_login_pic);
        tv_login_test = (TextView) slidingMenu.findViewById(R.id.tv_login_test);
        ll_share = (LinearLayout) slidingMenu.findViewById(R.id.ll_share);


        //判断是否已经登录
        if (preferences.getBoolean("login", false)) {
            //登录状态为true则 直接设置用户信息
            tv_login_test.setText(preferences.getString("user", null));
            //设置第三方登录不可用
            ll_share.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @description 设置主界面Title的文字内容
     */
    private void setTitleBarTest(String s) {
        MainFragment.setTitleBarTest(s);
    }

    /**
     * @description 添加新的碎片
     */
    public void addFragment(Fragment fragment) {
        this.fragment = fragment;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mFt.add(R.id.fl_main,fragment)
//                .addToBackStack(null)
                .commit();
    }

    /**
     * @description 替换碎片
     */
    public void replaceFragment(Fragment fragment) {
        this.fragment = fragment;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mFt.replace(R.id.fl_main,fragment)

                .commit();
    }
    /**
     * @description 替换碎片
     */
    public void replaceFragmentToStack(Fragment fragment) {
        this.fragment = fragment;
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mFt.replace(R.id.fl_main,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当点击返回键
        if (keyCode == event.KEYCODE_BACK) {
            //判断当前显示的碎片是不是ContentFragment类型
            if (!(fragment instanceof ContentFragment)) {
                replaceFragment(MainFragment);
                slidingMenu.showContent();
//                tv_titleBar.setText(getString(R.string.main_title_information));
                return true;
            }
            //判断显示的是slidingMenu的那个界面
            if (slidingMenu != null && (slidingMenu.isMenuShowing() || slidingMenu.isSecondaryMenuShowing())) {
                slidingMenu.showContent();
//                tv_titleBar.setText(getString(R.string.main_title_information));
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @description 给其他类调用的方法，用MainFragment替换当前的碎片
     * @param user
     */
    public void addMainFragment(String user) {
        replaceFragment(MainFragment);
        tv_login_test.setText(user);
        ll_share.setVisibility(View.INVISIBLE);
    }

    public void setslidingMenuShowMenu() {
        if (slidingMenu != null && slidingMenu.isMenuShowing()) {
            //隐藏
            slidingMenu.showContent();
        }else {
            //展示
            slidingMenu.showMenu();
        }
    }
    public void setslidingMenuShowSecondaryMenu() {
        if (slidingMenu != null && slidingMenu.isSecondaryMenuShowing()) {
            //隐藏
            slidingMenu.showContent();
        }else {
            //展示
            slidingMenu.showSecondaryMenu();
        }
    }
}
