package com.example.fbfatboy.zhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by cfb on 2018/6/20.
 */

public class NetBitmapCacheUtils {
    private LocalBitmapCacheUtils mLocalUtils;
    private MemoryBitmapCacheUtils mMemoryUtils;


    public NetBitmapCacheUtils(LocalBitmapCacheUtils localUtils, MemoryBitmapCacheUtils memoryUtils) {
        mLocalUtils = localUtils;
        mMemoryUtils = memoryUtils;
    }

    /**
     * 从网络获取图片并且保存在本地存储和内存中
     * @param imageView
     * @param url_str  图片的url地址
     * @return
     */
    public void getBitmap(ImageView imageView, String url_str) {
        new BitmapTask().execute(imageView,url_str);
    }

    /**
     * 集成AsyncTask以实现异步的操作,Object是doInbackground的形参类型,
     * Integer是onProgressUpdate的形参
     * Bitmap是doInbackGround的返回值类型,并且是onPostExecute的形参
     *
     */
    class BitmapTask extends AsyncTask<Object,Integer,Bitmap> {
        private ImageView imageView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 这个方法在子线程中运行
         * @param params excute()中的参数
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];
            String url = (String) params[1];
            Bitmap bitmap = download(url);
            //保存到本地
            mLocalUtils.setBitmapCache(bitmap,url);
            return bitmap;
        }

        /**
         * 这个方法在doInbackground执行完之后会回调,
         * 并且在主线程中运行,适合更新ui
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageView != null && bitmap != null){
                Log.d(TAG, "onPostExecute: 网络中读取图片");
                imageView.setImageBitmap(bitmap);

            }
        }

        private Bitmap download(String urlStr) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5*1000);
                conn.setReadTimeout(5*1000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200){
                    InputStream inputStream = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    return bitmap;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
