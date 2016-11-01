package com.timestudio.mynews.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;

/**
 * Created by hasee on 2016/11/1.
 *
 */

public class UserManager {

    RequestQueue mQueue;

    public UserManager(Context context) {
        this.mQueue = Volley.newRequestQueue(context);
    }

    public static void UserLogin(Context context, String user, String password) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ConnectUtil.APPCONET + "user_login?" + ConnectUtil.APP_VER + "&uid=" + user + "&pwd="+ password +"&device=0";
        Log.i("shen", "UserManager--------------------" + url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("shen", "UserManager--------------------" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    /**
     * 获取用户头像
     */
    private void getBitmap(String url, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener) {
        ImageRequest imageRequest = new ImageRequest(url, listener, 0, 0, Bitmap.Config.RGB_565, errorListener);
        mQueue.add(imageRequest);
    }

    /**
     * 获取本地头像
     */
    private Bitmap getBitmap(String url,Context context){
        String icon = url.substring(url.lastIndexOf("/") + 1);
        File file = new File(context.getCacheDir() + File.separator + "image");
        File f[] = file.listFiles();
        if (f != null) {
            for (File ff :f){
                if(ff.getName().equals(icon)){
                    return BitmapFactory.decodeFile(ff.getAbsolutePath());
                }
            }
        }
        return  null;
    }
    /**
     * 获取图片
     */
    public Bitmap getBitmap(String url, Context context, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener){
        Bitmap b = getBitmap(url, context);
        if(b != null){
            Log.i("GUO","本地");
            return b;
        }
        getBitmap(url, listener, errorListener);
        return null;
    }
}
