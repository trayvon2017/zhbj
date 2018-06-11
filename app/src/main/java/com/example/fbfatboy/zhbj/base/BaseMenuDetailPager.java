package com.example.fbfatboy.zhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by fbfatboy on 2018/6/11.
 */

public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootVew;
    public BaseMenuDetailPager(Activity activity){
        mActivity = activity;
        mRootVew = initView();
    }
    public abstract  View initView();
    public void  initData(){};
}
