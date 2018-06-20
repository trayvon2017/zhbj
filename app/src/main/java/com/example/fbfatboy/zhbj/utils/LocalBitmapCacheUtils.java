package com.example.fbfatboy.zhbj.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by cfb on 2018/6/19.
 */

class LocalBitmapCacheUtils {

    private MemoryBitmapCacheUtils mMemoryUtils;
    private static final String LOCAL_CACHE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath()+ File.separator+"zhbjPicCache";
    private static final String TAG = "LocalBitmapCacheUtils";

    public LocalBitmapCacheUtils(MemoryBitmapCacheUtils memoryUtils) {
        mMemoryUtils = memoryUtils;
    }


    public  void setBitmapCache(Bitmap bitmap, String url_str) {
        File dir = new File(LOCAL_CACHE_PATH);
        if (!dir.exists() && !dir.isDirectory()){
            dir.mkdirs();//创建文件夹
        }
        String fileName;
        try {
            fileName = MD5Encoder.encode(url_str);
            File file = new File(dir, fileName);
            //file路径存在,并且file是文件,说明有这个图片,不在保存
            if (file.exists()&&file.isFile()){
                Log.d(TAG, "saveBitmapCache: 文件存在,不需要缓存");
            }else {
                //缓存到本地
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,
                        new FileOutputStream(file));
                //同时缓存到内存中
                mMemoryUtils.setBitmapCache(bitmap,url_str);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Bitmap getBitmapCache(String url_str){

        Bitmap bitmap = null;
        String fileName = null;
        try {
            fileName = MD5Encoder.encode(url_str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(LOCAL_CACHE_PATH, fileName);
        if (file.exists()&&file.isFile()){
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                //内存中缓存一份
                mMemoryUtils.setBitmapCache(bitmap,url_str);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
