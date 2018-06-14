package com.example.fbfatboy.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by fbfatboy on 2018/6/14.
 */

public class PhotoJsonBean {
    public PhotoJsonData data;

    public class PhotoJsonData {
        public ArrayList<PhotoNews> news;
    }

    public class PhotoNews {
        public String title;
        public String listimage;
    }
}
