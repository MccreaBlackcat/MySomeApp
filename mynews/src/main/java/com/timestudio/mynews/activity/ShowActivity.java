package com.timestudio.mynews.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.timestudio.mynews.BaseActivity;
import com.timestudio.mynews.R;

public class ShowActivity extends BaseActivity {

    ViewPager vp_picture;

    View[] views = new View[4];

    TextView tv_picture_1;
    TextView tv_picture_2;
    TextView tv_picture_3;
    TextView tv_picture_4;

    SharedPreferences preferences;
    private final String FILE_NAME = "firstOpen";
    @Override

    protected int setContent() {
        return R.layout.activity_show;
    }

    @Override
    protected void initView() {
        preferences = getSharedPreferences(FILE_NAME,0);
        boolean isFirst = preferences.getBoolean("isFirstOpen",true);
        if (isFirst) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstOpen",false);
            editor.commit();
            vp_picture = (ViewPager) findViewById(R.id.vp_picture);
            views[0] = getLayoutInflater().inflate(R.layout.show_one, null);
            views[1] = getLayoutInflater().inflate(R.layout.show_tow, null);
            views[2] = getLayoutInflater().inflate(R.layout.show_three, null);
            views[3] = getLayoutInflater().inflate(R.layout.show_fort, null);
            views[3].findViewById(R.id.tv_fort).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(ShowActivity.this, MainActivity.class);
                    startActivity(mIntent);
                }
            });
            tv_picture_1 = (TextView) findViewById(R.id.tv_picture_1);
            tv_picture_2 = (TextView) findViewById(R.id.tv_picture_2);
            tv_picture_3 = (TextView) findViewById(R.id.tv_picture_3);
            tv_picture_4 = (TextView) findViewById(R.id.tv_picture_4);

            myVpAdapter vpAdapter =  new myVpAdapter();
            vp_picture.setAdapter(vpAdapter);
            vp_picture.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (position) {
                        case 0:
                            tv_picture_1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            tv_picture_2.setBackground(null);
                            tv_picture_3.setBackground(null);
                            tv_picture_4.setBackground(null);
                            break;
                        case 1:
                            tv_picture_2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            tv_picture_1.setBackground(null);
                            tv_picture_3.setBackground(null);
                            tv_picture_4.setBackground(null);
                            break;
                        case 2:
                            tv_picture_3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            tv_picture_2.setBackground(null);
                            tv_picture_1.setBackground(null);
                            tv_picture_4.setBackground(null);
                            break;
                        case 3:
                            tv_picture_4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            tv_picture_2.setBackground(null);
                            tv_picture_3.setBackground(null);
                            tv_picture_1.setBackground(null);
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } else {
            Intent mIntent = new Intent(ShowActivity.this, MainActivity.class);
            startActivity(mIntent);
            finish();
        }

    }

    @Override
    protected void setListener() {

    }

    public class myVpAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views[position]);
            return views[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views[position]);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
