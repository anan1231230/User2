package com.hclz.client.base.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

//import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.SDKInitializer;
import com.hclz.client.R;
import com.hclz.client.base.bean.Cart;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.handler.CrashHandler;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.log.LogUploadUtil;
import com.hclz.client.base.util.AppJsonFileReader;
import com.hclz.client.base.util.GetConfigFile;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.wxapi.WXUserInfo;
import com.hyphenate.easeui.controller.EaseUI;
import com.merben.wangluodianhua.NetPhone;
import com.merben.wangluodianhua.listeners.NetPhoneListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;

import java.util.Map;

import cn.beecloud.BeeCloud;
import cn.sharesdk.framework.ShareSDK;

public class HclzApplication extends MultiDexApplication {
    protected static Map<String, String> data;
    private static HclzApplication application;
    //配置文件信息
    private static Context mContext;
    private static Cart cart;
    WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == ProjectConstant.GET_SERVER_CONFIG_SUCCESS) {
                String jsonStr = AppJsonFileReader.getJsonFromConfig(mContext, mContext.getString(R.string.config_file));
                if (!TextUtils.isEmpty(jsonStr)) {
                    data = AppJsonFileReader.setData(jsonStr);
                } else {
                    data = AppJsonFileReader.getDefaultData();
                }
            }
            return true;
        }
    });
    private IWXAPI api;
    private WXUserInfo wxUserInfo;

    public static Map<String, String> getData() {
        if (data == null) {
            String jsonStr = AppJsonFileReader.getJson(mContext,  mContext.getString(R.string.config_file));
            if (!TextUtils.isEmpty(jsonStr)) {
                data = AppJsonFileReader.setData(jsonStr);
            } else {
                data = AppJsonFileReader.getDefaultData();
            }
        }
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public static Cart getCart() {
        if (cart == null) {
            cart = new Cart();
        }
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }


    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        EaseUI.getInstance().init(this,null);
        this.mContext = this.getApplicationContext();
        LogUploadUtil.setContext(mContext);


        //增加网络电话初始化功能
        NetPhone.init(this, new NetPhoneListener() {
            @Override
            public void onNeedRegister(com.merben.wangluodianhua.handler.WeakHandler handler) {
                NetPhone.register(mContext, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_PHONE), handler);
            }
        });

//        SDKInitializer.initialize(this);
        // 推荐在主Activity里的onCreate函数中初始化BeeCloud.
        BeeCloud.setAppIdAndSecret(getString(R.string.beeclound_id),
                getString(R.string.beeclound_secret));

        QbSdk.initX5Environment(mContext,null);
        registerToWX();
        getAppConfig();
        cart = new Cart();
        ShareSDK.initSDK(this);
    }

    private void registerToWX() {
        api = WXAPIFactory.createWXAPI(mContext, getString(R.string.wx_app_id), true);
        api.registerApp(getString(R.string.wx_app_id));
    }

    public void getAppConfig() {
        GetConfigFile configFile = new GetConfigFile(mContext,
                mContext.getString(R.string.config_file),
                mContext.getString(R.string.config_url), handler);
        Thread thread = new Thread(configFile);
        thread.start();
    }

    public IWXAPI getApi() {
        return api;
    }

    public void setApi(IWXAPI api) {
        this.api = api;
    }

    public WXUserInfo getWxUserInfo() {
        return wxUserInfo;
    }

    public void setWxUserInfo(WXUserInfo wxUserInfo) {
        this.wxUserInfo = wxUserInfo;
    }
}
