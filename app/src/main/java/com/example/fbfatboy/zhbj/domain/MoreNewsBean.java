package com.example.fbfatboy.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by trayvon on 2018/6/14.
 */

public class MoreNewsBean {
    public MoreNewsData data;

    public class MoreNewsData {
        public String more;
        public ArrayList<NewsTabBean.NewsBean> news;
    }


}
