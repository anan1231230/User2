package com.merben.wangluodianhua.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.merben.wangluodianhua.NetPhone;
import com.merben.wangluodianhua.R;
import com.merben.wangluodianhua.constant.NetphoneConstant;
import com.merben.wangluodianhua.fragment.ContactFragment;
import com.merben.wangluodianhua.fragment.DialPadFragment;
import com.merben.wangluodianhua.handler.WeakHandler;
import com.merben.wangluodianhua.util.SharedPreferencesUtil;
import com.merben.wangluodianhua.util.ToastUtil;
import com.yingtexun.entity.QueryBalanceEntity;
import com.yingtexun.utils.YingTeXunInfo;

public class NetPhoneActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ImageView iv_back;
    private TextView tv_yue, tv_chongzhi;
    private WeakHandler mHandler;
    private WeakHandler mRegisterHandler;

    public static void startMe(Context from){
        from.startActivity(new Intent(from, NetPhoneActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_phone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_yue = (TextView) findViewById(R.id.tv_yue);
        tv_chongzhi = (TextView) findViewById(R.id.tv_chongzhi);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_chongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NetPhoneActivity.this, ChargeActivity.class);
                startActivity(intent);
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        initHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTelData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mRegisterHandler.removeCallbacksAndMessages(null);
    }

    private void getTelData() {
        new Thread(new Runnable() {
            public void run() {
                String passwd = SharedPreferencesUtil.get(NetPhoneActivity.this, NetphoneConstant.ACCOUNT_INFO_PASSWD);
                if (!TextUtils.isEmpty(passwd)) {
                    QueryBalanceEntity entity = YingTeXunInfo.queryBalance(YingTeXunInfo.qureyBalanceUrl, YingTeXunInfo.uid, passwd);
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("entity", entity);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } else {
                    //未注册
                    NetPhone.mListener.onNeedRegister(mRegisterHandler);
                }
            }
        }).start();
    }

    private void initHandler() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                QueryBalanceEntity entity = (QueryBalanceEntity) msg.getData().getSerializable("entity");
                if (entity != null) {
                    if ("0".equals(entity.result)) {
                        //成功
                    } else if ("-1".equals(entity.result)) {
                        ToastUtil.showToast(NetPhoneActivity.this, "数据查询失败,请稍后再试!");
                    } else if ("-2".equals(entity.result)) {
                        ToastUtil.showToast(NetPhoneActivity.this, "参数错误,请联系客服人员!");
                    } else if ("-6".equals(entity.result)) {
                        ToastUtil.showToast(NetPhoneActivity.this, "Sign错误,请联系客服人员!");
                    } else if ("-8".equals(entity.result)) {
                        ToastUtil.showToast(NetPhoneActivity.this, "账户已过有效期!");
                    } else if ("-9".equals(entity.result)) {
                        ToastUtil.showToast(NetPhoneActivity.this, "账户余额不足,请及时充值");
                    } else if ("-10".equals(entity.result)) {
                        ToastUtil.showToast(NetPhoneActivity.this, "账户已被冻结,请联系客服人员!");
                    } else if ("-11".equals(entity.result)) {
                        ToastUtil.showToast(NetPhoneActivity.this, "后台程序错误,请联系客服人员!");
                    } else {
                        ToastUtil.showToast(NetPhoneActivity.this, "通讯错误,请稍后再试");
                    }
                    tv_yue.setText("余额：¥" + ((TextUtils.isEmpty(entity.balance)) ? "0" : entity.balance));
                } else {
                    tv_yue.setText("余额：¥" + 0);
                }
                return true;
            }
        });
        mRegisterHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        refresh();
                        break;
                    case 1:
//                        ToastUtil.showToast(NetPhoneActivity.this, "绑定号码失败，请重新注册!");
                        break;
                    default:
//                        ToastUtil.showToast(NetPhoneActivity.this, "绑定号码失败，请重新注册!");
                        break;
                }
                return true;
            }
        });
    }

    private void refresh() {
        getTelData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_net_phone, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DialPadFragment.newInstance();
                case 1:
                    return ContactFragment.newInstance();
                default:
                    return DialPadFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "拨号键盘";
                case 1:
                    return "通讯录";
            }
            return null;
        }
    }
}
