package com.timestudio.mynews.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.timestudio.mynews.R;

/**
 * A simple {@link Fragment} subclass.
 * @author create by shen on 2016-10-27
 * @description 展示新闻详情的的碎片
 *
 */
public class NewsDetailsFragment extends Fragment {

    private View v;
    private WebView wv_details;
    WebSettings settings ;


    public NewsDetailsFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news_details, container, false);
        initView();
        initWebData();
        return v;
    }

    /**
     * 加载碎片控件
     */
    private void initView() {
        wv_details = (WebView) v.findViewById(R.id.wv_details);
    }

    /**
     * 加载数据
     */
    private void initWebData() {
        String link = getArguments().getString("link");
        settings = wv_details.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        wv_details.loadUrl(link);
    }

}
