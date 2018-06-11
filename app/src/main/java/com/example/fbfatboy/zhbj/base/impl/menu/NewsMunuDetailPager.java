package com.example.fbfatboy.zhbj.base.impl.menu;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.fbfatboy.zhbj.base.BaseMenuDetailPager;

import org.w3c.dom.Text;

/**
 * Created by fbfatboy on 2018/6/11.
 */

public class NewsMunuDetailPager extends BaseMenuDetailPager {
    public NewsMunuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("菜单详情页 ：新闻");
        return textView;
    }
}
