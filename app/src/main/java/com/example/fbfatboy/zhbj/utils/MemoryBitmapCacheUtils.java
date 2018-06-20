package com.example.fbfatboy.zhbj.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static android.content.ContentValues.TAG;

/**
 * 直接使用hashMap,软引用改进的hashMap,以及lrucache
 * Created by cfb on 2018/6/19.
 */

public class MemoryBitmapCacheUtils {
    /*private   LinkedHashMap<String,Bitmap> memoryBitmaps =
            new LinkedHashMap<String,Bitmap>();*/
    /*private   LinkedHashMap<String,SoftReference<Bitmap>> memoryBitmaps =
            new LinkedHashMap<String,SoftReference<Bitmap>>();*/
    private LruCache<String , Bitmap> memoryCache;

    public MemoryBitmapCacheUtils() {
        long l = Runtime.getRuntime().maxMemory();
        Log.d(TAG, "MemoryBitmapCacheUtils: "+l);
        memoryCache = new LruCache<String , Bitmap>((int)l/8){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    public Bitmap getBitmapCache(String url_str) {
        /*//需要对bitmapSoftReference进行非空判断
        SoftReference<Bitmap> bitmapSoftReference = memoryBitmaps.get(url_str);
        if (bitmapSoftReference != null){
            Bitmap bitmap = bitmapSoftReference.get();
            return bitmap;
        }
        return null;*/
        Bitmap bitmap = memoryCache.get(url_str);
        return bitmap;
    }
    public  void setBitmapCache(Bitmap bitmap,String url_str) {
        /*SoftReference<Bitmap> bitmapSoftReference = new SoftReference<>(bitmap);
        memoryBitmaps.put(url_str,bitmapSoftReference);*/
        memoryCache.put(url_str,bitmap);
    }
}
