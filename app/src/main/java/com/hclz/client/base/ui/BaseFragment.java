/**
 * 类       名:BaseFragment
 * 作       者:
 * 主要功能:子Tab须继承 本BaseFragment
 * 创建日期：2015-09-10 13:54:00
 * 修  改  者：
 * 修改日期：
 * 修改内容：
 */

package com.hclz.client.base.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import com.baidu.mobstat.StatService;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.Cart;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.login.LoginActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;

import java.util.HashMap;
import java.util.Map;


public abstract class BaseFragment extends Fragment {
    /**
     * 任务参数集
     **/
    public HashMap<String, String> requestParams = new HashMap<String, String>();
    /**
     * 任务文件集
     **/
    public HashMap<String, String> files = new HashMap<String, String>();
    public SanmiAsyncTask sanmiAsyncTask;
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
    protected HclzApplication mApplication;
    /**
     * 上下文对象，等同于this
     **/
    protected Activity mContext;
    /**
     * 输入法管理器
     **/
    protected InputMethodManager mInputMethodManager;
    /**
     * 配置文件内容
     */
    protected Map<String, String> configMap;
    protected Cart cart;
    protected IWXAPI api;
    /**
     * 页面加载
     */
    private Boolean Load;

    public BaseFragment() {
        Load = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (HclzApplication) this.getActivity().getApplication();
        mContext = this.getActivity();
        sanmiAsyncTask = new SanmiAsyncTask(mContext);
        sanmiAsyncTask.setHandler(handler);
//		map = mApplication.getData();
        cart = HclzApplication.getCart();
        api = mApplication.getApi();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 目的 ：防止其他页面返回此页面时重复加载问题
        if (!Load) {
            setNowPage();
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /***
     * 页面重载
     */
    public void fragmentHeavy() {
        Load = false;
        onStart();
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
    }

    protected void gotoLogin() {
        LoginActivity.startMe(getActivity());
    }
}
