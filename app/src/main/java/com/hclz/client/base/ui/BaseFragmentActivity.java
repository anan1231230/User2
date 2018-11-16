/**
 * 类       名:BaseFragmentActivity
 * 作       者:
 * 主要功能:主Tab须继承 本BaseFragment
 * 创建日期：2015-09-10 13:54:00
 * 修  改  者：
 * 修改日期：
 * 修改内容：
 */
package com.hclz.client.base.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.hclz.client.R;
import com.hclz.client.base.util.ActivityUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SanmiActivityManager;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;


public abstract class BaseFragmentActivity extends FragmentActivity {
    /**
     * 任务参数集
     **/
    public HashMap<String, String> requestParams = new HashMap<String, String>();
    /**
     * 任务文件集
     **/
    public HashMap<String, String> files = new HashMap<String, String>();
    /**
     * 上下文对象，等同于this
     **/
    protected Activity mContext;
    /**
     * 获取传参使用
     **/
    protected Intent mIntent;
    /**
     * 输入法管理器
     **/
    protected InputMethodManager mInputMethodManager;

    /**
     * 配置文件内容
     */
    protected Map<String, String> configMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 共通
        mContext = this;
        mIntent = getIntent();
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        SanmiActivityManager.add(this);
        setStatusBarEffect();
        // 设置控件
        initView();
        // 实例化
        initInstance();
        // 设置数据
        initData();
        // 设置监听
        initListener();
        if (!PostHttpUtil.isnetWorkAvilable(mContext)){
            Toast.makeText(mContext,"网络无连接,请开启网络连接",Toast.LENGTH_LONG).show();
        }
    }

    private void setStatusBarEffect() {
//        /*add title bar background [START]*/
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return;
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.red_title);
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        //百度统计
        StatService.onResume(mContext);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
        //百度统计
        StatService.onPause (mContext);
    }

    /***
     * 销毁监听事件
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /***
     * 设置视图
     */
    protected abstract void initView();

    /**
     * 实例化
     */
    protected abstract void initInstance();

    /***
     * 设置数据
     */
    protected abstract void initData();

    /***
     * 设置监听
     */
    protected abstract void initListener();

    /***
     * 返回按钮设置
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
     * 完全退出程序
     */
    public void quitApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
     * @param title   字符串资源文件
     */
    protected void setCommonTitle(int title) {
        getCommonTitle().setText(
                getApplicationContext().getResources().getString(title));
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
}
