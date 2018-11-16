package com.hclz.client.me;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.R;
import com.hclz.client.base.cart.Cart;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.HclzConstant;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.ver.ApkVerManager;
import com.hclz.client.base.ver.VersionUtils;
import com.hclz.client.login.ForgetPwdActivity;
import com.hclz.client.shouye.newcart.DiandiCart;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hclz.client.MainActivity.PERMISSION_REQUEST_CODE;

public class SettingActivity extends BaseActivity implements OnClickListener {

    private TextView tvChangePwd, tvCheckUpdate, tvClearHuancun, tvAboutUs, tvFeedback, tvVersion, tv_tuijianren;
    private Button btnExit;

    //下载用
    private String mNewVersion;
    private String mMinVersion;
    private String mDownloadUrl;
    private String mReleaseNotes;
    WeakHandler mUpdateHandler;
    private ProgressDialog mProgressDialog;
    private static int BUFFER_SIZE = 8096;//缓冲区大小

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from) {
        Intent intent = new Intent(from, SettingActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeTuijianrenButtonStyle();
    }

    private void changeTuijianrenButtonStyle() {
        String introducer = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_INTRODUCER);
        if (!TextUtils.isEmpty(introducer) && !"\"\"".equals(introducer)) {
            tv_tuijianren.setText("推荐人: " + introducer);
            tv_tuijianren.setClickable(false);
        } else {
            tv_tuijianren.setText("补填注册推荐人");
            tv_tuijianren.setClickable(true);
        }
    }

    @Override
    protected void initView() {
        tvChangePwd = (TextView) findViewById(R.id.tv_change_pwd);
        tvCheckUpdate = (TextView) findViewById(R.id.tv_check_update);
        tvClearHuancun = (TextView) findViewById(R.id.tv_clear_huancun);
        tvAboutUs = (TextView) findViewById(R.id.tv_about_us);
        tvFeedback = (TextView) findViewById(R.id.tv_feedback);
        btnExit = (Button) findViewById(R.id.btn_exit);
        tvVersion = (TextView) findViewById(R.id.setting_version);
        tv_tuijianren = (TextView) findViewById(R.id.tv_tuijianren);
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.setting);
        tvVersion.setText("当前版本:" + VersionUtils.getVerName(mContext));
    }

    @Override
    protected void initListener() {
        tvChangePwd.setOnClickListener(this);
        tvCheckUpdate.setOnClickListener(this);
        tvClearHuancun.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        tv_tuijianren.setOnClickListener(this);

        changeTuijianrenButtonStyle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_pwd:
                ForgetPwdActivity.startMe(mContext);
                break;
            case R.id.tv_check_update:
                checkUpdate();
                break;
            case R.id.tv_clear_huancun:
                ImageUtility.getInstance(mContext).clearCache();
                ToastUtil.showToast(mContext, "缓存清理完毕~");
                break;
            case R.id.tv_about_us:
                AboutUsActivity.startMe(mContext);
                break;
            case R.id.tv_feedback:
                FeedBackActivity.startMe(mContext);
                break;
            case R.id.tv_tuijianren:
                ChageTuijianrenPhoneActivity.startMe(mContext);
                break;
            case R.id.btn_exit:
                HclzConstant.getInstance().isNeedRefresh = true;
                String previous_user_type = SharedPreferencesUtil.get(mContext, "user_type");
                SharedPreferencesUtil.save(mContext, "user_type", "normal");

                if (previous_user_type.equals("dshop")) {
                    ToastUtil.showToast(mContext, "之前为特殊用户,现在已经退出登录,商品价格已变,请重新添加购物车~");
                    SanmiConfig.isMallNeedRefresh = true;
                    SanmiConfig.isHaiwaiNeedRefresh = true;

                    SanmiConfig.isOrderNeedRefresh = true;
                    DiandiCart.getInstance().clear(mContext);

                } else if (previous_user_type.equals("buser")||previous_user_type.equals("euser")) {
                    ToastUtil.showToast(mContext, "之前为特殊用户,现在已经退出登录,商品价格已变,请重新添加购物车~");
                    SanmiConfig.isMallNeedRefresh = true;
                    SanmiConfig.isHaiwaiNeedRefresh = true;
                    SanmiConfig.isOrderNeedRefresh = true;
                    DiandiCart.getInstance().clear(mContext);
                }else if (previous_user_type.equals("euser")) {
                    ToastUtil.showToast(mContext, "之前为特殊用户,现在已经退出登录,商品价格已变,请重新添加购物车~");
                    SanmiConfig.isMallNeedRefresh = true;
                    SanmiConfig.isHaiwaiNeedRefresh = true;
                    SanmiConfig.isOrderNeedRefresh = true;
                    DiandiCart.getInstance().clear(mContext);
                } else if (previous_user_type.equals("cshop")) {
                    ToastUtil.showToast(mContext, "之前为特殊用户,现在已经退出登录,商品价格已变,请重新添加购物车~");
                    SanmiConfig.isMallNeedRefresh = true;
                    SanmiConfig.isHaiwaiNeedRefresh = true;

                    SanmiConfig.isOrderNeedRefresh = true;
                    DiandiCart.getInstance().clear(mContext);

                }
                DiandiCart.getInstance().clear(mContext);
                //清空mainaccount、subaccount信息
                SharedPreferencesUtil.save(mContext, ProjectConstant.APP_USER_MID, "");
                SharedPreferencesUtil.save(mContext, ProjectConstant.APP_USER_SESSIONID, "");
                SharedPreferencesUtil.save(mContext, ProjectConstant.APP_USER_INTRODUCER, "");
                SharedPreferencesUtil.save(mContext, ProjectConstant.APP_USER_PHONE, "");
                MeFragment.startMe(mContext);
                finish();
                break;
            default:
                break;
        }
    }

    private void checkUpdate() {
        mUpdateHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Android_6_Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                        doNewVersionUpdate();
                        break;
                    case 1:
                        mProgressDialog.cancel();
                        update();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mNewVersion = HclzApplication.getData().get("app_version");
        mMinVersion = HclzApplication.getData().get("app_min_version");
        mDownloadUrl = HclzApplication.getData().get("download_url");

        String previousVer = getVerName();
        if (mNewVersion != null && !mNewVersion.equals("") && previousVer != null && !previousVer.equals("") && !mNewVersion.equals(previousVer)) {
            SharedPreferencesUtil.save(mContext, "download_name", "hclz" + System.currentTimeMillis() + ".apk");
            mUpdateHandler.sendEmptyMessage(0);
        } else {
            ToastUtil.showToast(mContext,"当前版本已经是最新~");
        }
    }

    public String getVerName() {
        String verName = "";
        try {
            verName = getPackageManager().getPackageInfo("com.hclz.client", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("版本名称获取异常", e.getMessage());
        }
        return verName;
    }

    public void doNewVersionUpdate() {
        int count = 0;
        if (TextUtils.isEmpty(mDownloadUrl)) return;
        mProgressDialog = new ProgressDialog(this);
        String verName = this.getVerName();
        if (CommonUtil.isVersionBigger(verName, mNewVersion)) {
            if (CommonUtil.isVersionBiggerOrEqual(verName, mMinVersion)) {
                StringBuffer sb = new StringBuffer();
                sb.append("当前版本：");
                sb.append(verName);
                sb.append("\n发现版本：");
                sb.append(mNewVersion);
                sb.append("\n更新后才能看到更精彩的世界哦~");
                Dialog dialog = new AlertDialog.Builder(this)
                        .setTitle("软件更新")
                        .setMessage(sb.toString())
                        .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mProgressDialog.setTitle("正在下载");
                                mProgressDialog.setMessage("请稍后...");
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                downFile(mDownloadUrl);
                            }
                        })
                        .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                finish();
                            }
                        }).create();
                //显示更新框
                dialog.show();
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("当前版本：");
                sb.append(verName);
                sb.append("\n发现版本：");
                sb.append(mNewVersion);
                sb.append("\n更新后才能看到更精彩的世界哦~");
                Dialog dialog = new AlertDialog.Builder(this)
                        .setTitle("软件更新")
                        .setMessage(sb.toString())
                        .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mProgressDialog.setTitle("正在下载");
                                mProgressDialog.setMessage("请稍后...");
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                downFile(mDownloadUrl);
                            }
                        }).setCancelable(false).create();
                //显示更新框
                dialog.show();
            }
        }
    }

    public void downFile(final String destUrl) {
        mProgressDialog.show();
        new Thread() {
            public void run() {
                FileOutputStream fos = null;
                BufferedInputStream bis = null;
                HttpURLConnection httpUrl = null;
                URL url = null;
                byte[] buf = new byte[BUFFER_SIZE];
                int size = 0;
                //建立链接
                try {
                    url = new URL(destUrl);
                    httpUrl = (HttpURLConnection) url.openConnection();
                    //连接指定的资源
                    httpUrl.connect();

                    int length = httpUrl.getContentLength();

                    //获取网络输入流
                    bis = new BufferedInputStream(httpUrl.getInputStream());
                    //建立文件
                    File file = new File(Environment.getExternalStorageDirectory(), SharedPreferencesUtil.get(mContext, "download_name"));
                    if (!file.exists()) {
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        file.createNewFile();
                    }
                    fos = new FileOutputStream(file);
                    int count = 0;
                    //保存文件
                    while ((size = bis.read(buf)) != -1) {
                        fos.write(buf, 0, size);
                        count += size;
                        mProgressDialog.setProgress((int) ((double) count / (double) length * 100));
                    }

                    fos.close();
                    bis.close();
                    httpUrl.disconnect();
                    done();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void done() {
        new Thread() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                mUpdateHandler.sendMessage(message);
            }
        }.start();
    }

    public void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), SharedPreferencesUtil.get(mContext, "download_name")))
                , "application/vnd.android.package-archive");
        startActivity(intent);
    }

    public void Android_6_Permission(String permission) {
        //首先判断版本号是否大于等于6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !(mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            doNewVersionUpdate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNewVersionUpdate();
    }

}
