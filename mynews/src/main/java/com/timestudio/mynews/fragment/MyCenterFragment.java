package com.timestudio.mynews.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.timestudio.mynews.R;
import com.timestudio.mynews.adapter.MyCenterAdapter;
import com.timestudio.mynews.util.ConnectUtil;
import com.timestudio.mynews.util.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCenterFragment extends Fragment implements View.OnClickListener {


    private View view;
    private ImageView iv_myCenter_back;
    private ImageView iv_myCenter_portrait;
    private TextView tv_myCenter_user;
    private TextView tv_myCenter_time;
    private TextView tv_myCenter_address;
    private TextView tv_myCenter_comnum;
    private TextView tv_myCenter_integration;
    private TextView tv_myCenter_login;
    private ListView lv_myCenter_log;
    private TextView tv_myCenter_exit;


    private String uid;
    private String portrait;
    private String integration;
    private String comnum;
    private String time;
    private String address;

    ArrayList<String[]> mList = new ArrayList<>();
    boolean isShowLog = true;
    private PopupWindow mPopupWindow;
    private TextView tv_photo;
    private TextView tv_take_photo;
    private TextView tv_cancel;

    public MyCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_center, container, false);
        initView();
        setListener();
        initData();
        return view;
    }

    private void initView() {
        iv_myCenter_back = (ImageView) view.findViewById(R.id.iv_myCenter_back);
        iv_myCenter_portrait = (ImageView) view.findViewById(R.id.iv_myCenter_portrait);
        tv_myCenter_user = (TextView) view.findViewById(R.id.tv_myCenter_user);
        tv_myCenter_time = (TextView) view.findViewById(R.id.tv_myCenter_time);
        tv_myCenter_address = (TextView) view.findViewById(R.id.tv_myCenter_address);
        tv_myCenter_comnum = (TextView) view.findViewById(R.id.tv_myCenter_comnum);
        tv_myCenter_integration = (TextView) view.findViewById(R.id.tv_myCenter_integration);
        tv_myCenter_login = (TextView) view.findViewById(R.id.tv_myCenter_login);
        lv_myCenter_log = (ListView) view.findViewById(R.id.lv_myCenter_log);
        tv_myCenter_exit = (TextView) view.findViewById(R.id.tv_myCenter_exit);

        View popupView = getLayoutInflater(null).inflate(R.layout.choice_pic_pppwindow, null);
        //获取到ppp的控件
        tv_photo = (TextView) popupView.findViewById(R.id.tv_picppp_photo);
        tv_take_photo = (TextView) popupView.findViewById(R.id.tv_picppp_take);
        tv_cancel = (TextView) popupView.findViewById(R.id.tv_picppp_cancel);
        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
    }

    private void setListener() {
        iv_myCenter_back.setOnClickListener(this);
        iv_myCenter_portrait.setOnClickListener(this);
        iv_myCenter_back.setOnClickListener(this);
        tv_myCenter_login.setOnClickListener(this);
        tv_myCenter_exit.setOnClickListener(this);
        //pppwindow控件设置监听
        tv_photo.setOnClickListener(this);
        tv_take_photo.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    private void initData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        SharedPreferences preferences = getActivity().getSharedPreferences("login", 0);
        String token = preferences.getString("token", "");
        String url = ConnectUtil.APPCONET + "user_home?" + ConnectUtil.APP_VER + "&imei=111111111111111" +
                "&token=" + token;
        Log.i("shen", url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("message").equals("OK")) {
                        JSONObject ob = object.getJSONObject("data");
                        uid = ob.getString("uid");
                        portrait = ob.getString("portrait");
                        integration = ob.getString("integration");
                        comnum = ob.getString("comnum");
                        JSONArray array = ob.getJSONArray("loginlog");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject ob2 = array.getJSONObject(1);
                            time = ob2.getString("time");
                            address = ob2.getString("address");
                            mList.add(new String[]{time,address});
                        }
                    }
                    UserManager userManager = new UserManager(getActivity());

                    userManager.getBitmap(portrait, getActivity(), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            iv_myCenter_portrait.setImageBitmap(bitmap);
                            String icon = portrait;
                            icon = icon.substring(icon.lastIndexOf("/") + 1);
                            File file = new File(getActivity().getCacheDir(), "image");
                            if(!file.exists()){
                                file.mkdirs();
                            }
                            try {
                                OutputStream out = new FileOutputStream(new File(file,icon));
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tv_myCenter_user.setText(uid);
                tv_myCenter_time.setText(mList.get(0)[0]);
                tv_myCenter_address.setText(mList.get(0)[1]);
                tv_myCenter_comnum.setText(comnum);
                tv_myCenter_integration.setText(integration);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_myCenter_back:
                break;
            case R.id.iv_myCenter_portrait:
                //设置头像
                mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
                mPopupWindow.showAtLocation(getActivity().findViewById(R.id.ll_mtCenter), Gravity.BOTTOM,0,0);
                break;
            case R.id.tv_myCenter_login:
                if (isShowLog) {
                    lv_myCenter_log.setVisibility(View.VISIBLE);
                    MyCenterAdapter adapter = new MyCenterAdapter(getActivity());
                    adapter.setMyList(mList);
                    lv_myCenter_log.setAdapter(adapter);
                    isShowLog = false;
                } else {
                    lv_myCenter_log.setVisibility(View.INVISIBLE);
                    isShowLog = true;
                }
                break;
            case R.id.tv_myCenter_exit:
                break;
            case R.id.tv_picppp_photo:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,400);
                break;
            case R.id.tv_picppp_take:
                Intent mIntent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(mIntent1,100);
                break;
            case R.id.tv_picppp_cancel:
                mPopupWindow.dismiss();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 400 || requestCode ==500) {
            if (data != null) {
                Crop(data.getData());
            }
        } else if (requestCode == 100) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            iv_myCenter_portrait.setImageBitmap(bitmap);
        }
    }

    private void Crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 设置类型
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪命令
        intent.putExtra("crop", true);
        // X方向上的比例
        intent.putExtra("aspectX",1);
        // Y方向上的比例
        intent.putExtra("aspectY",1);
        // 裁剪区的宽
        intent.putExtra("outputX",100);
        // 裁剪区的高
        intent.putExtra("outputY",100);
        // 返回数据
        intent.putExtra("return-data",true);
        startActivityForResult(intent,100);
    }
}
