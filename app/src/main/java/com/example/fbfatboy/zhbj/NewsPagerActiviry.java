package com.example.fbfatboy.zhbj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.sharesdk.onekeyshare.OnekeyShare;

import static android.content.ContentValues.TAG;

public class NewsPagerActiviry extends Activity implements View.OnClickListener{
    @ViewInject(R.id.ib_back)
    private ImageButton ib_back;
    @ViewInject(R.id.ib_textsize)
    private ImageButton ib_textsize;
    @ViewInject(R.id.ib_share)
    private ImageButton ib_share;
    @ViewInject(R.id.wv_website)
    private WebView wv_website;
    @ViewInject(R.id.pb_refreshing_website)
    private ProgressBar pb_refreshing_website;
    private String[] textSzieStrs;
    private WebSettings webViewSettings;
    private int current =2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_pager_activiry);
        initView();
        initData();
    }

    private void initData() {
        textSzieStrs = new String[]{"超大字体", "大号字体", "中号字体", "小号字体", "超小字体"};
        webViewSettings = wv_website.getSettings();
        String url = getIntent().getStringExtra("url");
        Log.d("点击当前的url", "initData: "+url);
        wv_website.loadUrl(url);

        webViewSettings.setBuiltInZoomControls(true);//缩放页面按钮
        webViewSettings.setJavaScriptEnabled(true);//添加js支持
        webViewSettings.setUseWideViewPort(true);//双击缩放
//        wv_website.loadUrl("https://www.baidu.com/");
        wv_website.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //show pb
                pb_refreshing_website.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished:onPageFinished ");
                super.onPageFinished(view, url);
                //收起pb
                pb_refreshing_website.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                pb_refreshing_website.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "请求数据超时 请检查网络 webview", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView() {
        ViewUtils.inject(this);
        ib_back.setOnClickListener(this);
        ib_share.setOnClickListener(this);
        ib_textsize.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
                //修改文字的大小
               showDialog();
                break;
            case R.id.ib_share:
                //分享当前页面
                showShare();
                break;
        }
    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体大小");
        builder.setSingleChoiceItems(textSzieStrs, current, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: setsINGLE:"+which);
                current = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: setPotitive:"+which);
                switch (current){
                    case 0 :
                        webViewSettings.setTextZoom(200);
                        break;
                    case 1 :
                        webViewSettings.setTextZoom(150);

                        break;
                    case 2 :
                        webViewSettings.setTextZoom(100);

                        break;
                    case 3 :
                        webViewSettings.setTextZoom(80);

                        break;
                    case 4 :
                        webViewSettings.setTextZoom(50);

                        break;
                }
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

}
