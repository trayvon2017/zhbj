package com.example.fbfatboy.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fbfatboy.zhbj.MainActivity;
import com.example.fbfatboy.zhbj.R;

/**
 * Created by dengdeng on 2018/6/7.
 */

public class BasePager {
    public Activity mActivity;
    public TextView mTv_basepager;
    public ImageButton mIb_basepager;
    public FrameLayout mFl_basepager;
    public ImageButton ib_changeStyle;
    public View rootView ;
    public BasePager(Activity activity){
        mActivity = activity;
        rootView = initView();
    }
    public View initView(){
        View view = View.inflate(mActivity, R.layout.base_pager_layout, null);
        mTv_basepager = (TextView) view.findViewById(R.id.tv_basepager);
        mIb_basepager = (ImageButton) view.findViewById(R.id.ib_basepager);
        mFl_basepager = (FrameLayout) view.findViewById(R.id.fl_basepager);
        ib_changeStyle = (ImageButton)view.findViewById(R.id.ib_changeStyle);
        mIb_basepager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        
        return view;

    }

    private void toggle() {
        MainActivity mainUI = (MainActivity)mActivity;
        mainUI.getSlidingMenu().toggle();
    }

    public void initData(){};


}
