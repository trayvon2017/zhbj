package com.example.fbfatboy.zhbj.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * 模仿BitmapUtils搞一个自己的出来
 * 需要实现三级缓存，网络缓存，本地缓存，内存缓存
 * 实现过程：从网络缓存开始按顺序实现
 * Created by cfb on 2018/6/19.
 */


public class MyBitmapUtils {
    NetBitmapCacheUtils mNetUtils;
    LocalBitmapCacheUtils mLocalUtils;
    MemoryBitmapCacheUtils mMemoryUtils;

    public MyBitmapUtils(MemoryBitmapCacheUtils memoryUtils) {
        mMemoryUtils = memoryUtils;
        mLocalUtils = new LocalBitmapCacheUtils(memoryUtils);
        mNetUtils = new NetBitmapCacheUtils(mLocalUtils,memoryUtils);
    }

    public void display(final ImageView imageId, final String url_str) {
        //先从内存获取
        Bitmap bitmap = mMemoryUtils.getBitmapCache(url_str);
        if (bitmap != null){
            Log.d(TAG, "display: 内存中读取图片成功");
            imageId.setImageBitmap(bitmap);
            return;
        }
        //从本地获取图片
        bitmap = mLocalUtils.getBitmapCache(url_str);
        if (bitmap != null){
            Log.d(TAG, "display: 本地读取图片成功");
            imageId.setImageBitmap(bitmap);
            return;
        }
        //从网络获取图片
        mNetUtils.getBitmap(imageId,url_str);
    }

}
