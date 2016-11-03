package com.timestudio.mynews.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.timestudio.mynews.R;
import com.timestudio.mynews.activity.MainActivity;
import com.timestudio.mynews.adapter.NewsTitleAdapter;
import com.timestudio.mynews.entity.NewsTitle;
import com.timestudio.mynews.myView.lib3.xlistview.XListView;
import com.timestudio.mynews.newsDB.NewsDBManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * 显示收藏新闻列表的Fragment
 */
public class NewsCollectFragment extends Fragment implements XListView.IXListViewListener{

    private View view;
    private ArrayList<NewsTitle> mList = new ArrayList<>();
    private XListView xlv_collect;

    private int count = 0;
    private int offset = 20;
    private NewsDBManager dbManager;
    private NewsTitleAdapter titleAdapter;

    public NewsCollectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_collect, container, false);
        dbManager = new NewsDBManager(getActivity());
        initView();
        titleAdapter = new NewsTitleAdapter(getActivity(),xlv_collect);
        setListener();
        initData();
        return view;
    }

    /**
     * 绑定控件
     */
    private void initView() {
        xlv_collect = (XListView) view.findViewById(R.id.xlv_collect);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        xlv_collect.setPullLoadEnable(true);
        xlv_collect.setPullRefreshEnable(true);
        xlv_collect.setXListViewListener(this);

        /*
        * 点击XListView实现碎片切换
        * */
        xlv_collect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsDetailsFragment fragment = new NewsDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsTitle", mList.get(position-1));
                fragment.setArguments(bundle);
                count = 0;
                offset = 20;
                ((MainActivity)getActivity()).addFragment(fragment);
//                ((MainActivity)getActivity()).replaceFragment(fragment);
            }
        });
    }

    /**
     * 加载数据
     */
    private void initData() {
        mList = dbManager.queryCollectNews(count, offset);
        count = offset + 1 ;
        offset = offset + 20 ;
        Log.i("shen", "NewsCollectFragment---------------------: " + mList.size());
        if (mList != null) {
            titleAdapter.setMyList(mList);
            xlv_collect.setAdapter(titleAdapter);
        }

    }

    @Override
    public void onRefresh() {
        xlv_collect.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        ArrayList<NewsTitle> list = dbManager.queryCollectNews(count, offset);
        if (list != null) {
            titleAdapter.insertNewsList(list);
            mList = titleAdapter.getMyList();
            titleAdapter.update();
        }
        xlv_collect.stopLoadMore();
    }
}
