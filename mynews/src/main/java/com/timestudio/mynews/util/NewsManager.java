package com.timestudio.mynews.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee on 2016/10/26.
 * 新闻管理类
 */

public class NewsManager {

    //一个请求队列
    private RequestQueue mQueue;

    public NewsManager(Context context) {
        this.mQueue = Volley.newRequestQueue(context);
    }

    /**
     * 获取JSONObject
     * @param url
     * @param listener
     * @param errorListener
     */
    public void getJSONObject(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {

        StringRequest stringRequest = new StringRequest(url, listener, errorListener);
        mQueue.add(stringRequest);

    }

    /**
     * 获取JSONObject
     * @param url
     * @param listener
     * @param errorListener
     */
    public void getJSONObjectofPost(String url, Response.Listener<String> listener, Response.ErrorListener errorListener,
                                    final int nid, final String token, final String ctx) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, listener, errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("ver", "1");
                map.put("nid", nid + "");
                map.put("token", token);
                map.put("imei", "111111111111111");
                map.put("ctx", ctx);
                return map;
            }
        };
        mQueue.add(stringRequest);

    }

    public void addImage(String url, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener) {
        ImageRequest imageRequest = new ImageRequest(url,listener,0,0, Bitmap.Config.RGB_565,errorListener);
        mQueue.add(imageRequest);
    }
}
