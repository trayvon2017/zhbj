package com.example.fbfatboy.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.fbfatboy.zhbj.utils.SpUtils;

public class SplashActivity extends Activity {

    public static final String IS_FIRST_IN = "is_first_in";
    private RelativeLayout mRl_splash;
    private AnimationSet animationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        initAnimation();
        startAnimation();
    }

    private void initAnimation() {
        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setFillAfter(true);
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);
        //渐变
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);
        //创建动画集合
        animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
    }

    private void startAnimation() {
        mRl_splash.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //做出判断，是否第一次进入，SP存储判断
                boolean isFirst = SpUtils.getBoolean(getApplicationContext(), IS_FIRST_IN);
                if (isFirst){
                    //是的话进入guideActivity
                    startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                }else {
                    ////不是第一次进入，这时候直接进入主页面
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));

                }
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initUI() {
        mRl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
    }
}
