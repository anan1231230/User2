package com.hclz.client.base.ver;

import android.util.Log;

import com.hclz.client.base.util.CommonUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 简单的下载器
 */
public class SimpleDownloader {
    private static final String TAG = SimpleDownloader.class.getSimpleName();
    private static final int IO_BUFFER_SIZE = 8 * 1024;

    private SimpleDownloader() {
    }

    /**
     * 下载URL 到 StringBuilder。
     *
     * @param urlString
     * @param result    下载的结果
     * @return true: 成功
     */
    public static boolean downloadUrlToStringBuilder(String urlString, StringBuilder result) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        boolean success = downloadUrlToStream(urlString, bos);
        if (success) {
            result.append(bos.toString());
        }
        return success;
    }

    /**
     * 下载URL 到输出流 <br />
     *
     * @param urlString
     * @param outputStream
     * @return true 成功， 其它 false
     */
    public static boolean downloadUrlToStream(String urlString, OutputStream outputStream) {

        return downloadUrlToStream(urlString, outputStream, null);
    }

    /**
     * 下载URL 到输出流 <br />
     *
     * @param urlString
     * @param outputStream
     * @param dp
     * @return true 成功， 其它 false
     */
    public static boolean downloadUrlToStream(String urlString, OutputStream outputStream, DownloadProgress dp) {
        // 只要输出流不同， 完全不用考虑并发的问题.
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (dp != null) {
                dp.onGetLenth(urlConnection.getContentLength());
            }
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            byte[] buffer = new byte[1024];
            int len = -1;
            long downLength = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                downLength += len;
                if (dp != null) {
                    dp.curDownLenth(downLength);
                    ;
                }
            }
            out.flush();

            return true;
        } catch (final IOException e) {
            Log.e(TAG, "Error in download - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            CommonUtil.closeQuitely(out, in);
        }
        return false;
    }

    /**
     * 下载进度回调
     *
     * @author tcshare.org
     */
    public interface DownloadProgress {
        /**
         * 获取到内容长度
         *
         * @param contentLenth
         */
        void onGetLenth(long contentLenth);

        /**
         * 当前下载的长度
         *
         * @param downLenth
         */
        void curDownLenth(long downLenth);
    }
}
