package com.hclz.client;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.bean.Position;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.HclzConstant;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.StartAlarmReceiver;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.util.TxtToSpeackUtil;
import com.hclz.client.base.ver.VersionUtils;
import com.hclz.client.shouye.ShouyeFragment;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.faxian.FaxianFragment;
import com.hclz.client.me.MeFragment;
import com.hclz.client.order.OrderFragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity {

    public static final int CURRENT_HUODONG = 0;
    public static final int CURRENT_HCLZ = 1;
    public static final int CURRENT_HWJP = 2;
    public static final int CURRENT_ORDER = 3;
    public static final int CURRENT_ME = 4;
    private int mCurrentFragment = CURRENT_HCLZ;

    public static final int PERMISSION_REQUEST_CODE = 99;

    LinearLayout llNetWorkErr;
    RelativeLayout rlContent;
    TextView btnReLoad;
    private Bundle mBundle;
    private String toFragment;
    //fengyi.hua add for jump to rdoMall with tid [END]
    private RadioGroup rdoGrpTabMenu;
    private OnIntentChangedListener intentChangedListener;
    //fengyi.hua add for jump to rdoMall with tid [START]
    public RadioButton rdoHaiwai, rdoMall, rdoOrder, rdoMe;
    private boolean isExit = false;
    //创建Handler对象，用来处理消息
    WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            isExit = false;
            return true;
        }
    });

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
        Intent intent = new Intent(from, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addCart();
        checkUpdate();
        checkIfFirstUse();
        getScreenSize();
        toggleFragment(ShouyeFragment.class);
        startAlarm();
        TxtToSpeackUtil.getInstence().loadMedia(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_main);
    }

    private void addCart() {
        String userPhone = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_PHONE);
        if (!TextUtils.isEmpty(userPhone)) {
            String cartJson = SharedPreferencesUtil.get(mContext, userPhone);
            if (!TextUtils.isEmpty(cartJson)) {
                DiandiCart.getInstance().loadCart(cartJson, mContext);
            }
        }
    }

    private void startAlarm() {
        String user_type = SharedPreferencesUtil.get(mContext, "user_type");
        if ("cshop".equals(user_type)) {
            StartAlarmReceiver.getInstence(mContext).startAlarmReceiver();
        } else {
            StartAlarmReceiver.getInstence(mContext).stopAlarmReceiver();
        }
    }

    public int getCurrentVisibleFragment() {
        return mCurrentFragment;
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
        }
    }

    public String getVerName() {
        String verName = "";
        verName = VersionUtils.getVerName(mContext);
        return verName;
    }

    private void getScreenSize() {
        Display d = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        SanmiConfig.screen_width = size.x;
        SanmiConfig.screen_height = size.y;
    }


    private void checkIfFirstUse() {
        int intAppStartUpTimes;
        String strAppStartUpTimes = SharedPreferencesUtil.get(this, ProjectConstant.APP_START_UP_TIMES);
        if (CommonUtil.isNull(strAppStartUpTimes)) {
            intAppStartUpTimes = 0;
        } else {
            intAppStartUpTimes = Integer.valueOf(strAppStartUpTimes);
        }
        // 判定项目启动次数
        if (intAppStartUpTimes <= 0) {
            SharedPreferencesUtil.save(mContext, ProjectConstant.APP_START_UP_TIMES, "1");
            // 跳转到引导页
            GuideActivity.startMe(mContext);
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        this.initData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//	    super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        rdoGrpTabMenu = (RadioGroup) this.findViewById(R.id.rdoGrp_tab_menu);

        //fengyi.hua add for jump to rdoMall with tid [START]
        rdoHaiwai = (RadioButton) this.findViewById(R.id.rdoBtn_tab_haiwai);
        rdoMall = (RadioButton) this.findViewById(R.id.rdoBtn_tab_mall);
        rdoOrder = (RadioButton) this.findViewById(R.id.rdoBtn_tab_order);
        rdoMe = (RadioButton) this.findViewById(R.id.rdoBtn_tab_me);
        //fengyi.hua add for jump to rdoMall with tid [END]

        llNetWorkErr = (LinearLayout) findViewById(R.id.network_err);
        rlContent = (RelativeLayout) findViewById(R.id.content_frame);
        btnReLoad = (TextView) findViewById(R.id.btn_reload);
        displayContentFrame();
    }

    private void displayNetWrkErrFrame() {
        llNetWorkErr.setVisibility(View.VISIBLE);
        rlContent.setVisibility(View.GONE);
    }

    private void displayContentFrame() {
        llNetWorkErr.setVisibility(View.GONE);
        rlContent.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initInstance() {
    }


    public void doNewVersionUpdate() {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.hclz.client.fileprovider",
                    new File(Environment.getExternalStorageDirectory(), SharedPreferencesUtil.get(mContext, "download_name")));

            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), SharedPreferencesUtil.get(mContext, "download_name"))),
                    "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//        mContext.startActivity(intent);
//
//        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), SharedPreferencesUtil.get(mContext, "download_name")))
//                , "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    protected void initData() {
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            toFragment = mBundle.getString("fragment");
            if (toFragment != null) {
                if (toFragment.equals("OrderFragment")) {//选择支付界面跳转过来
                    rdoOrder.setChecked(true);
                    mCurrentFragment = CURRENT_ORDER;
                    toggleFragment(OrderFragment.class);
                } else if (toFragment.equals("ShouyeFragment")){
                    rdoMall.setChecked(true);
                    mCurrentFragment = CURRENT_HCLZ;
                }
            }
        }
    }

    @Override
    protected void initListener() {
        rdoGrpTabMenu.setOnCheckedChangeListener(new OnTabListener());
    }

    /**
     * 切换Fragment
     * <p>
     * 切换Fragment
     * </p>
     *
     * @return 无
     */
    public void toggleFragment(final Class<? extends Fragment> currentFragment) {
        if (!PostHttpUtil.isnetWorkAvilable(mContext)) {
            displayNetWrkErrFrame();
            SanmiConfig.isFirstLoad = false;
            btnReLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleFragment(currentFragment);
                }
            });
        } else {
            displayContentFrame();
            FragmentManager manager = getSupportFragmentManager();
            String strCurrentFragmentName = currentFragment.getName();
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment fragment = manager.findFragmentByTag(strCurrentFragmentName);

            if (fragment == null) {
                try {
                    fragment = currentFragment.newInstance();
                    // 替换时保留Fragment,以便复用
                    transaction.add(R.id.content_frame, fragment,
                            strCurrentFragmentName);
                } catch (Exception e) {
                    // ignore
                }
            }
            // 遍历存在的Fragment,隐藏其他Fragment
            List<Fragment> fragments = manager.getFragments();
            if (fragments != null) {
                for (Fragment fm : fragments) {
                    if (!fm.equals(fragment)) {
                        transaction.hide(fm);
                    }
                }
            }
            long startms = System.currentTimeMillis();
            transaction.show(fragment);
            transaction.commit();
        }
    }

    //fengyi.hua add for jump to rdoMall with tid [START]
//    public void turnToShopingFragment(String tid) {
//        Class<? extends Fragment> currentFragment = MallFragment.class;
//        rdoPaihang.setChecked(true);
//        FragmentManager manager = getSupportFragmentManager();
//        String strCurrentFragmentName = currentFragment.getName();
//        FragmentTransaction transaction = manager.beginTransaction();
//        Fragment fragment = manager.findFragmentByTag(strCurrentFragmentName);
//        MallFragment mallFragment;
//
//        if (fragment == null) {
//            try {
//                fragment = currentFragment.newInstance();
//                // 替换时保留Fragment,以便复用
//                transaction.add(R.id.content_frame, fragment,
//                        strCurrentFragmentName);
//            } catch (Exception e) {
//                // ignore
//            }
//            mallFragment = (MallFragment) fragment;
//            mallFragment.setTid(tid);
//        } else {
//            mallFragment = (MallFragment) fragment;
//            mallFragment.scrollToTid(tid);
//        }
//        // 遍历存在的Fragment,隐藏其他Fragment
//        List<Fragment> fragments = manager.getFragments();
//        if (fragments != null) {
//            for (Fragment fm : fragments) {
//                if (!fm.equals(fragment)) {
//                    transaction.hide(fm);
//                }
//            }
//        }
////        transaction.setCustomAnimations(R.anim.fragment_in_right, R.anim.fragment_out_left);
//        transaction.show(fragment);
//        transaction.commit();
//    }
    //fengyi.hua add for jump to rdoMall with tid [END]

    public void setOnIntentChangedListener(
            OnIntentChangedListener intentChangedListener) {
        this.intentChangedListener = intentChangedListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 当keyCode等于退出事件值时
            ToQuitTheApp();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //封装ToQuitTheApp方法
    private void ToQuitTheApp() {
        if (isExit) {
            // ACTION_MAIN with category CATEGORY_HOME 启动主屏幕
            HclzConstant.getInstance().isNeedRefresh = true;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);// 使虚拟机停止运行并退出程序
        } else {
            isExit = true;
            ToastUtil.showToast(mContext, getString(R.string.exit_tips));
            mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后发送消息
        }
    }


    /**
     * 定义一个接口
     *
     * @author zqy
     */
    public interface OnIntentChangedListener {
        /**
         * 里面传个值
         */
        public void onChanged(Position position);
    }

    /**
     * 类 名:OnTabListener 主要功能:单选按钮 选项改变
     */
    private class OnTabListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rdoBtn_tab_mall:// 网上商城
                    toggleFragment(ShouyeFragment.class);
                    mCurrentFragment = CURRENT_HCLZ;
                    rdoMall.setChecked(true);
                    break;
                case R.id.rdoBtn_tab_haiwai:// 海外精品
                    rdoHaiwai.setChecked(true);
                    mCurrentFragment = CURRENT_HWJP;
                    toggleFragment(FaxianFragment.class);
                    break;
                case R.id.rdoBtn_tab_order:// 订单
                    rdoOrder.setChecked(true);
                    mCurrentFragment = CURRENT_ORDER;
                    toggleFragment(OrderFragment.class);
                    break;
                case R.id.rdoBtn_tab_me:// 我的
                    rdoMe.setChecked(true);
                    mCurrentFragment = CURRENT_ME;
                    toggleFragment(MeFragment.class);
                    break;
                default:
                    break;
            }
        }
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
