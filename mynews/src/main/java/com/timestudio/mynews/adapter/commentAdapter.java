package com.timestudio.mynews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.timestudio.mynews.R;
import com.timestudio.mynews.entity.CommentEntity;
import com.timestudio.mynews.myView.lib3.xlistview.XListView;
import com.timestudio.mynews.util.NewsManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by hasee on 2016/10/31.
 */

public class CommentAdapter extends MyBaseAdapter<CommentEntity> {

    private XListView xlv_comment;
    private LruCache<String,Bitmap> lruCache = new LruCache(1024 * 1024);
    private NewsManager newsManager;

    public CommentAdapter(Context mContext,XListView xListView) {
        super(mContext);
        this.xlv_comment = xListView;

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
            return bit;
        }
        //判断本地是否有图片
        bit = getCacheImage(iconName);
        if (bit != null) {
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
                ImageView im = (ImageView) xlv_comment.findViewWithTag(url);
                if (im != null) {
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.comment_item, null);
            holder = new ViewHolder();
            holder.iv_user_icon = (ImageView) convertView.findViewById(R.id.iv_user_icon);
            holder.tv_comment_item_user = (TextView) convertView.findViewById(R.id.tv_comment_item_user);
            holder.tv_comment_item_content = (TextView) convertView.findViewById(R.id.tv_comment_item_content);
            holder.tv_comment_item_date = (TextView) convertView.findViewById(R.id.tv_comment_item_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_comment_item_user.setText(myList.get(position).getUid());
        holder.tv_comment_item_content.setText(myList.get(position).getContent());
        holder.tv_comment_item_date.setText(myList.get(position).getStamp());
        Bitmap b = getBitmap(myList.get(position).getPortrait());
        if (b != null) {
            holder.iv_user_icon.setImageBitmap(b);
        }

        holder.iv_user_icon.setTag(myList.get(position).getPortrait());

        return convertView;
    }

    class ViewHolder {
        ImageView iv_user_icon;
        TextView tv_comment_item_user;
        TextView tv_comment_item_content;
        TextView tv_comment_item_date;

    }

}
