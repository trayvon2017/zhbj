package com.example.fbfatboy.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by fbfatboy on 2018/6/12.
 */

public class TopNewsViewPager extends ViewPager {


    private int startX;
    private int startY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


        //请求自己处理事件
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                Log.d("ACTION_DOWN", "dispatchTouchEvent: "+ startX +"-"+ startY);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int dX = moveX - startX;
                int dY = moveY - startY;
                Log.d("ACTION_MOVE", "dispatchTouchEvent: "+ startX +"-"+ startY);
                //判断是上下滑动还是左右滑动
                if (Math.abs(dX)>Math.abs(dY)){
                    if (dX>0){
                        //在第一页的时候拦截
                        if(getCurrentItem() == 0){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else{

                        if (getCurrentItem()==(getAdapter().getCount()-1)){

                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;

        }

        return super.dispatchTouchEvent(ev);
    }
}
