package com.example.fbfatboy.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fbfatboy.zhbj.R;
import com.example.fbfatboy.zhbj.utils.SpUtils;

import java.text.SimpleDateFormat;

import static android.content.ContentValues.TAG;

/**
 * 自定义的listView附带下拉刷新功能
 * Created by trayvon on 2018/6/13.
 */

public class PullToRefreshListView extends ListView {

    private static final String LAST_REFRESH_TIME = "last_refresh_time";
    private View headerView;
    private int headerViewMeasuredHeight;
    //触摸ViewPager的时候 会拦截到这个事件 所以要加一个判断
    private int startY = -1;
    private int moveY;
    private int dy;
    private int padding;
    private ImageView iv_arrows;
    private ProgressBar pb_refreshing;
    private TextView tv_explain;
    private TextView tv_refresh_time;

    private static final int PULL_TO_REFRESH = 1;
    private static final int RELEASE_TO_REFRESH = 2;
    private static final int REFRESHING = 3;
    private int currentState = PULL_TO_REFRESH;
    private RotateAnimation animPulltoRelease;
    private RotateAnimation animReleasetoPull;
    private OnRefreshListener mListener;
    private View footerView;
    private int footerViewMeasuredHeight;

    private boolean isLoadmore = false;

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();

    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();

    }

    /**
     * 给listView添加头布局
     */
    private void initHeaderView(){
        initAnimation();
        headerView = View.inflate(getContext(), R.layout.pull_to_refresh_headerview, null);
        iv_arrows = (ImageView) headerView.findViewById(R.id.iv_arrows);
        pb_refreshing = (ProgressBar) headerView.findViewById(R.id.pb_refreshing);
        tv_explain = (TextView) headerView.findViewById(R.id.tv_explain);
        tv_refresh_time = (TextView) headerView.findViewById(R.id.tv_refresh_time);
        //默认下拉状态
        pb_refreshing.setVisibility(INVISIBLE);
        iv_arrows.setVisibility(VISIBLE);
        tv_explain.setText("下拉刷新");

        String last_refresh_time = SpUtils.getString(getContext(), LAST_REFRESH_TIME);
        tv_refresh_time.setText(last_refresh_time);



        headerView.measure(0,0);
        headerViewMeasuredHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0,-headerViewMeasuredHeight,0,0);
        addHeaderView(headerView);
    }

    /**
     * 初始化下拉加载更多的view
     */
    private void initFooterView(){
        footerView = View.inflate(getContext(), R.layout.pull_to_refresh_footerview, null);
        //默认隐藏
        footerView.measure(0,0);
        footerViewMeasuredHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0,-footerViewMeasuredHeight,0,0);

        addFooterView(footerView);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE ){//非滑动触摸状态
                    int lastVisiblePosition = getLastVisiblePosition();
                    if (lastVisiblePosition == getCount() -1){
                        //显示footerView
                        showFooterView();
                        isLoadmore = true;
                        if (mListener != null){
                            //并且执行loadMore
                            mListener.loadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 滑动到最后一个并且停下的时候显示footerView
     */
    private void showFooterView() {
        footerView.setPadding(0,0,0,0);
        setSelection(getLastVisiblePosition());
    }

    /**
     * 重写ontouchevent方法，当第一个条目可见的时候才执行下拉刷新的操作
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //当第一个条目可见的时候才执行下拉的操作
        if (getFirstVisiblePosition()==0&&currentState!=REFRESHING){
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startY = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                {
                    Log.d(TAG, "onTouchEvent: Actionmove首次"+startY);
                    if (startY == -1){
                        startY = (int) ev.getY();
                    }
                    Log.d(TAG, "onTouchEvent: Actionmove新"+startY);
                    moveY = (int) ev.getY();
                    dy = moveY - startY;
                    //根据滑动距离判断下拉条目的展示状态
                    if (dy>0){
                        padding = dy - headerViewMeasuredHeight;
                        headerView.setPadding(0, padding, 0, 0);
                        if (padding<=0){//下拉状态，页面更新
                            if(currentState != PULL_TO_REFRESH){
                                currentState = PULL_TO_REFRESH;
                                refreshHeaderView();
                            }

                        }else{//下拉刷新状态，更新页面
                            headerView.setPadding(0, 0, 0, 0);
                            if (currentState != RELEASE_TO_REFRESH){
                                currentState = RELEASE_TO_REFRESH;
                                refreshHeaderView();
                            }

                        }
                        return true;
                    }
                }
                break;
                case MotionEvent.ACTION_UP:
                    if (currentState == RELEASE_TO_REFRESH){
                        currentState = REFRESHING;
                        refreshHeaderView();
                    }
                    if (currentState == PULL_TO_REFRESH){
                        //下拉状态 不用做任何操作 收起顶栏即可
                        hideHeaderView();
                    }
                    startY = -1;
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void hideHeaderView() {
        headerView.setPadding(0,-headerViewMeasuredHeight,0,0);
        currentState = PULL_TO_REFRESH;
        pb_refreshing.setVisibility(INVISIBLE);
        iv_arrows.setVisibility(VISIBLE);
        tv_explain.setText("下拉刷新");
    }

    private void initAnimation(){
        animPulltoRelease = new RotateAnimation(0,180,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        animPulltoRelease.setDuration(300);
        animPulltoRelease.setFillAfter(true);
        animReleasetoPull = new RotateAnimation(180,0,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        animReleasetoPull.setDuration(300);
        animReleasetoPull.setFillAfter(true);
    }

    /**
     * 根据当前的状态更新头标
     */
    private void refreshHeaderView() {
        switch (currentState){
            case PULL_TO_REFRESH:
                pb_refreshing.setVisibility(INVISIBLE);
                iv_arrows.setVisibility(VISIBLE);
                tv_explain.setText("下拉刷新");
                iv_arrows.startAnimation(animReleasetoPull);
                break;
            case RELEASE_TO_REFRESH:
                pb_refreshing.setVisibility(INVISIBLE);
                iv_arrows.setVisibility(VISIBLE);
                tv_explain.setText("释放更新");
                iv_arrows.startAnimation(animPulltoRelease);
                break;
            case REFRESHING:
                iv_arrows.clearAnimation();
                tv_explain.setText("正在更新");
                pb_refreshing.setVisibility(VISIBLE);
                iv_arrows.setVisibility(INVISIBLE);

                if (mListener != null){
                    mListener.onRefresh();
                }

                break;

        }

    }

    private void setCurrentFormatTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long  currentTimeMillis = System.currentTimeMillis();
        String last_refresh_time = simpleDateFormat.format(currentTimeMillis);
        tv_refresh_time.setText(last_refresh_time);
        //存储上次更新页面的时间
        SpUtils.putString(getContext(),LAST_REFRESH_TIME,last_refresh_time);
    }
    public interface OnRefreshListener{
        public void onRefresh();

        public void loadMore();
    }
    public void setOnRefreshListener(OnRefreshListener listener){
        mListener = listener;
    }
    public void onRefreshComplete(boolean isSuccess){

        if (isLoadmore){

            //隐藏掉footerview
            hideFooterView();


        }else{
            hideHeaderView();
            if (isSuccess){
                setCurrentFormatTime();

            }
        }
    }

    private void hideFooterView() {
        isLoadmore = false;
        footerView.setPadding(0,-footerViewMeasuredHeight,0,0);
    }

}
