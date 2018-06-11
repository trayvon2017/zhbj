package com.example.fbfatboy.zhbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fbfatboy.zhbj.MainActivity;
import com.example.fbfatboy.zhbj.R;
import com.example.fbfatboy.zhbj.base.impl.NewsPager;
import com.example.fbfatboy.zhbj.domain.NewsMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by dengdeng on 2018/6/7.
 */

public class LeftMenuFragment extends BaseFragment {
    private ArrayList<NewsMenu.NewsMenuData> mDatas;
    private int checkedItem ;
    @ViewInject(R.id.lv_leftmenu_tabs)
    private ListView leftmenuTabs;
    private MyAdapter myAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.left_menu_fragment, null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {

    }

    public void dealData(NewsMenu newsMenu) {
        checkedItem = 0;
        mDatas = newsMenu.data;
        myAdapter = new MyAdapter();
        leftmenuTabs.setAdapter(myAdapter);
        leftmenuTabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkedItem = position;
                myAdapter.notifyDataSetChanged();
                //切换mainFragment的内容
                //首先拿到mainFragment，然后修改视图
                MainActivity mainUI = (MainActivity) LeftMenuFragment.this.mActivity;
                MainContentFragment mainContentFragment = mainUI.getMainContentFragment();
                NewsPager pager = mainContentFragment.getNewsPager();
                pager.setData(position);
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.item_leftmenu_layout, null);
            TextView tv_leftmunu_tabs = (TextView) view.findViewById(R.id.tv_leftmunu_tabs);
            tv_leftmunu_tabs.setText(getItem(position).title);
            if (position == checkedItem){
                tv_leftmunu_tabs.setEnabled(true);
            }else {
                tv_leftmunu_tabs.setEnabled(false);
            }
            return view;
        }
    }
}
