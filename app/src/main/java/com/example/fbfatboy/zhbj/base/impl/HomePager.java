package com.example.fbfatboy.zhbj.base.impl;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.fbfatboy.zhbj.R;
import com.example.fbfatboy.zhbj.base.BasePager;

/**
 * Created by dengdeng on 2018/6/7.
 */

public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        mIb_basepager.setVisibility(View.GONE);

        mTv_basepager.setText("主页");
        TextView textView = new TextView(mActivity);
        textView.setText("我是首页");
        textView.setTextColor(mActivity.getResources().getColor(R.color.btn));
        mFl_basepager.addView(textView);
    }
}
