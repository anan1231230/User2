package com.hclz.client.me;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCPay;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;

public class SelectPayMethod3Activity extends BaseActivity {
    private final String TAG = "SelectPayMethodActivity";
    private ImageView ivCommHeadLeft;
    private TextView tvOrderPrice;
    private RadioGroup rgPayType;
    private TextView tvOk;

    private ProgressDialog loadingDialog;
    BCCallback bcCallback = new BCCallback() {
        @Override
        public void done(BCResult bcResult) {
            final BCPayResult bcPayResult = (BCPayResult) bcResult;
            //此处关闭loading界面
            loadingDialog.dismiss();

            //根据你自己的需求处理支付结果
            //需要注意的是，此处如果涉及到UI的更新，请在UI主进程或者Handler操作
            SelectPayMethod3Activity.this.runOnUiThread(new Runnable() {
                @SuppressLint("StringFormatMatches")
                @Override
                public void run() {
                    String result = bcPayResult.getResult();
                    /*
                          注意！
                      	所有支付渠道建议以服务端的状态金额为准，此处返回的RESULT_SUCCESS仅仅代表手机端支付成功
                    */
                    if (result.equals(BCPayResult.RESULT_SUCCESS)) {
                        ToastUtil.showToast(mContext, getString(R.string.pay_success));
                        turnToOrderFragment();
                    } else if (result.equals(BCPayResult.RESULT_CANCEL)) {
                        ToastUtil.showToast(mContext, getString(R.string.pay_cancle));
                        turnToOrderFragment();
                    } else if (result.equals(BCPayResult.RESULT_FAIL)) {
                        ToastUtil.showToast(mContext, getString(R.string.pay_fail, bcPayResult.getErrMsg(), bcPayResult.getDetailInfo()));
                        turnToOrderFragment();
                    } else if (result.equals(BCPayResult.RESULT_UNKNOWN)) {
                        //可能出现在支付宝8000返回状态
                        ToastUtil.showToast(mContext, getString(R.string.order_status_unknown));
                        turnToOrderFragment();
                    } else {
                        ToastUtil.showToast(mContext, getString(R.string.pay_other_error));
                        turnToOrderFragment();
                    }

                    if (bcPayResult.getId() != null) {
                        //你可以把这个id存到你的订单中，下次直接通过这个id查询订单
                        Log.w(TAG, "bill id retrieved : " + bcPayResult.getId());
                        //根据ID查询，详细请查看demo
//                      getBillInfoByID(bcPayResult.getId());
                    }
                }
            });
        }
    };
    private String billId;
    private int paymentAmout;
    private String zhifuMid;

    private int payType = 0;//0默认支付宝支付1微信支付

    /**
     * 页面跳转
     *
     * @param from
     * @param billId
     * @param payment_amount 支付金额
     * @param zhifu_mid
     */
    public static void startMe(Context from ,String billId ,int payment_amount,String zhifu_mid){
        Intent intent = new Intent(from, SelectPayMethod3Activity.class);
        intent.putExtra("billId",billId);
        intent.putExtra("payment_amount",payment_amount);
        intent.putExtra("zhifu_mid",zhifu_mid);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_select_paymethod3);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        ivCommHeadLeft = (ImageView) findViewById(R.id.iv_comm_head_left);
        tvOrderPrice = (TextView) findViewById(R.id.tv_order_price);
        rgPayType = (RadioGroup) findViewById(R.id.rg_pay_type);
        tvOk = (TextView) findViewById(R.id.tv_ok);

        // 如果调起支付太慢, 可以在这里开启动画, 以progressdialog为例
        loadingDialog = new ProgressDialog(SelectPayMethod3Activity.this);
        loadingDialog.setMessage(getString(R.string.start_pay));
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
    }

    @Override
    protected void initInstance() {
        // 如果用到微信支付，在用到微信支付的Activity的onCreate函数里调用以下函数.
        // 第二个参数需要换成你自己的微信AppID.   wx2831fae42eb879be
        BCPay.initWechatPay(SelectPayMethod3Activity.this, getString(R.string.wx_app_id));
    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.select_pay_method);
        if (mIntent != null) {
            billId = mIntent.getStringExtra("billId");
            paymentAmout = mIntent.getIntExtra("payment_amount", 0);
            zhifuMid = mIntent.getStringExtra("zhifu_mid");
            tvOrderPrice.setText("¥" + CommonUtil.getMoney(paymentAmout));
        }
    }

    @Override
    protected void initListener() {
        ivCommHeadLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToOrderFragment();
            }
        });
        rgPayType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_zhifubao:
                        payType = 0;
                        break;
                    case R.id.rb_weixin:
                        payType = 1;
                        break;
                    default:
                        break;
                }
            }
        });
        tvOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				paymentAmout = 1;
                if (payType == 0) {//支付宝
                    loadingDialog.show();
                    Map<String, String> mapOptional = new HashMap<String, String>();
                    mapOptional.put("zhifu_mid", zhifuMid);
                    mapOptional.put("billtype", "buycard");
                    BCPay.getInstance(SelectPayMethod3Activity.this).reqAliPaymentAsync(
                            "Android支付宝支付-好吃懒做",
                            (int) paymentAmout,
                            billId,
                            mapOptional,
                            bcCallback);
                } else {//微信
                    loadingDialog.show();
                    //对于微信支付, 手机内存太小会有OutOfResourcesException造成的卡顿, 以致无法完成支付
                    //这个是微信自身存在的问题
                    Map<String, String> mapOptional = new HashMap<String, String>();
                    mapOptional.put("zhifu_mid", zhifuMid);
                    mapOptional.put("billtype", "buycard");
                    if (BCPay.isWXAppInstalledAndSupported() &&
                            BCPay.isWXPaySupported()) {
                        BCPay.getInstance(SelectPayMethod3Activity.this).reqWXPaymentAsync(
                                "Android微信支付-好吃懒做",               //订单标题
                                (int) paymentAmout,                            //订单金额(分)
                                billId,                      //订单流水号
                                mapOptional,                  //扩展参数(可以null)
                                bcCallback);                  //支付完成后回调入口
                    }
                }
            }
        });
    }

    private void turnToOrderFragment() {
        QianbaoZhangdanActivity.startMe(mContext);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            turnToOrderFragment();
        }

        //return true 表示后续不再监听back
        return true;
    }
}
