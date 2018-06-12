package com.example.fbfatboy.zhbj.utils;

import android.content.Context;

/**
 * Created by fbfatboy on 2018/6/12.
 */

public class CacheUtils {
    public static void saveCache(Context context, String url, String json){
        SpUtils.putString(context,url,json);
    }
    public static String getCache(Context context,String url){
        return SpUtils.getString(context,url);
    }
}
