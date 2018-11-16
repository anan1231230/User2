package com.hclz.client.base.ver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.ver.SimpleDownloader.DownloadProgress;
import com.hclz.client.base.view.VerDialog;
import com.hclz.client.base.view.VerDialog.OnNewVersionButtonClickListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**********************************************************************
 * 程序名： ApkVerManager.java 功能描述：用于程序的apk版本管理，实现客户端程序的更新与安装等。 程序版本：1.0
 **********************************************************************/
public class ApkVerManager {
    /**
     * TAG
     */
    private static final String TAG = ApkVerManager.class.getSimpleName();
    private static final int notifyId = 102;
    // 首次登陆 0 其他 1
    public int loading = 0;
    private String verNm = "";

    // 下载相关
    private String serverVerNm = "";
    private String serverVerUrl = "";
    private Context context;
    private NotificationManager mNotificationManager;
    private Builder mBuilder;
    private UpdateTask updateTask;

    public ApkVerManager(Context context, int loadingparam) {
        this.context = context;
        this.loading = loadingparam;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setWhen(System.currentTimeMillis())
                .setContentIntent(getDefalutIntent(context, 0))
                .setOngoing(false)
                .setSmallIcon(R.mipmap.ic_launcher);
        updateTask = new UpdateTask();
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    private PendingIntent getDefalutIntent(Context ctx, int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 1, new Intent(), flags);
        return pendingIntent;
    }

    /**
     * 取得当前程序版本号。
     *
     * @return 当前程序版本号
     */
    private void getCurrentVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            setVerNm(packageInfo.versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 后台线程，向后台服务器发送版本检验信息
     */
    public void checkVersionsInfo() {
        //获取本地app版本号
        getCurrentVersion();
        //获取服务器版本号
        serverVerNm = HclzApplication.getData().get("app_version");
        serverVerUrl = HclzApplication.getData().get("download_url");
//        if (this.verNm.equals(serverVerNm)) {//版本号相同
        if (!CommonUtil.isVersionBigger(verNm,serverVerNm)){
            ToastUtil.showToast(context, "当前已经是最新版本");
        } else {//版本号不同
            String[] releaseNotes = JsonUtility.fromJson(HclzApplication.getData().get("release_notes"), new TypeToken<String[]>() {
            });
            VerDialog dialog = new VerDialog(context, serverVerNm, releaseNotes);
            dialog.setmListener(new OnNewVersionButtonClickListener() {
                @Override
                public void onInstallClick() {
//					if(TextUtils.isEmpty(serverVerUrl)){
//						serverVerUrl = "http://gdown.baidu.com/data/wisegame/d43eb443632a158e/anzhishichang_6010.apk";
//					}
                    if (!TextUtils.isEmpty(serverVerUrl)) {
                        startUpdate();
                    }
                }

                @Override
                public void onIgnoreClick() {

                }
            });
            dialog.showDialog();
        }
    }

    /**
     * 开始更新
     */
    public void startUpdate() {
        updateTask.execute("");
    }

    /**
     * 任务已经完成
     *
     * @return
     */
    public boolean isFinished() {
        return updateTask.getStatus() == AsyncTask.Status.FINISHED;
    }

    public String getVerNm() {
        return verNm;
    }

    public void setVerNm(String verNm) {
        this.verNm = verNm;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 升级任务
     *
     * @author tcshare.org
     */
    private class UpdateTask extends AsyncTask<String, Integer, Integer> {
        private final String apk = context.getExternalCacheDir() + File.separator + "hclz.apk";
        private int maxLength = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBuilder.setContentTitle("检测新版本").setContentText("进度:").setTicker("开始下载");
            mNotificationManager.notify(notifyId, mBuilder.build());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                mBuilder.setContentTitle("APK下载中...")
                        .setContentText(String.format("进度: %.2f %%", Float.valueOf(values[0]) / maxLength * 100))
                        .setProgress(maxLength, values[0], false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mNotificationManager.notify(notifyId, mBuilder.build());
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 1) {
                mBuilder.setAutoCancel(true)// 点击后让通知将消失
                        .setProgress(0, 0, false)
                        .setContentTitle("下载完成")
                        .setContentText("点击安装")
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setTicker("下载完成！");
                // 打开安装包
                Intent apkIntent = new Intent();
                apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                apkIntent.setAction(android.content.Intent.ACTION_VIEW);

                Uri uri = Uri.fromFile(new File(apk));
                apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                PendingIntent contextIntent = PendingIntent.getActivity(context, 0, apkIntent, 0);
                mBuilder.setContentIntent(contextIntent);
            } else if (result == -1) {
                mBuilder.setAutoCancel(true)// 点击后让通知将消失
                        .setProgress(0, 0, false)
                        .setContentTitle("下载失败！")
                        .setContentText("下载失败！")
                        .setTicker("下载失败！");
            } else if (result == 0) {
                mBuilder.setAutoCancel(true)
                        .setProgress(0, 0, false)
                        .setContentTitle("升级完成！")
                        .setContentText("当前已经为最新版本！")
                        .setTicker("升级完成！");
                Toast.makeText(context, "当前已经为最新版本！", Toast.LENGTH_SHORT).show();
            }
            mNotificationManager.notify(notifyId, mBuilder.build());
        }

        @Override
        protected Integer doInBackground(String... params) {
            String url = serverVerUrl;
            if (url != null) {
                OutputStream os = null;
                try {
                    os = new FileOutputStream(apk);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return -1;
                }
                boolean downSuccess = SimpleDownloader.downloadUrlToStream(url, os, new DownloadProgress() {
                    @Override
                    public void onGetLenth(long contentLenth) {
                        maxLength = Long.valueOf(contentLenth).intValue();
                    }

                    @Override
                    public void curDownLenth(long downLenth) {
                        publishProgress(Long.valueOf(downLenth).intValue());
                    }
                });
                return downSuccess ? 1 : -1;
            }
            return 0;
        }
    }
}
