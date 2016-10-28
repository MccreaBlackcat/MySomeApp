package com.timestudio.mynews.fragment;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.timestudio.mynews.R;
import com.timestudio.mynews.activity.MainActivity;
import com.timestudio.mynews.entity.NewsTitle;
import com.timestudio.mynews.entry.NewsTitleEntry;
import com.timestudio.mynews.newsDB.NewsDBManager;
import com.timestudio.mynews.util.ConnectUtil;
import com.timestudio.mynews.util.NewsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author create by shen on 2016-10-27
 * @description 展示新闻详情的的碎片
 */
public class NewsDetailsFragment extends Fragment implements View.OnClickListener {

    private View v;
    private WebView wv_details;
    private ProgressBar pb_details;
    private LinearLayout ll_sorry_noNet;
    //    private LinearLayout ll_loading;
    private ImageView iv_back;
    private ImageView iv_more_choice;
    private Button btn_collect;
    private Button btn_comment;
    //数据库
    private NewsDBManager dbManager;
    WebSettings settings;

    private PopupWindow mPopupWindow;

    private NewsTitle newsTitle;
    //是否收藏，默认为没有收藏
    private boolean isCollect = false;

    public NewsDetailsFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news_details, container, false);
        dbManager = new NewsDBManager(getActivity());
        initView();
        initWebData();
        return v;
    }

    /**
     * 加载碎片控件
     */
    private void initView() {
        wv_details = (WebView) v.findViewById(R.id.wv_details);
        pb_details = (ProgressBar) v.findViewById(R.id.pb_news_details);
        ll_sorry_noNet = (LinearLayout) v.findViewById(R.id.ll_sorry_noNet);
//        ll_loading = (LinearLayout) v.findViewById(R.id.ll_loading);
        iv_more_choice = (ImageView) v.findViewById(R.id.iv_more_choice);

        iv_back = (ImageView) v.findViewById(R.id.iv_back);


        View popupView = getLayoutInflater(null).inflate(R.layout.choice_popupwindows, null);
        btn_collect = (Button) popupView.findViewById(R.id.btn_collect);
        btn_comment = (Button) popupView.findViewById(R.id.btn_comment);
        setListener();
        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
    }

    /**
     * 设置监听
     */

    private void setListener() {
        iv_back.setOnClickListener(this);
        iv_more_choice.setOnClickListener(this);
        btn_collect.setOnClickListener(this);
        btn_comment.setOnClickListener(this);
    }

    /**
     * 加载数据
     */
    private void initWebData() {
        if (!ConnectUtil.isNetworkAvailable(getActivity())) {
            pb_details.setVisibility(View.GONE);
            ll_sorry_noNet.setVisibility(View.VISIBLE);
        } else {
            ll_sorry_noNet.setVisibility(View.INVISIBLE);
            wv_details.setVisibility(View.VISIBLE);
            newsTitle = (NewsTitle) getArguments().getSerializable("newsTitle");
            wv_details.loadUrl(newsTitle.getLink());
            settings = wv_details.getSettings();
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            wv_details.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            wv_details.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        pb_details.setVisibility(View.GONE);
                    } else {
                        if (View.INVISIBLE == pb_details.getVisibility()) {
                            pb_details.setVisibility(View.VISIBLE);
                        }
                        pb_details.setProgress(newProgress);
                    }
                    super.onProgressChanged(view, newProgress);
                }
            });

            //获取评论的数量，设置到UI上
            initCommentQuantity();

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack();

                break;
            case R.id.iv_more_choice:
                mPopupWindow.showAsDropDown(v);
                break;

            case R.id.btn_collect:
                updataDB();
                break;
            case R.id.btn_comment:
                //点击评论，跳转到评论的Fragment
                break;
        }
    }

    /**
     * 点击收藏更新数据库
     */
    private void updataDB() {
        if (!isCollect) {
            //收藏执行操作
            dbManager.inserNewsCollect(newsTitle);
            isCollect = true;
            btn_collect.setText("取消收藏");
        } else {
            //取消收藏操作
            dbManager.deleteNewsCollect(newsTitle.getNid());
            //回调刷新收藏里的适配器
            isCollect = false;
            btn_collect.setText("收藏");
        }

    }

    /**
     * 获取到评论数目
     */
    protected void initCommentQuantity() {
        String url = ConnectUtil.APPCONET + "cmt_num?ver=1&nid=" + newsTitle.getNid();
        NewsManager manager = new NewsManager(getActivity());
        manager.getJSONObject(url, listener, errorListener);
    }

    protected Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            String commentNum = null;
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("message").equals("OK")) {
                    commentNum = object.getString("data");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (commentNum != null) {
                btn_comment.setText("评论(" + commentNum + ")");
            }

        }
    };

    protected Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };
}
