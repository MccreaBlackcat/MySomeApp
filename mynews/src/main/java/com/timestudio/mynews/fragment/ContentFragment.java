package com.timestudio.mynews.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.timestudio.mynews.R;
import com.timestudio.mynews.activity.MainActivity;
import com.timestudio.mynews.adapter.HorizontalTypeAdapter;
import com.timestudio.mynews.adapter.NewsTitleAdapter;
import com.timestudio.mynews.entity.NewsTitle;
import com.timestudio.mynews.entity.NewsTitleHorizontal;
import com.timestudio.mynews.myView.HorizontalListView;
import com.timestudio.mynews.myView.lib3.xlistview.XListView;
import com.timestudio.mynews.newsDB.NewsDBManager;
import com.timestudio.mynews.util.ConnectUtil;
import com.timestudio.mynews.util.NewsManager;
import com.timestudio.mynews.util.parser.ParserNews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment implements View.OnClickListener{

    ImageView iv_menu;
    ImageView iv_login;
    TextView tv_titleBar;
    ImageView iv_moreType;
    LinearLayout ll_loading;

    HorizontalListView hlv_title_type;
    XListView xlv_details;
    ArrayList<NewsTitle> mDataXList = new ArrayList<NewsTitle>();
    private ArrayList<NewsTitleHorizontal> mDataList = new ArrayList<>();
    //适配器
    HorizontalTypeAdapter adapter;
    NewsTitleAdapter newsTitleAdapter;

    //类型实体类对象
    NewsTitleHorizontal newsTitleHorizontal;
    //新闻类ID号
    int subidNum;
    //新闻管理类实例
    private NewsManager manager;
    //数据库管理类实例
    private NewsDBManager dbManager;

    //加载数据库下标
    private int count = 0;
    private int offset = 20;

    public ContentFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        manager = new NewsManager(getActivity());
        dbManager = new NewsDBManager(getActivity());
        adapter = new HorizontalTypeAdapter(getActivity());
        iv_menu = (ImageView) view.findViewById(R.id.iv_Menu);
        iv_login = (ImageView) view.findViewById(R.id.iv_login);
        tv_titleBar = (TextView) view.findViewById(R.id.tv_titleBar);
        ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
        iv_moreType = (ImageView) view.findViewById(R.id.iv_moreType);
        iv_menu.setOnClickListener(this);
        iv_login.setOnClickListener(this);

        hlv_title_type = (HorizontalListView) view.findViewById(R.id.hlv_title_type);
        xlv_details = (XListView) view.findViewById(R.id.xlv_details);
        newsTitleAdapter = new NewsTitleAdapter(getActivity(), xlv_details);
        xlv_details.setPullLoadEnable(true);
        xlv_details.setPullRefreshEnable(true);
        xlv_details.setRefreshTime(new SimpleDateFormat(getActivity().getResources().getString(R.string.app_time)).format(new Date()));
        xlv_details.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                loadNewsRefresh();
                xlv_details.setRefreshTime(new SimpleDateFormat(getActivity().getResources().getString(R.string.app_time)).format(new Date()));
                xlv_details.stopRefresh();

            }

            @Override
            public void onLoadMore() {
                loadMoreData();
                xlv_details.stopLoadMore();
            }
        });
        //加载横向类型
        initTitleData();

        return view;
    }

    /**
     * @descritpion 加载横向 Title 的函数
     */
    private void initTitleData() {
        ll_loading.setVisibility(View.VISIBLE);
        mDataList = dbManager.queryNewsType();
        if (!ConnectUtil.isNetworkAvailable(getActivity())) {
            if (mDataList.size() > 0) {
                adapter.setMyList(mDataList);
                initNews(subidNum = mDataList.get(0).getSubid());
                hlv_title_type.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(),getString(R.string.netWork_not) + "无数据!",Toast.LENGTH_SHORT).show();
            }
        } else {
            if (mDataList.size() > 0) {
                adapter.setMyList(mDataList);
                initNews(subidNum = mDataList.get(0).getSubid());
                hlv_title_type.setAdapter(adapter);
            } else {
                String url = ConnectUtil.APPCONET + "news_sort?ver=1&imei=111111111111111";
                manager.getJSONObject(url, listenerOfNewsType, errorListener);
            }
        }
        //加载新闻数据
        setNewsAdapterData();
        initData();
        ll_loading.setVisibility(View.GONE);

    }

    /**
     * @descritpion 设置横向 Title 适配器数据
     */
    private void initData() {

        adapter.setMyList(mDataList);
        hlv_title_type.setAdapter(adapter);
        setListListener();

    }

    /**
     * @param subid 新闻类型
     * @description 加载新闻概要
     */
    private void initNews(int subid) {
        if (!ConnectUtil.isNetworkAvailable(getActivity())) {
            Log.i("shen", "准备查询数据库……"+subid +"----"+ count + "----" + offset);
            mDataXList = dbManager.queryNews(subid, count, offset);
            count += offset + 1;
            offset += count + 19;
            newsTitleAdapter.setMyList(mDataXList);
        } else {
            String url = ConnectUtil.APPCONET +
                    "news_list?ver=1&subid=" + subid + "&dir=1&nid=1&stamp=20150101&cnt=20";
            manager.getJSONObject(url,listenerOfNewNewsList,errorListener);
        }
        xlv_details.setAdapter(newsTitleAdapter);
    }

    /**
     * 设置新闻内容适配器
     */
    private void setNewsAdapterData() {
        newsTitleAdapter.setMyList(mDataXList);
        xlv_details.setAdapter(newsTitleAdapter);
    }

    /**
     * 点击横排的title加载不同类型的新闻
     */
    private void setListListener() {
        hlv_title_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setPositionIndex(position);
                adapter.notifyDataSetChanged();
                mDataXList.clear();
                count = 0;
                offset = 20;
                int subid = mDataList.get(position).getSubid();
                subidNum = subid;
                initNews(subid);
            }
        });

        /*
        * 点击XListView实现碎片切换
        * */
        xlv_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsDetailsFragment fragment = new NewsDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsTitle", mDataXList.get(position-1));
                fragment.setArguments(bundle);
                count = 0;
                offset = 20;
                ((MainActivity)getActivity()).addFragment(fragment);
//                ((MainActivity)getActivity()).replaceFragment(fragment);
            }
        });

        iv_moreType.setOnClickListener(this);
    }


    /**
     * 加载最新新闻数据
     */

    private void loadNewsRefresh() {

        if (!ConnectUtil.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.netWork_not), Toast.LENGTH_SHORT).show();
        } else {
            String url = ConnectUtil.APPCONET + "/news_list?ver=" + ConnectUtil.APP_VER + "&subid=" +
                    subidNum + "&dir=1&nid=1&stamp=11111111&cnt=20";
            manager.getJSONObject(url,listenerOfNewNewsList,errorListener);
        }
    }

    /**
     * 上拉加载更多数据
     */
    private void loadMoreData() {

        if (ConnectUtil.isNetworkAvailable(getActivity())) {
            mDataXList = newsTitleAdapter.getMyList();
            String url = ConnectUtil.APPCONET + "news_list?ver=" + ConnectUtil.APP_VER +
                    "&subid=" + subidNum + "&dir=2&nid=" +
                    mDataXList.get(mDataXList.size() - 1).getNid()
                    + "&stamp=11111111&cnt=20";
            manager.getJSONObject(url,listenerOfLoadMoreNews,errorListener);
        }
    }

    /**
     * 请求返回结果正确
     */

    //请求新闻类型
    protected Response.Listener<String> listenerOfNewsType = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            ArrayList<NewsTitleHorizontal> list = dbManager.queryNewsType();
            //解析字符串
            mDataList = ParserNews.parserNewsType(s);
            adapter.setMyList(mDataList);
            initNews(subidNum = mDataList.get(0).getSubid());
            hlv_title_type.setAdapter(adapter);
            //将类型写入数据库
            if (mDataList.size() > list.size()) {
                dbManager.deleteNewsType();
                for (int i = 0; i < mDataList.size(); i++) {
                    dbManager.insertNewsType(mDataList.get(i));
                }
            }

        }
    };

    //请求最新新闻列表
    protected Response.Listener<String> listenerOfNewNewsList = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            mDataXList.clear();
            ArrayList<NewsTitle> list = dbManager.queryNewsAll(subidNum);
            mDataXList = ParserNews.parserNewsList(s);
            if (mDataXList.size() > list.size()) {
                //删除数据库表数据
                dbManager.deleteNewsTitle(subidNum);
                //插入数据库
                for (int i = 0; i < mDataXList.size(); i++) {
                    mDataXList.get(i).setSubid(subidNum);
                    dbManager.inserNews(mDataXList.get(i));
                }
            }
            newsTitleAdapter.setMyList(mDataXList);
            newsTitleAdapter.update();

        }
    };

    //上滑加载更多新闻
    protected Response.Listener<String> listenerOfLoadMoreNews = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            mDataXList = ParserNews.parserNewsList(s);
            newsTitleAdapter.insertNewsList(mDataXList);
            mDataXList = newsTitleAdapter.getMyList();
            newsTitleAdapter.update();
        }
    };

    /**
     * 请求返回结果错误
     */
    protected Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.i("shen", "请求结果错误！");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_Menu:
                ((MainActivity)getActivity()).setslidingMenuShowMenu();
                break;
            //点击分享
            case R.id.iv_login:
                ((MainActivity)getActivity()).setslidingMenuShowSecondaryMenu();
                break;
            case R.id.iv_moreType:
                break;
        }
    }

    /**
     * @description 设置主界面Title的文字内容
     */
    public void setTitleBarTest(String s) {
        tv_titleBar.setText(s);
    }
}
