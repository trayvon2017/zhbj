package com.example.fbfatboy.zhbj.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fbfatboy.zhbj.MainActivity;
import com.example.fbfatboy.zhbj.R;
import com.example.fbfatboy.zhbj.base.BaseMenuDetailPager;
import com.example.fbfatboy.zhbj.domain.NewsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by fbfatboy on 2018/6/11.
 */

public class NewsMunuDetailPager extends BaseMenuDetailPager {
    private ArrayList<NewsMenu.NewsTabData> mChildren;

    private ArrayList<NewsTabPager> mPagers;

    @ViewInject(R.id.vp_newscenter_newstab)
    private ViewPager vp_newscenter_newstab;

    @ViewInject(R.id.tab_indicator)
    private TabPageIndicator tab_indicator;

    @ViewInject(R.id.ibtn_next_tab)
    private ImageButton ibtn_next_tab;

    public NewsMunuDetailPager(Activity activity,ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        mChildren = children;
    }

    @Override
    public View initView() {


        View view = View.inflate(mActivity, R.layout.news_center_tabs_layout, null);
        ViewUtils.inject(this,view);


        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<NewsTabPager>();
        //初始化页签
        for (int i = 0;i< mChildren.size();i++){
            mPagers.add(new NewsTabPager(mActivity,mChildren.get(i)));
        }
        vp_newscenter_newstab.setAdapter(new TabPagerAdapter());
        tab_indicator.setViewPager(vp_newscenter_newstab);
        ibtn_next_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = vp_newscenter_newstab.getCurrentItem();
                vp_newscenter_newstab.setCurrentItem(++currentItem);
            }
        });
        tab_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPagers.get(position).initData();
                MainActivity mainUI = (MainActivity) NewsMunuDetailPager.this.mActivity;
                if (position == 0){

                    //开启侧边栏
                    mainUI.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else{
                    //禁用侧边栏
                    mainUI.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagers.get(0).initData();
    }

    public class TabPagerAdapter extends PagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {

            return mChildren.get(position).title;
        }

        @Override
        public int getCount() {
            return mChildren.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsTabPager newsTabPager = mPagers.get(position);

            View view = newsTabPager.mRootVew;
//            newsTabPager.initData();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
