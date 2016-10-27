package com.timestudio.mynews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompatBase;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.timestudio.mynews.R;
import com.timestudio.mynews.entity.NewsTitle;
import com.timestudio.mynews.myView.lib3.xlistview.XListView;
import com.timestudio.mynews.util.NewsManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.timestudio.mynews.R.id.tv_title_title;

/**
 * Created by shenGqiang on 2016/10/18.
 * 新闻概要适配器
 */

public class NewsTitleAdapter extends MyBaseAdapter<NewsTitle> {

    private XListView xlv_view;
    private NewsManager newsManager;
    private LruCache<String,Bitmap> lruCache = new LruCache(1024 * 1024);


    public NewsTitleAdapter(Context mContext, XListView xlv) {
        super(mContext);
        this.xlv_view = xlv;

        newsManager = new NewsManager(mContext);
    }

    /**
     * @param url
     * @return
     * @description 获取图片的方法
     */

    private Bitmap getBitmap(final String url) {
        final String iconName = url.substring(url.lastIndexOf("/") + 1);
        //获取缓存中的图片
        Bitmap bit = lruCache.get(iconName);

        if (bit != null) {
            Log.i("shen", "内存");
            return bit;
        }
        //判断本地是否有图片
        bit = getCacheImage(iconName);
        if (bit != null) {
            Log.i("shen", "本地");
            return bit;
        }
        //网络连接获取图片
        asyncHttpImage(url,iconName);

        return bit;
    }

    private void asyncHttpImage(final String url, final String iconName) {

        newsManager.addImage(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                //取出ImageView
                ImageView im = (ImageView) xlv_view.findViewWithTag(url);
                if (im != null) {
                    Log.i("shen", "网络");
                    im.setImageBitmap(bitmap);
                    //运行缓存
                    lruCache.put(iconName, bitmap);
                    //获取本地缓存文件夹路径
                    File f = mContext.getCacheDir();
                    //判断是否存在
                    if (f.exists()) {
                        //不存在则创建
                        f.mkdir();
                    }
                    //创建文件
                    File mFile = new File(f , iconName);
                    //创建输出流，输出到本地文件
                    OutputStream op = null;
                    try {
                        mFile.createNewFile();
                        op = new FileOutputStream(mFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, op);
                        Log.i("shen", "已经保存到本地");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            op.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
//                    im.setBackgroundResource(R.mipmap.defaultpic);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private Bitmap getCacheImage(String iconName) {
        Bitmap bit = null;
        File[] f = mContext.getCacheDir().listFiles();
        File mfile = null;
        for (File file : f) {
            if (iconName.equals(file.getAbsolutePath())) {
                mfile = file;
                break;
            }
        }
        if (mfile == null) {
            return null;
        }
        bit = BitmapFactory.decodeFile(mfile.getAbsolutePath());
        //出入内存缓存
        lruCache.put(iconName, bit);
        return bit;
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.news_title_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_title_title = (TextView) convertView.findViewById(tv_title_title);
            viewHolder.tv_title_detailes = (TextView) convertView.findViewById(R.id.tv_title_details);
            viewHolder.tv_title_time = (TextView) convertView.findViewById(R.id.tv_title_time);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_title_picture);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_title_title.setText(myList.get(position).getTitle());
        viewHolder.tv_title_detailes.setText(myList.get(position).getSummary());
        viewHolder.tv_title_time.setText(myList.get(position).getStamp());
        Bitmap b = getBitmap(myList.get(position).getIcon());
        if (b != null) {
            viewHolder.iv_icon.setImageBitmap(b);
        }

        viewHolder.iv_icon.setTag(myList.get(position).getIcon());

        return convertView;
    }

    class ViewHolder {
        TextView tv_title_title;
        TextView tv_title_detailes;
        TextView tv_title_time;
        ImageView iv_icon;
    }

}
