package com.merben.wangluodianhua.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.merben.wangluodianhua.NetPhone;
import com.merben.wangluodianhua.R;
import com.merben.wangluodianhua.handler.WeakHandler;
import com.merben.wangluodianhua.util.ContactBean;
import com.merben.wangluodianhua.util.ToastUtil;

public class CallActivity extends AppCompatActivity {

    private TextView tv_name_suoxie, tv_name, tv_phone, tv_zhuangtai, tv_quxiao;
    private ContactBean mContact;
    private WeakHandler mHandler;
    private WeakHandler mCallHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //0:动画效果,1,开始拨打电话,2,界面消失
                case 0:
                    String zhuangtai = tv_zhuangtai.getText().toString();
                    if (zhuangtai.equals(".")) {
                        tv_zhuangtai.setText("..");
                    } else if (zhuangtai.equals("..")) {
                        tv_zhuangtai.setText("...");
                    } else if (zhuangtai.equals("...")) {
                        tv_zhuangtai.setText(".");
                    } else {
                        tv_zhuangtai.setText(".");
                    }
                    mCallHandler.sendEmptyMessageDelayed(0, 500);
                    break;
                case 1:
                    call();
                    mCallHandler.sendEmptyMessageDelayed(2, 2000);
                    break;
                case 2:
                    finish();
                    break;
            }
            return true;
        }
    });
    private WeakHandler mRegisterHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        tv_name_suoxie = (TextView) findViewById(R.id.tv_name_suoxie);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_zhuangtai = (TextView) findViewById(R.id.tv_zhuangtai);
        tv_quxiao = (TextView) findViewById(R.id.tv_quxiao);
        initHandler();
        Intent intent = getIntent();
        if (intent == null) {
            ToastUtil.showToast(this, "号码错误!");
            finish();
        } else {
            mContact = (ContactBean) intent.getSerializableExtra("contact");
            if (mContact == null || TextUtils.isEmpty(mContact.getPhoneNum())) {
                ToastUtil.showToast(this, "号码错误!");
                finish();
            } else {
                startCall();
            }
        }
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mCallHandler.removeCallbacksAndMessages(null);
        mRegisterHandler.removeCallbacksAndMessages(null);
    }

    private void initHandler() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what != 101) {
                    if (NetPhone.mListener != null) {
                        NetPhone.mListener.onNeedRegister(mRegisterHandler);
                    }
                }
                switch (msg.what) {
                    case -11:
                    case -12:
                    case -13:
                        ToastUtil.showToast(CallActivity.this, "系统错误,请稍后重试!");
                        break;
                    case -10:
                        ToastUtil.showToast(CallActivity.this, "账号被冻结，请联系客服!");
                        break;
                    case -9:
                        ToastUtil.showToast(CallActivity.this, "余额不足，请充值!");
                        break;
                    case -8:
                        ToastUtil.showToast(CallActivity.this, "账号过期，请充值激活!");
                    case -7:
                    default:
                        break;
                    case -6:
                        ToastUtil.showToast(CallActivity.this, "签名错误!");
                        break;
                    case -5:
                        ToastUtil.showToast(CallActivity.this, "号码被冻结，请半小时后在试!");
                        break;
                    case -4:
//                        ToastUtil.showToast(CallActivity.this, "绑定号码失败，请重新注册!");
                        if (NetPhone.mListener != null) {
                            NetPhone.mListener.onNeedRegister(mRegisterHandler);
                        }
                        break;
                    case -3:
                        ToastUtil.showToast(CallActivity.this, "呼叫号码错误!");
                        break;
                    case -2:
                        ToastUtil.showToast(CallActivity.this, "密码错误，请重新登录!");
                        break;
                    case -1:
                        ToastUtil.showToast(CallActivity.this, "回拨失败，请重新登录!");
                        break;
                    case 101:
                        ToastUtil.showToast(CallActivity.this, "回拨成功!请等待来电!");
                        break;
                    case 102:
                        ToastUtil.showToast(CallActivity.this, "回拨失败，请重新拨打!");
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
                        ToastUtil.showToast(CallActivity.this, "绑定号码失败，请重新注册!");
                        break;
                    default:
                        ToastUtil.showToast(CallActivity.this, "绑定号码失败，请重新注册!");
                        break;
                }
                return true;
            }
        });
    }

    private void refresh() {
        mCallHandler.sendEmptyMessageDelayed(1, 5000);
    }

    private void startCall() {
        String name = TextUtils.isEmpty(mContact.getDesplayName()) ? "未知" : mContact.getDesplayName();
        String phone = mContact.getPhoneNum();

        tv_name.setText(name);
        tv_phone.setText(phone);
        tv_name_suoxie.setText(name.substring(0, 1));
        mCallHandler.sendEmptyMessageDelayed(0, 500);
        mCallHandler.sendEmptyMessageDelayed(1, 5000);
    }

    private void call() {
        NetPhone.call(this, mHandler, mContact.getPhoneNum());
    }

}
