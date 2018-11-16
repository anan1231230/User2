package com.hclz.client.base.util;

import android.content.Context;
import android.os.Message;

import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.handler.WeakHandler;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 获取服务器配置文件存储在app缓存中
 *
 * @author 作者     ：
 * @version 版本号 ：
 *          功能介绍    ：
 */
public class GetConfigFile implements Runnable {
    private static Context mContext;
    private String fileName;
    private String url;
    private WeakHandler handler;

    public GetConfigFile(Context context, String fileName, String url, WeakHandler handler) {
        super();
        mContext = context;
        this.fileName = fileName;
        this.url = url;
        this.handler = handler;
    }

    public static void configFile(String newFilename, String _urlStr) {
        try {
            URL url = new URL(_urlStr);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            CacheFile cacheFile = new CacheFile(mContext);
            cacheFile.writeCache(newFilename, FileUtils.readTextInputStream(is));
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        configFile(fileName, url);
        if (handler != null) {
            Message msg = new Message();
            msg.what = ProjectConstant.GET_SERVER_CONFIG_SUCCESS;
            handler.sendMessage(new Message());
        }
    }

}
