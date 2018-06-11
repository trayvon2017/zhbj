package com.example.fbfatboy.zhbj.fragment;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.fbfatboy.zhbj.MainActivity;
import com.example.fbfatboy.zhbj.R;
import com.example.fbfatboy.zhbj.base.BasePager;
import com.example.fbfatboy.zhbj.base.impl.GovPager;
import com.example.fbfatboy.zhbj.base.impl.HomePager;
import com.example.fbfatboy.zhbj.base.impl.NewsPager;
import com.example.fbfatboy.zhbj.base.impl.SettingPager;
import com.example.fbfatboy.zhbj.base.impl.SmartServicePager;
import com.example.fbfatboy.zhbj.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by dengdeng on 2018/6/7.
 */

public class MainContentFragment extends BaseFragment {
    private ArrayList<BasePager> mPagers;
    private NoScrollViewPager mVp_main_content;
    private RadioGroup mRgtabs;
    private MainActivity mainUI;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.main_content_fragment, null);
        mVp_main_content = (NoScrollViewPager) view.findViewById(R.id.vp_main_content);
        mRgtabs = (RadioGroup) view.findViewById(R.id.rg_tabs);

        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovPager(mActivity));
        mPagers.add(new SettingPager(mActivity));
        mVp_main_content.setAdapter(new MyPagerAdapter());
        mPagers.get(0).initData();
        mainUI = (MainActivity) MainContentFragment.this.mActivity;
        mainUI.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        mVp_main_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPagers.get(position).initData();
                if (position == 0 || position == 4){

                    mainUI.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }else {
                    mainUI.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //这是radiogroup
        mRgtabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.rb_home_tab:
                        mVp_main_content.setCurrentItem(0,false);
                        break;
                    case R.id.rb_news_tab:
                        mVp_main_content.setCurrentItem(1,false);

                        break;
                    case R.id.rb_smartservice_tab:
                        mVp_main_content.setCurrentItem(2,false);

                        break;
                    case R.id.rb_gov_tab:
                        mVp_main_content.setCurrentItem(3,false);

                        break;
                    case R.id.rb_setting_tab:
                        mVp_main_content.setCurrentItem(4,false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public NewsPager getNewsPager() {
        return (NewsPager)mPagers.get(1);
    }


    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
//            pager.initData();
            container.addView(pager.rootView);
            return pager.rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
