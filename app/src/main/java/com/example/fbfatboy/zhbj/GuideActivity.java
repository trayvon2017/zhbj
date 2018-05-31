package com.example.fbfatboy.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.fbfatboy.zhbj.utils.SpUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private ViewPager mVpGuide;
    private RelativeLayout mRlPoints;
    private ArrayList<ImageView> mImageList;
    private int mDisWithInTwoPoints;
    private ImageView redPoint;
    private Button mbtn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initData();
        initUI();

    }

    private void initData() {
        mImageList = new ArrayList<>();
        int[] imageIds = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        for (int i :imageIds){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(i);
            mImageList.add(imageView);
        }
    }

    private void initUI() {
        mVpGuide = (ViewPager)findViewById(R.id.vp_guide);
        mRlPoints = (RelativeLayout)findViewById(R.id.rl_points);
        mbtn_start = (Button)findViewById(R.id.btn_start);
        mbtn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                SpUtils.putBoolean(getApplicationContext(),SplashActivity.IS_FIRST_IN,false);
                finish();
            }
        });
        mVpGuide.setAdapter(new MyPageAdapter());
        for (int i = 0 ;i<mImageList.size();i++){
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.pointer_gray);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            if (i>0){
                Log.d("guide-pointer", "initUI: ");
                //margin值变化
                params.leftMargin = 40*i;
            }
            //红点
            point.setLayoutParams(params);
            mRlPoints.addView(point);
            final ViewTreeObserver viewTreeObserver = mRlPoints.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

//                    mDisWithInTwoPoints = mRlPoints.getChildAt(1).getLeft() - mRlPoints.getWidth();
                    mDisWithInTwoPoints = mRlPoints.getChildAt(1).getLeft();
                    Log.d("测算两个原点之间距离", "onGlobalLayout: "+mDisWithInTwoPoints);
                    mRlPoints.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });

        }
        redPoint = new ImageView(this);
        redPoint.setBackgroundResource(R.drawable.pointer_red);
        mRlPoints.addView(redPoint);


        mVpGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int newLeft = (int) (position * mDisWithInTwoPoints + positionOffset * mDisWithInTwoPoints);
                Log.d("测算newLeft", "onPageScrolled: "+newLeft);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.leftMargin = newLeft;
                redPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                //判断页面位置，决定是否显示按钮
                if (position == mImageList.size()-1){
                    //显示Button
                    mbtn_start.setVisibility(View.VISIBLE);
                }else {
                    mbtn_start.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyPageAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageList.get(position));
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
