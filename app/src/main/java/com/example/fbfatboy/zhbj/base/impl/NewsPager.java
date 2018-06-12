package com.example.fbfatboy.zhbj.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbfatboy.zhbj.MainActivity;
import com.example.fbfatboy.zhbj.R;
import com.example.fbfatboy.zhbj.base.BaseMenuDetailPager;
import com.example.fbfatboy.zhbj.base.BasePager;
import com.example.fbfatboy.zhbj.base.impl.menu.InteractMunuDetailPager;
import com.example.fbfatboy.zhbj.base.impl.menu.NewsMunuDetailPager;
import com.example.fbfatboy.zhbj.base.impl.menu.PhotosMunuDetailPager;
import com.example.fbfatboy.zhbj.base.impl.menu.TopicMunuDetailPager;
import com.example.fbfatboy.zhbj.domain.NewsMenu;
import com.example.fbfatboy.zhbj.fragment.LeftMenuFragment;
import com.example.fbfatboy.zhbj.global.GlobalConstanst;
import com.example.fbfatboy.zhbj.utils.SpUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by dengdeng on 2018/6/7.
 */

public class NewsPager extends BasePager {

    private ArrayList<BaseMenuDetailPager> mDetailPagers;
    private NewsMenu mNewsMenu;
    public NewsPager(Activity activity) {
        super(activity);
    }
    @Override
    public void initData() {
        mTv_basepager.setText("新闻");
        mIb_basepager.setVisibility(View.VISIBLE);

        TextView textView = new TextView(mActivity);
        textView.setText("我是新闻页");
        textView.setTextColor(mActivity.getResources().getColor(R.color.btn));
        mFl_basepager.addView(textView);
        String result = SpUtils.getString(mActivity, GlobalConstanst.ZHBJ_SERVER +
                GlobalConstanst.CATEGORY_JSON);
        if (!TextUtils.isEmpty(result)){
            sendDataToLeftMenu(result);
        }else {
            requestDataFromServer();
        }



    }

    private void requestDataFromServer() {
        HttpUtils xUtils = new HttpUtils();
        xUtils.send(HttpRequest.HttpMethod.GET, GlobalConstanst.ZHBJ_SERVER +
                GlobalConstanst.CATEGORY_JSON, new RequestCallBack<Object>() {

            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                String result = (String)responseInfo.result;
                Log.d(TAG, "onSuccess: "+result);
                sendDataToLeftMenu(result);
                SpUtils.putString(mActivity,GlobalConstanst.ZHBJ_SERVER +
                        GlobalConstanst.CATEGORY_JSON,result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendDataToLeftMenu(String result) {
        Gson gson = new Gson();
        mNewsMenu = gson.fromJson(result, NewsMenu.class);

        MainActivity mainUI = (MainActivity)mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        leftMenuFragment.dealData(mNewsMenu);

        mDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mDetailPagers.add(new NewsMunuDetailPager(mActivity,mNewsMenu.data.get(0).children));
        mDetailPagers.add(new TopicMunuDetailPager(mActivity));
        mDetailPagers.add(new PhotosMunuDetailPager(mActivity));
        mDetailPagers.add(new InteractMunuDetailPager(mActivity));
        //默认设置新闻页面
        setData(0);
    }


    public void setData(int position) {
        mFl_basepager.removeAllViews();
        mDetailPagers.get(position).initData();
        mFl_basepager.addView(mDetailPagers.get(position).mRootVew);
        mTv_basepager.setText(mNewsMenu.data.get(position).title);
    }
}
