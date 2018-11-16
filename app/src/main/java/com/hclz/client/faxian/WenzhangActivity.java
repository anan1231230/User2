package com.hclz.client.faxian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.ui.BaseAppCompatActivity;

public class WenzhangActivity extends BaseAppCompatActivity {

    private TextView mTitleTv;
    private WebView wv_wenzhang;
    private String mTitle,mUrl;
    private ImageView mBackIv;

    ProgressBar pb;


    public static void startMe(Context from, String title, String url) {
        Intent intent = new Intent(from, WenzhangActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wenzhang);
    }

    @Override
    protected void initView() {
        mTitleTv = (TextView) findViewById(R.id.product_title);
        mBackIv = (ImageView) findViewById(R.id.iv_comm_head_left);
        wv_wenzhang = (WebView) findViewById(R.id.wv_wenzhang);
        pb = (ProgressBar) findViewById(R.id.pb);

        //启用支持javascript
        WebSettings settings = wv_wenzhang.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    protected void initInstance() {
        configMap = HclzApplication.getData();
        mIntent = mContext.getIntent();
        if (mIntent != null) {
            mTitle = mIntent.getStringExtra("title");
            mUrl = mIntent.getStringExtra("url");
        }
    }

    private void showContent() {
        mTitleTv.setText(mTitle);
        //WebView加载web资源
        wv_wenzhang.loadUrl(mUrl);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv_wenzhang.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        wv_wenzhang.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb.setProgress(newProgress);
                if(newProgress==100){
                    pb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

    }

    @Override
    protected void initData() {
        showContent();
    }

    @Override
    protected void initListener() {
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(wv_wenzhang.canGoBack())
            {
                wv_wenzhang.goBack();//返回上一页面
                return true;
            }
            else
            {
                finish();//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
