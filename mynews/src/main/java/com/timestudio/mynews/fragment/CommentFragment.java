package com.timestudio.mynews.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.timestudio.mynews.R;
import com.timestudio.mynews.activity.MainActivity;
import com.timestudio.mynews.adapter.CommentAdapter;
import com.timestudio.mynews.entity.CommentEntity;
import com.timestudio.mynews.myView.lib3.xlistview.XListView;
import com.timestudio.mynews.util.ConnectUtil;
import com.timestudio.mynews.util.NewsManager;
import com.timestudio.mynews.util.parser.ParserNews;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment implements View.OnClickListener,XListView.IXListViewListener{

    //声明控件
    private View view;
    private ImageView iv_back_comment;
    private XListView xlv_comment;
    private EditText et_comment;
    private Button btn_send_comment;

    private CommentAdapter commentAdapter;

    private NewsManager manager;
    private ArrayList<CommentEntity> commentEntities = new ArrayList<>();
    private int nid;
    String token;
    private String ctx;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment, container, false);
        manager = new NewsManager(getActivity());
        initView();
        commentAdapter = new CommentAdapter(getActivity(),xlv_comment);
        initListener();
        initData();
        return view;
    }

    /**
     * 加载控件
     */
    private void initView() {
        iv_back_comment = (ImageView) view.findViewById(R.id.iv_back_comment);
        xlv_comment = (XListView) view.findViewById(R.id.xlv_comment);
        et_comment = (EditText) view.findViewById(R.id.et_comment);
        btn_send_comment = (Button) view.findViewById(R.id.btn_send_comment);
    }

    private void initListener() {
        iv_back_comment.setOnClickListener(this);
        xlv_comment.setPullLoadEnable(true);
        xlv_comment.setPullRefreshEnable(true);
        xlv_comment.setXListViewListener(this);
        btn_send_comment.setOnClickListener(this);

        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ctx = s.toString();
            }
        });
    }

    private void initData() {
        nid = getArguments().getInt("nid");
        RefreshComment();


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_send_comment:
                SharedPreferences preferences = getActivity().getSharedPreferences("login", 0);
                boolean isLogin = preferences.getBoolean("login", false);
                if (isLogin) {
                    token = preferences.getString("token", "");
                    publishComment();
                    et_comment.setText("");

                } else {
                    Toast.makeText(getActivity(),"请先登录！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_back_comment:
                ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack();
                break;
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        RefreshComment();
        xlv_comment.setRefreshTime(new SimpleDateFormat(getActivity().getResources().getString(R.string.app_time)).format(new Date()));
        xlv_comment.stopRefresh();
    }

    /**
     * 上滑加载更多
     */
    @Override
    public void onLoadMore() {
        LoadMoreComment();
        xlv_comment.stopLoadMore();
    }

    /**
     * 刷新最新的评论
     */
    private void RefreshComment() {
        int dir = 1;
        String url = ConnectUtil.APPCONET + "cmt_list?ver=1&nid=" + nid + "&type=1&stamp=11111111&cid=1&dir=" + dir + "&cnt=20";
        manager.getJSONObject(url, refreshListener, errorListener);
    }

    /**
     * 加载更多评论
     */
    private void LoadMoreComment() {
        int dir = 2;
        String url = ConnectUtil.APPCONET + "cmt_list?ver=1&nid=" + nid + "&type=1&stamp=11111111&cid=" +
                commentEntities.get(commentAdapter.getMyList().size() - 1).getCid()+ "&dir=" + dir + "&cnt=20";
        manager.getJSONObject(url,loadMoreListener,errorListener);
    }

    /**
     * 获取数据成功
     */
    protected Response.Listener<String> refreshListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            commentEntities.clear();
            commentEntities = ParserNews.parserCommentList(s);
            commentAdapter.setMyList(commentEntities);
            xlv_comment.setAdapter(commentAdapter);
        }
    };

    protected Response.Listener<String> loadMoreListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            ArrayList<CommentEntity> list = ParserNews.parserCommentList(s);
            commentAdapter.insertNewsList(list);
            commentAdapter.update();
        }
    };

    /**
     * 获取失败
     */
    protected Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    /**
     * 发表评论
     */
    private void publishComment() {
        String url = ConnectUtil.APPCONET + "cmt_commit";
        manager.getJSONObjectofPost(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("message").equals("OK")) {
                        String stamp = new SimpleDateFormat(getActivity().getResources().getString(R.string.app_time)).format(new Date());
                        String uid = getActivity().getSharedPreferences("login",0).getString("uid","");
                        CommentEntity commentEntity = new CommentEntity(stamp,uid, ctx,"");
                        commentEntities.add(0, commentEntity);
                        commentAdapter.update();
                        xlv_comment.setSelection(0);
                        Toast.makeText(getActivity(),"发表成功",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"发表失败",Toast.LENGTH_SHORT).show();
            }
        },
                nid,
                token,
                ctx);

//        String url = ConnectUtil.APPCONET + "cmt_commit?" + ConnectUtil.APP_VER + "&nid=" + nid + "&token="
//                + token + "&imei=111111111111111" + "&ctx=" + ctx;
//        manager.getJSONObject(url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        try {
//                            JSONObject object = new JSONObject(s);
//                            if (object.getString("message").equals("OK")) {
//                                String stamp = new SimpleDateFormat(getActivity().getResources().getString(R.string.app_time)).format(new Date());
//                                String uid = getActivity().getSharedPreferences("login",0).getString("user","");
//                                CommentEntity commentEntity = new CommentEntity(stamp,uid, ctx,"");
//                                commentEntities.add(0, commentEntity);
//                                commentAdapter.update();
//                                xlv_comment.setSelection(0);
//                                Toast.makeText(getActivity(),"发表成功",Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(getActivity(),"发表失败",Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
}
