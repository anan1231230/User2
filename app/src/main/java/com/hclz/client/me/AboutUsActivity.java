package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hclz.client.R;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.view.WaitingDialogControll;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class AboutUsActivity extends BaseActivity {

//    private TextView tvInfo;
//    private ListView lvFeatures;
    private String url = "http://www.haochilanzuo.com/merben/about.html";
    private WebView mWebview;
    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 8888:
                    WaitingDialogControll.dismissLoadingDialog();
                    break;
            }
            return true;
        }
    });

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, AboutUsActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_about_us);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
//        tvInfo = (TextView) findViewById(R.id.tv_info);
//        lvFeatures = (ListView) findViewById(R.id.lv_features);
        mWebview = (WebView) findViewById(R.id.wv_about_us);
        mWebview.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
//        configMap = HclzApplication.getData();
        setCommonTitle(R.string.about_us);
//        String aboutHclz = configMap.get("about_hclz");
//        JsonObject obj = JsonUtility.parse(aboutHclz);
//        tvInfo.setText(obj.get("info") == null ? "" : obj.get("info").getAsString());
//        String[] features = JsonUtility.fromJson(obj.get("features"), new TypeToken<String[]>() {
//        });
//        ArrayAdapter adapter = new ArrayAdapter<>(mContext, R.layout.item_note_list, features);
//        lvFeatures.setAdapter(adapter);
        showLoadingDialog();
        mWebview.loadUrl(url);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dissmissDialog();
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onPause() {
        Log.i("hjm","onPause");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mWebview != null) {
                mWebview.onPause(); // 暂停网页中正在播放的视频
            }
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mWebview != null) {
                mWebview.onPause(); // 暂停网页中正在播放的视频
            }
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mWebview != null) {
                mWebview.onPause(); // 暂停网页中正在播放的视频
            }
        }
        super.onDestroy();
    }

    private void dissmissDialog() {
        mHandler.sendEmptyMessage(8888);
    }

    private void showLoadingDialog() {
        WaitingDialogControll.showLoadingDialog(mContext);
        mHandler.sendEmptyMessageDelayed(8888, 20000);
    }
}
