package com.timestudio.mynews.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timestudio.mynews.R;

/**
 * A simple {@link Fragment} subclass.
 * 显示收藏新闻列表的Fragment
 */
public class NewsCollectFragment extends Fragment {


    public NewsCollectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_collect, container, false);
    }

}
