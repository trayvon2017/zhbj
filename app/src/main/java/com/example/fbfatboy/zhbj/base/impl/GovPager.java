package com.example.fbfatboy.zhbj.base.impl;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.fbfatboy.zhbj.R;
import com.example.fbfatboy.zhbj.base.BasePager;

/**
 * Created by dengdeng on 2018/6/7.
 */

public class GovPager extends BasePager {
    public GovPager(Activity activity) {
        super(activity);
    }
    @Override
    public void initData() {
        mTv_basepager.setText("政务");
        mIb_basepager.setVisibility(View.VISIBLE);
        TextView textView = new TextView(mActivity);
        textView.setText("我是gov页");
        textView.setTextColor(mActivity.getResources().getColor(R.color.btn));
        mFl_basepager.addView(textView);
    }
}
