package com.example.fbfatboy.zhbj.utils;

import android.content.Context;
import android.util.Log;

import com.example.fbfatboy.zhbj.domain.NewsTabBean;

import static android.content.ContentValues.TAG;

/**
 * Created by trayvon on 2018/6/14.
 */

public class IsReadUtils {

    private static final String IS_READED = "is_readed";

    /**
     * 判断是否已读
     * @return
     */
    public static boolean isReaded(Context context, NewsTabBean.NewsBean newsBean){
        String readedIds = getReadedIds(context);
        Log.d(TAG, "已存ids:  "+readedIds);
        return readedIds.contains(newsBean.id+"");
    }

    /**
     * 存储已读的新闻id
     * @param context
     * @param newsBean
     */
    public static void saveReadState(Context context, NewsTabBean.NewsBean newsBean){

        if (!isReaded(context,newsBean)){
            String readedIds = getReadedIds(context);
            readedIds = readedIds + newsBean.id +",";
            SpUtils.putString(context,IS_READED,readedIds);
        }
    }

    /**
     * 获取已存的已读ids
     * @param context
     * @return
     */
    private static String getReadedIds(Context context){
        return SpUtils.getString(context, IS_READED);
    }

}
