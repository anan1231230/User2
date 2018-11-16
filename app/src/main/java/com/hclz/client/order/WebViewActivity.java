package com.hclz.client.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.PostHttpUtil;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class WebViewActivity extends BaseActivity {
    private TextView tvRight;
    private WebView webview;

    private String title;
    private String url;

    private boolean isShowRightButton = false;

    /**
     * 页面跳转
     *
     * @param from
     * @param title 标题
     * @param url webview加载地址
     */
    public static void startMe(Context from,String title,String url,boolean isShowRightButton){
        Intent intent = new Intent(from, WebViewActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        intent.putExtra("isShowRightButton", isShowRightButton);
        from.startActivity(intent);
    }

    /**
     * 页面跳转
     *
     * @param from
     * @param title 标题
     * @param url webview加载地址
     */
    public static void startMe(Context from,String title,String url){
        Intent intent = new Intent(from, WebViewActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_webview);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initView() {
        tvRight = (TextView) findViewById(R.id.txt_comm_head_rght);
        webview = (WebView) findViewById(R.id.webview);
        // 设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        configMap = HclzApplication.getData();

        if (mIntent != null) {
            title = mIntent.getStringExtra(ProjectConstant.WEBVIEW_TITLE);
            url = mIntent.getStringExtra(ProjectConstant.WEBVIEW_URL);
            isShowRightButton = mIntent.getBooleanExtra("isShowRightButton", false);
            setCommonTitle(title);
            if (isShowRightButton) {
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText(getString(R.string.to_get_coupon));
            }
            // 加载需要显示的网页
            webview.loadUrl(url);
            // 设置Web视图
            webview.setWebViewClient(new HelloWebViewClient());
        }
    }

    @Override
    protected void initListener() {
        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 加载需要显示的网页
                tvRight.setVisibility(View.GONE);
                setCommonTitle(R.string.get_coupon);
                webview.loadUrl(ServerUrlConstant.GET_COUPON.getWebViewMethod() + "?" + PostHttpUtil.getBasicUrl(mContext, configMap, true));
            }
        });
    }

    // Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
