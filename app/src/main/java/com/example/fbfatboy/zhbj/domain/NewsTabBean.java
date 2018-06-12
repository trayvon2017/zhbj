package com.example.fbfatboy.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by fbfatboy on 2018/6/12.
 */

public class NewsTabBean {
    public NewsData data;
    public class NewsData{
        public String more;
        public ArrayList<NewsBean> news;
        public ArrayList<TopNewsBean> topnews;

    }


    public class NewsBean {
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }

    public class TopNewsBean {
        public int id;
        public String pubdate;
        public String title;
        public String topimage ;
        public String type;
        public String url;
    }
}
