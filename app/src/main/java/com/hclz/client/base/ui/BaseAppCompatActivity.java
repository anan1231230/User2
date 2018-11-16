package com.hclz.client.base.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.util.ActivityUtility;
import com.hclz.client.base.util.SanmiActivityManager;
import com.hclz.client.login.LoginActivity;
import com.hclz.client.login.RegisterActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;


public abstract class BaseAppCompatActivity extends AppCompatActivity{
    /**
     * 任务参数集
     */
    public HashMap<String, String> requestParams = new HashMap<String, String>();
    public HashMap<String, String> fileParams = new HashMap<>();
    /**
     * 任务文件集
     */
    public HashMap<String, String> files = new HashMap<String, String>();
    public SanmiAsyncTask sanmiAsyncTask;

    /***
     * 关闭获取广播的Activity
     */
//	public void finishActivity() {
//		sendBroadcast(new Intent(BroadcastDefine.BROADCAST_ACTIVITY_FINISH));
//		ActivityUtility.stopMark();
//	}

//	@Override
//	public SysApplication getApplicationContext() {
//		return (SysApplication) super.getApplicationContext();
//	}
    public WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 11) {
                Bundle bundle = msg.getData();
                String gotoActivity = bundle.getString("gotoActivity");
                gotoLogin(gotoActivity);
            }
            return true;
        }
    });
    protected HclzApplication mApplication;
    /**
     * 上下文对象，等同于this
     */
    protected Activity mContext;
    /**
     * 输入法管理器
     */
    protected InputMethodManager mInputMethodManager;
    /**
     * 获取页面传参使用
     */
    protected Intent mIntent;
    /**
     * 配置文件内容
     */
    protected Map<String, String> configMap;
    protected IWXAPI api;
    /**
     * 接收广播
     */
    private BroadcastReceiver bReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        // 共通
        mApplication = (HclzApplication) this.getApplication();
        mContext = this;
        sanmiAsyncTask = new SanmiAsyncTask(mContext);
        sanmiAsyncTask.setHandler(handler);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        SanmiActivityManager.add(this);
        mIntent = getIntent();
//		map = mApplication.getData();
        api = mApplication.getApi();
        setStatusBarEffect();
        // 设置视图
        initView();
        // 实例化
        initInstance();
        // 设置初始数据
        initData();
        // 设置监听
        initListener();
//		if (ActivityUtility.ifMark()) {
//			bReceiver = ActivityUtility.finishActivity(this);
////			registerReceiver(bReceiver, new IntentFilter(
////					BroadcastDefine.BROADCAST_ACTIVITY_FINISH));
//		}

    }

    private void setStatusBarEffect() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return;
//        }
//        /*add title bar background [START]*/
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            return;
//            setTranslucentStatus(true);
//        }
//
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.red_title);
//
//        /*add title bar background[END]*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        //百度统计
        StatService.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
        //百度统计
        StatService.onPause (mContext);
    }

    @Override
    protected void onDestroy() {
        if (bReceiver != null) {
            unregisterReceiver(bReceiver);
            bReceiver = null;
        }
        super.onDestroy();
    }

    protected abstract void init(Bundle savedInstanceState);

    /***
     * 获取视图
     */
    protected abstract void initView();

    /**
     * 实例化
     */
    protected abstract void initInstance();

    /***
     * 视图设置数据
     */
    protected abstract void initData();

    /***
     * 控件设置监听事件
     */
    protected abstract void initListener();

    /***
     * 设置返回按钮
     *
     * @param v
     */
    public void ButtonBackClick(View v) {
        // 隐藏键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(v.getWindowToken(), 0);
        onBackPressed();
    }

    /***
     * 获取标题
     *
     * @return 标题
     */
    private TextView getCommonTitle() {
        TextView titleView = (TextView) findViewById(R.id.tv_comm_head_title);
        return titleView;
    }

    /***
     * 设置标题
     *
     * @param title 标题名字
     */
    protected void setCommonTitle(String title) {
        if (title != null) {
            getCommonTitle().setText(title);
        } else {
            Log.e("title_set", "title is null");
        }

    }

    /***
     * 设置标题
     *
     * @param title 字符串资源文件
     */
    protected void setCommonTitle(int title) {
        getCommonTitle().setText(
                getApplicationContext().getResources().getString(title));
    }

    /**
     * 彻底退出程序
     */
    public void quitApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        // 清空所有activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // 绕过生命周期直接关闭
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /***
     * 开始标记
     */
    public void startMark() {
        ActivityUtility.startMark();
    }

    /***
     * 结束标记
     */
    public void endMark() {
        ActivityUtility.stopMark();
    }

    protected void gotoLogin(String gotoActivity) {
        if (gotoActivity.equals("LoginActivity")) {
            LoginActivity.startMe(mContext);
        }
        if (gotoActivity.equals("RegisterActivity")) {
            RegisterActivity.startMe(mContext);
        }
    }
}
