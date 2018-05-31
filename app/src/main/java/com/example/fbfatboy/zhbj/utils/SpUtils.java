package com.example.fbfatboy.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fbfatboy on 2018/5/29.
 */

public class SpUtils {
    public static void putBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public static boolean getBoolean(Context context,String key){

        return context.getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean(key,true);
    }

}
