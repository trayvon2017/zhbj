package com.example.fbfatboy.zhbj.base.impl.menu;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.fbfatboy.zhbj.base.BaseMenuDetailPager;

/**
 * Created by fbfatboy on 2018/6/11.
 */

public class InteractMunuDetailPager extends BaseMenuDetailPager {
    public InteractMunuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("菜单详情页 ：互动");
        return textView;
    }
}
