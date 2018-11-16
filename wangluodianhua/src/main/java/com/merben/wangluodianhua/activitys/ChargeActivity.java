package com.merben.wangluodianhua.activitys;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.merben.wangluodianhua.NetPhone;
import com.merben.wangluodianhua.R;
import com.merben.wangluodianhua.handler.WeakHandler;
import com.merben.wangluodianhua.util.ToastUtil;

public class ChargeActivity extends AppCompatActivity {
    ImageView iv_back;
    EditText et_account, et_passwd;
    TextView tv_chongzhi;
    WeakHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_account = (EditText) findViewById(R.id.et_account);
        et_passwd = (EditText) findViewById(R.id.et_passwd);
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
                chongzhi();
            }
        });

        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bundle bundle = msg.getData();
                        String result = bundle.getString("result");
                        if (!TextUtils.isEmpty(result)) {
                            if ("0".equals(result)) {
                                ToastUtil.showToast(ChargeActivity.this, "提交成功正在处理中，请在2分钟以后查询余额！");
                                finish();
                            } else if ("1".equals(result)) {
                                ToastUtil.showToast(ChargeActivity.this, "充值卡无效或已被使用!");
                            } else if ("3".equals(result)) {
                                ToastUtil.showToast(ChargeActivity.this, "充值失败");
                            } else if ("-2".equals(result)) {
                                ToastUtil.showToast(ChargeActivity.this, "充值卡无效或已被使用!");
                            } else if ("-3".equals(result)) {
                                ToastUtil.showToast(ChargeActivity.this, "充值帐号错误！");
                            } else if ("-4".equals(result)) {
                                ToastUtil.showToast(ChargeActivity.this, "充值卡已过期!");
                            } else if ("-5".equals(result)) {
                                ToastUtil.showToast(ChargeActivity.this, "充值金额错误！");
                            } else if ("-6".equals(result)) {
                                ToastUtil.showToast(ChargeActivity.this, "充值卡号格式错误！");
                            } else if ("-7".equals(result)) {
                                ToastUtil.showToast(ChargeActivity.this, "充值密码格式错误！");
                            } else if ("-9".equals(result)) {
                                ToastUtil.showToast(ChargeActivity.this, "充值卡号或者密码错误！");
                            } else {
                                ToastUtil.showToast(ChargeActivity.this, "通讯故障,请稍后再试");
                            }
                        }
                        break;
                    case 1:
                        ToastUtil.showToast(ChargeActivity.this, "通讯故障,请稍后再试");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void chongzhi() {

        if (TextUtils.isEmpty(et_account.getText().toString()) || TextUtils.isEmpty(et_passwd.getText().toString())) {
            ToastUtil.showToast(this, "请填写正确的充值卡账号和密码!");
            return;
        }

        NetPhone.charge(this, et_account.getText().toString(), et_passwd.getText().toString(), mHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(this);
    }
}
