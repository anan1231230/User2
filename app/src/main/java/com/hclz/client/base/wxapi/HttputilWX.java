package com.hclz.client.base.wxapi;

import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.log.SanmiLogger;
import com.hclz.client.base.util.StreamUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttputilWX {

    private static HttpURLConnection conn = null;
    private static DataOutputStream dos;
    private static String data;

    public static String GetServer(String path) {
        URL url = null;
        InputStream inputStream = null;
        try {
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(SanmiConfig.TIMEOUT_CONNECT_HTTP);
            conn.setReadTimeout(SanmiConfig.TIMEOUT_READ_FILE);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            dos = new DataOutputStream(conn.getOutputStream());

            dos.flush();
            dos.close();
            data = StreamUtil.iputStreamToString(get(conn));
            SanmiLogger.d(HttputilWX.class.getName(), "The back data is \n" + data);
        } catch (Exception e) {
            data = null;
        } finally {
            if (inputStream != null) {
                inputStream = null;
            }
            if (dos != null) {
                dos = null;
            }
            if (conn != null) {
                conn = null;
            }
        }
        return data;
    }

    /**
     * 获取服务器返回流
     *
     * @param conn 连接
     * @return InputStream or null
     * @throws IOException
     */
    private static InputStream get(HttpURLConnection conn) throws IOException {
        int code = conn.getResponseCode();
        return (code == HttpURLConnection.HTTP_OK) ? conn.getInputStream()
                : null;
    }

}
