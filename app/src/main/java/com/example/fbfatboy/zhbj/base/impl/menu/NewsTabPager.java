package com.example.fbfatboy.zhbj.base.impl.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbfatboy.zhbj.NewsPagerActiviry;
import com.example.fbfatboy.zhbj.R;
import com.example.fbfatboy.zhbj.base.BaseMenuDetailPager;
import com.example.fbfatboy.zhbj.domain.MoreNewsBean;
import com.example.fbfatboy.zhbj.domain.NewsMenu;
import com.example.fbfatboy.zhbj.domain.NewsTabBean;
import com.example.fbfatboy.zhbj.global.GlobalConstanst;
import com.example.fbfatboy.zhbj.utils.CacheUtils;
import com.example.fbfatboy.zhbj.utils.IsReadUtils;
import com.example.fbfatboy.zhbj.utils.SpUtils;
import com.example.fbfatboy.zhbj.view.PullToRefreshListView;
import com.example.fbfatboy.zhbj.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.EventBase;
import com.viewpagerindicator.CirclePageIndicator;


import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by fbfatboy on 2018/6/12.
 */

public class NewsTabPager extends BaseMenuDetailPager {
    private NewsMenu.NewsTabData mNewsTabData;
    private String mUrl;

    @ViewInject(R.id.vp_topNews)
    private TopNewsViewPager vp_topNews;

    @ViewInject(R.id.lv_news)
    private PullToRefreshListView lv_news;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator indicator;

    @ViewInject(R.id.tv_topnews_title)
    private TextView tv_topnews_title;


    private NewsTabBean.NewsData mData;
    private ArrayList<NewsTabBean.TopNewsBean> mTopNews;
    private ArrayList<NewsTabBean.NewsBean> mNews;
    private String more;
    private MoreNewsBean moreNewsBean;
    private MoreNewsBean.MoreNewsData moreNewsBeanData;
    private MyNewsAdapter mAdapter;
    private Handler mHandler ;


    public NewsTabPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mNewsTabData = newsTabData;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.news_tab_layout, null);
        ViewUtils.inject(this,view);
        View headerView = View.inflate(mActivity, R.layout.top_news_layout, null);
        ViewUtils.inject(this,headerView);
        lv_news.addHeaderView(headerView);
        lv_news.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDataFromServer();
            }

            @Override
            public void loadMore() {
                requestMoreDataFromServer();
            }
        });
        return view;
    }

    /**
     * 加载更多数据
     */
    private void requestMoreDataFromServer() {
        //TODO 加载更多数据，重新访问服务器
        HttpUtils httpUtils = new HttpUtils();
        if (!TextUtils.isEmpty(more)){
            httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstanst.ZHBJ_SERVER + more, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //处理数据
                    String result = responseInfo.result;
                    processMoreData(result);

                    //更新页面，调用lv的方法以更新
                    lv_news.onRefreshComplete(true);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //请求失败
                    Toast.makeText(mActivity, "loadmore超时 请检查网络 ", Toast.LENGTH_SHORT).show();
                    lv_news.onRefreshComplete(false);
                }
            });
        }else {
            Toast.makeText(mActivity, "没有更多数据 ", Toast.LENGTH_SHORT).show();
            lv_news.onRefreshComplete(false);
        }

    }

    /**
     * 处理加载到的更多数据
     * @param result 从服务器获取的json
     */
    private void processMoreData(String result) {
        Gson gson = new Gson();
        moreNewsBean = gson.fromJson(result, MoreNewsBean.class);
        moreNewsBeanData = moreNewsBean.data;
        //修改下次加载更多的url
        more = moreNewsBeanData.more;
        ArrayList<NewsTabBean.NewsBean> moreNews = moreNewsBeanData.news;
        mNews.addAll(moreNews);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void initData() {


        mUrl = GlobalConstanst.ZHBJ_SERVER + mNewsTabData.url;
        String cache = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }
        requestDataFromServer();
    }

    private void requestDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrl , new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CacheUtils.saveCache(mActivity,mUrl,result);
                processData(result);
                lv_news.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "请求数据超时 请检查网络 newsTabpager", Toast.LENGTH_SHORT).show();
                lv_news.onRefreshComplete(false);
            }
        });
    }

    private void processData(String result) {



        Gson gson = new Gson();
        mData = gson.fromJson(result, NewsTabBean.class).data;
        mTopNews = mData.topnews;
        mNews = mData.news;
//        for (NewsTabBean.NewsBean newsBean :mNews){
//            Log.d(TAG, "mNews: id:"+newsBean.id+"title:"+newsBean.title);
//        }
        more = mData.more;

        vp_topNews.setAdapter(new TopPagerAdapter());

        indicator.setViewPager(vp_topNews);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_topnews_title.setText(mTopNews.get(position).title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tv_topnews_title.setText(mTopNews.get(0).title);
        //发送空的message 处置轮播
        if (mHandler == null){
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0){
                        int currentItem = vp_topNews.getCurrentItem();
                        currentItem++;
                        if (currentItem == mTopNews.size()-1 ){
                            currentItem = 0;
                        }
                        vp_topNews.setCurrentItem(currentItem);
                        sendEmptyMessageDelayed(0,5000);
                    }

                }
            };
            mHandler.sendEmptyMessageDelayed(0,5000);

            vp_topNews.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            //停止轮播的执行，清楚handler的消息
                            mHandler.removeCallbacksAndMessages(null);

                            break;
                        case MotionEvent.ACTION_UP:

                            mHandler.sendEmptyMessageDelayed(0,5000);
                            break;
                        case MotionEvent.ACTION_CANCEL:

                            mHandler.sendEmptyMessageDelayed(0,5000);
                            break;
                    }

                    return false;
                }
            });
        }




        //列表新聞
        mAdapter = new MyNewsAdapter();
        lv_news.setAdapter(mAdapter);
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**由于添加了两个headerView的关系，
                 * 所以我们需要被点击的条目对应到list数据源里面的position是从2开始的
                 */
                //记录位置，下次进入的时候恢复页面
                NewsTabBean.NewsBean newsClicked =
                        (NewsTabBean.NewsBean) mNews.get(position - 2);
                Log.d(TAG, "onItemClick: 创建已读id："+ newsClicked.id);
                IsReadUtils.saveReadState(mActivity,newsClicked);
                //设置标题为灰色
                TextView tv_news_title_clicked = (TextView) view.findViewById(R.id.tv_news_title);
                tv_news_title_clicked.setTextColor(Color.RED);
                //打开超链接，访问页面
                Intent intent = new Intent(mActivity, NewsPagerActiviry.class);
                intent.putExtra("url",newsClicked.url);
                mActivity.startActivity(intent);

            }
        });

    }

    private class TopPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(mActivity);
            BitmapUtils bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.display(imageView,mTopNews.get(position).topimage);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.news_pic_default);
            container.addView(imageView);
            return imageView;

            /*TextView textView = new TextView(mActivity);
            textView.setText(mTopNews.get(position).title);
            container.addView(textView);
            return textView;*/

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

    private class MyNewsAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mNews.size();
        }

        @Override
        public NewsTabBean.NewsBean getItem(int position) {
            return mNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                holder = new ViewHolder();
                convertView = View.inflate(mActivity,R.layout.item_news_newstab_layout,null);
                holder.iv_listimage = (ImageView) convertView.findViewById(R.id.iv_listimage);
                holder.tv_news_title = (TextView) convertView.findViewById(R.id.tv_news_title);
                holder.tv_news_pubdate = (TextView) convertView.findViewById(R.id.tv_news_pubdate);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            BitmapUtils bitmapUtils = new BitmapUtils(mActivity);
            holder.iv_listimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            bitmapUtils.display(holder.iv_listimage,getItem(position).listimage);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.pic_item_list_default);

            holder.tv_news_title.setText(getItem(position).title);
            holder.tv_news_pubdate.setText(getItem(position).pubdate);
            //判断是否已读，设置文本颜色
            if (IsReadUtils.isReaded(mActivity,getItem(position))) {
                holder.tv_news_title.setTextColor(Color.RED);
                Log.d(TAG, "getView: 该位置的positionid"+getItem(position).id+
                        "title:"+getItem(position).title+"position:"+position);
            }else {
                holder.tv_news_title.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }
    private static class ViewHolder{
        ImageView iv_listimage;
        TextView tv_news_title;
        TextView tv_news_pubdate;

    }

}
