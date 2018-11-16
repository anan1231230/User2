package com.hclz.client.base.util;

import android.content.Context;

import com.hclz.client.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * 在app文件目录中读取缓存文件
 *
 * @author 作者     ：
 * @version 版本号 ：
 *          功能介绍    ：
 */
public class CacheFile {

    private Context mContext;

    public CacheFile(Context context) {
        this.mContext = context;
    }

    public void writeCache(String filename, String content) {
        try {
            // 以追加模式打开文件输出流
            FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            // 将FileOutputStream包装成PrintStream
            PrintStream ps = new PrintStream(fos);
            // 输出文件内容
            ps.println(content);
            ps.close();
        } catch (Exception e) {
            ToastUtil.showToast(mContext, mContext.getString(R.string.operation_failed));
        }
    }

    public String readCache(String filename) {
        try {
            // 打开文件输入流
            FileInputStream fis = mContext.openFileInput(filename);
            byte[] buff = new byte[1024];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder("");
            while ((hasRead = fis.read(buff)) > 0) {
                sb.append(new String(buff, 0, hasRead));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
