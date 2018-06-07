package com.example.fbfatboy.zhbj;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.fbfatboy.zhbj.fragment.LeftMenuFragment;
import com.example.fbfatboy.zhbj.fragment.MainContentFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    private static final String LEFT_MENU_FRAGMENT = "left_menu_fragment";
    private static final String MAIN_CONTENT_FRAGMENT = "main_content_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(360);
        initFragment();
    }

    private void initFragment() {
        FragmentManager sFm = getSupportFragmentManager();
        FragmentTransaction transaction = sFm.beginTransaction();
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),LEFT_MENU_FRAGMENT);
        transaction.replace(R.id.fl_main,new MainContentFragment(),MAIN_CONTENT_FRAGMENT);
        transaction.commit();
    }
}
