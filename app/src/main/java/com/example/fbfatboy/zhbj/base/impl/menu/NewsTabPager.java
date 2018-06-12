package com.example.fbfatboy.zhbj.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbfatboy.zhbj.R;
import com.example.fbfatboy.zhbj.base.BaseMenuDetailPager;
import com.example.fbfatboy.zhbj.domain.NewsMenu;
import com.example.fbfatboy.zhbj.domain.NewsTabBean;
import com.example.fbfatboy.zhbj.global.GlobalConstanst;
import com.example.fbfatboy.zhbj.utils.CacheUtils;
import com.example.fbfatboy.zhbj.utils.SpUtils;
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
import com.viewpagerindicator.CirclePageIndicator;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by fbfatboy on 2018/6/12.
 */

public class NewsTabPager extends BaseMenuDetailPager {
    private NewsMenu.NewsTabData mNewsTabData;
    private String mUrl;

    @ViewInject(R.id.vp_topNews)
    private TopNewsViewPager vp_topNews;

    @ViewInject(R.id.lv_news)
    private ListView lv_news;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator indicator;

    @ViewInject(R.id.tv_topnews_title)
    private TextView tv_topnews_title;


    private NewsTabBean.NewsData mData;
    private ArrayList<NewsTabBean.TopNewsBean> mTopNews;
    private ArrayList<NewsTabBean.NewsBean> mNews;


    public NewsTabPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mNewsTabData = newsTabData;
    }

    @Override
    public View initView() {
        //TODO 加载布局
        View view = View.inflate(mActivity, R.layout.news_tab_layout, null);
        ViewUtils.inject(this,view);
        View headerView = View.inflate(mActivity, R.layout.top_news_layout, null);
        ViewUtils.inject(this,headerView);
        lv_news.addHeaderView(headerView);
        return view;
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
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "请求数据超时 请检查网络 newsTabpager", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processData(String result) {



        Gson gson = new Gson();
        mData = gson.fromJson(result, NewsTabBean.class).data;
        mTopNews = mData.topnews;
        mNews = mData.news;

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





        //列表新聞
        lv_news.setAdapter(new MyNewsAdapter());

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

            return convertView;
        }
    }
    private static class ViewHolder{
        ImageView iv_listimage;
        TextView tv_news_title;
        TextView tv_news_pubdate;

    }

}
