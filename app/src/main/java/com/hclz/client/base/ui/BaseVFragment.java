package com.hclz.client.base.ui;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.baidu.mobstat.StatService;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.login.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/***
 * v4包中的Fragment
 *
 * @author jia-changyu
 */
public abstract class BaseVFragment extends Fragment {
    /**
     * 任务参数集
     **/
    public HashMap<String, String> requestParams = new HashMap<String, String>();
    /**
     * 任务文件集
     **/
    public HashMap<String, String> files = null;
    /**
     * 异步任务
     **/
    public SanmiAsyncTask sanmiAsyncTask;
    /**
     * 上下文对象，等同于this
     **/
    protected Activity mContext;
    public WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 11) {
                gotoLogin();
            }
            if (msg.what == 8888) {
                WaitingDialogControll.dismissLoadingDialog();
            }
            return true;
        }
    });
    /**
     * 页面加载
     */
    private boolean Load;
    /**
     * 加载次数
     */
    private int count = 0;

    /**
     * 配置文件内容
     */
    protected Map<String, String> configMap;

    public BaseVFragment() {
        Load = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 目的 ：防止其他页面返回此页面时重复加载问题
        if (!Load) {
            setNowPage();
        }
        count += 1;
    }

    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    public void onPause() {
        super.onPause();
        StatService.onPause (this);
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
     * 获取数据
     */
    protected abstract void initData();

    /***
     * 设置视图数据
     */
    protected abstract void setViewData();

    /***
     * 设置监听
     */
    protected abstract void setListener();

    /***
     * 页面重载
     */
    public void fragmentHeavy() {
        Load = false;
        count = 1;
    }

    /***
     * 设置当前页面
     */
    public void setNowPage() {
        initView();
        initInstance();
        initData();
        setViewData();
        setListener();
        Load = true;
        count += 1;
    }

    public void refresh(){
        initData();
    }

    protected void gotoLogin() {
        LoginActivity.startMe(getActivity());
    }
}
