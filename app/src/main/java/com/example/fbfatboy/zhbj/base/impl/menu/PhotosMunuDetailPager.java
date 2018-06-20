package com.example.fbfatboy.zhbj.base.impl.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbfatboy.zhbj.R;
import com.example.fbfatboy.zhbj.base.BaseMenuDetailPager;
import com.example.fbfatboy.zhbj.domain.PhotoJsonBean;
import com.example.fbfatboy.zhbj.global.GlobalConstanst;
import com.example.fbfatboy.zhbj.utils.CacheUtils;
import com.example.fbfatboy.zhbj.utils.MemoryBitmapCacheUtils;
import com.example.fbfatboy.zhbj.utils.MyBitmapUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by fbfatboy on 2018/6/11.
 */

public class PhotosMunuDetailPager extends BaseMenuDetailPager {
    @ViewInject(R.id.lv_photos)
    private ListView lvPhotos;
    @ViewInject(R.id.gv_photos)
    private GridView gvPhotos;
    private ImageButton mIbChangeStyle;
    private ArrayList<PhotoJsonBean.PhotoNews> mPhotoNews;
    private MyBitmapUtils mBitmapUtils;
    private boolean isListView ;


    public PhotosMunuDetailPager(Activity mActivity, ImageButton ib_changeStyle) {
        super(mActivity);
        mIbChangeStyle = ib_changeStyle;
    }

    public PhotosMunuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.photos_detail_layout, null);
        ViewUtils.inject(this,view);

        return view;
    }

    @Override
    public void initData() {
        isListView = true;
        showListStyle();
        mIbChangeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListView){
                    showGridView();
                }else {
                    showListStyle();
                }
                isListView = !isListView;
            }
        });
        String cache = CacheUtils.getCache(mActivity, GlobalConstanst.PHOTOS_JSON);
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }
        requestDataFromServer();
    }

    private void showGridView() {
        lvPhotos.setVisibility(View.GONE);
        gvPhotos.setVisibility(View.VISIBLE);
        mIbChangeStyle.setImageResource(R.drawable.icon_pic_grid_type);
    }

    private void showListStyle() {
        lvPhotos.setVisibility(View.VISIBLE);
        gvPhotos.setVisibility(View.GONE);
        mIbChangeStyle.setImageResource(R.drawable.icon_pic_list_type);

    }

    /**
     * 处理json，填充数据
     * @param json
     */
    private void processData(String json) {
        Gson gson = new Gson();
        PhotoJsonBean photoJsonBean = gson.fromJson(json, PhotoJsonBean.class);
        mPhotoNews = photoJsonBean.data.news;
        MyPhotoNewsAdapter myPhotoNewsAdapter = new MyPhotoNewsAdapter();
        lvPhotos.setAdapter(myPhotoNewsAdapter);
        gvPhotos.setAdapter(myPhotoNewsAdapter);
    }

    private void requestDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstanst.PHOTOS_JSON, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CacheUtils.saveCache(mActivity,GlobalConstanst.PHOTOS_JSON,result);
                processData(result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "请求数据超时 请检查网络 photosPager", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class MyPhotoNewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPhotoNews.size();
        }

        @Override
        public PhotoJsonBean.PhotoNews getItem(int position) {
            return mPhotoNews.get(position);
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
                convertView = View.inflate(mActivity,R.layout.item_photo_news_layout,null);
                holder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo_listimage);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_photo_title);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            //使用BitmapUtils获取到图片，并且设置给
            if (mBitmapUtils == null){
                mBitmapUtils = new MyBitmapUtils(new MemoryBitmapCacheUtils());
            }
            mBitmapUtils.display(holder.ivPhoto,getItem(position).listimage);
            holder.tvTitle.setText(getItem(position).title);

            return convertView;
        }
    }
    static class ViewHolder{
        public ImageView ivPhoto;
        public TextView tvTitle;
    }
}
