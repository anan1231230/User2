package com.hclz.client.order;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.ver.VersionUtils;
import com.hclz.client.kitchen.bean.User;
import com.hclz.client.me.LinshiChongzhiActivity;
import com.hclz.client.me.QianbaomimaSettingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCPay;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;

public class SelectPayMethodActivity extends BaseActivity {
    private final String TAG = "SelectPayMethodActivity";
    private ImageView ivCommHeadLeft;
    private TextView tvOrderPrice;
    private RadioGroup rgPayType;
    private RadioButton rb_qianbao, rb_zhifubao, rb_weixin, rb_offline;
    private TextView tvOk, tv_tishi;

    private ProgressDialog loadingDialog;
    BCCallback bcCallback = new BCCallback() {
        @Override
        public void done(BCResult bcResult) {
            final BCPayResult bcPayResult = (BCPayResult) bcResult;
            //此处关闭loading界面
            loadingDialog.dismiss();

            //根据你自己的需求处理支付结果
            //需要注意的是，此处如果涉及到UI的更新，请在UI主进程或者Handler操作
            SelectPayMethodActivity.this.runOnUiThread(new Runnable() {
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
    private String orderId;
    private int paymentAmout;
    private String zhifuMid;

    private int payType = 2;//0支付宝支付1微信支付2钱包支付(默认)
    private int mWalletPay = 1;//0 不允许钱包支付 1允许钱包支付
    private User.AssetsEntity assetsEntity;

    /**
     * 页面跳转
     *
     * @param from
     * @param orderId        订单id
     * @param payment_amount 支付金额
     * @param zhifu_mid
     */
    public static void startMe(Context from, String orderId, int payment_amount ,String zhifu_mid){
        startMe(from,orderId,1,payment_amount,zhifu_mid);
    }

    /**
     * 页面跳转
     *
     * @param from
     * @param orderId        订单id
     * @param payment_amount 支付金额
     * @param zhifu_mid
     */
    public static void startMe(Context from, String orderId,int wallet_pay, int payment_amount ,String zhifu_mid){
        Intent intent = new Intent(from, SelectPayMethodActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("payment_amount", payment_amount);
        intent.putExtra("zhifu_mid", zhifu_mid);
        intent.putExtra("wallet_pay", wallet_pay);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_select_paymethod);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getQianbaoInfo();
    }

    @Override
    protected void initView() {
        ivCommHeadLeft = (ImageView) findViewById(R.id.iv_comm_head_left);
        tvOrderPrice = (TextView) findViewById(R.id.tv_order_price);
        rgPayType = (RadioGroup) findViewById(R.id.rg_pay_type);
        rb_qianbao = (RadioButton) findViewById(R.id.rb_qianbao);
        rb_zhifubao = (RadioButton) findViewById(R.id.rb_zhifubao);
        rb_offline = (RadioButton) findViewById(R.id.rb_offline);
        rb_weixin = (RadioButton) findViewById(R.id.rb_weixin);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        tv_tishi = (TextView) findViewById(R.id.tv_tishi);
        // 如果调起支付太慢, 可以在这里开启动画, 以progressdialog为例
        loadingDialog = new ProgressDialog(SelectPayMethodActivity.this);
        loadingDialog.setMessage(getString(R.string.start_pay));
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
    }

    @Override
    protected void initInstance() {
        // 如果用到微信支付，在用到微信支付的Activity的onCreate函数里调用以下函数.
        // 第二个参数需要换成你自己的微信AppID.   wx2831fae42eb879be
        BCPay.initWechatPay(SelectPayMethodActivity.this, getString(R.string.wx_app_id));
    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.select_pay_method);
        if (mIntent != null) {
            orderId = mIntent.getStringExtra("orderId");
            paymentAmout = mIntent.getIntExtra("payment_amount", 0);
            zhifuMid = mIntent.getStringExtra("zhifu_mid");
            mWalletPay = mIntent.getIntExtra("wallet_pay", 1);
            tvOrderPrice.setText("¥" + CommonUtil.getMoney(paymentAmout));
            getQianbaoInfo();
        }
    }

    private void getQianbaoInfo() {
        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
//            contentObj.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            contentObj.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));

            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.ASSETS_USER.getAssetsMethod(),
                    requestParams, new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            assetsEntity = JsonUtility.fromJson(result, User.AssetsEntity.class);
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showContent() {
        if (mWalletPay == 0) {
            rb_qianbao.setEnabled(false);
            rb_zhifubao.setChecked(true);
            tv_tishi.setVisibility(View.VISIBLE);
            tv_tishi.setText("此次交易不能用钱包支付");
        } else if (assetsEntity != null && assetsEntity.getAssetsettings() != null) {
            if (assetsEntity.getAssetsettings().is_freeze()) {
                rb_qianbao.setEnabled(false);
                rb_zhifubao.setChecked(true);
                tv_tishi.setVisibility(View.VISIBLE);
                tv_tishi.setText("钱包账户被冻结");
            } else if (assetsEntity.getAssetsettings().getPasswd_error_count() > 0 && assetsEntity.getAssetsettings().getPasswd_error_count_remain() > 0) {
//                                    tv_tishi.setText("您还有" + assetsEntity.getAssetsettings().getPasswd_error_count_remain() +"次机会");
                tv_tishi.setVisibility(View.GONE);
                rb_qianbao.setEnabled(true);
            } else {
                tv_tishi.setVisibility(View.GONE);
                rb_qianbao.setEnabled(true);
            }
        }
        String user_type=SharedPreferencesUtil.get(mContext,"user_type");
//        if ("dshop".equals(user_type)||"cshop".equals(user_type)||"buser".equals(user_type)) {
        if ("dshop".equals(user_type)||"cshop".equals(user_type)) {
            rb_offline.setVisibility(View.VISIBLE);
        } else {
            rb_offline.setVisibility(View.GONE);
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
                    case R.id.rb_qianbao:
                        payType = 2;
                        break;
                    case R.id.rb_offline:
                        payType = 3;
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
                    BCPay.getInstance(SelectPayMethodActivity.this).reqAliPaymentAsync(
                            "Android支付宝支付-好吃懒做",
                            (int) paymentAmout,
                            orderId,
                            mapOptional,
                            bcCallback);
                } else if (payType == 1) {//微信
                    loadingDialog.show();
                    //对于微信支付, 手机内存太小会有OutOfResourcesException造成的卡顿, 以致无法完成支付
                    //这个是微信自身存在的问题
                    Map<String, String> mapOptional = new HashMap<String, String>();
                    mapOptional.put("zhifu_mid", zhifuMid);
                    if (BCPay.isWXAppInstalledAndSupported() &&
                            BCPay.isWXPaySupported()) {
                        BCPay.getInstance(SelectPayMethodActivity.this).reqWXPaymentAsync(
                                "Android微信支付-好吃懒做",               //订单标题
                                (int) paymentAmout,                            //订单金额(分)
                                orderId,                      //订单流水号
                                mapOptional,                  //扩展参数(可以null)
                                bcCallback);                  //支付完成后回调入口
                    }
                } else if (payType == 3) {//线下支付
                    offlinePay();
                } else {//好吃懒做钱包支付
                    if (assetsEntity != null && !assetsEntity.getAssetsettings().getIs_zhifu_passwd_set()) {
                        new AlertDialog.Builder(mContext).setTitle("您的支付密码尚未设定,请设置6位数字的密码！")
                                .setPositiveButton("设置支付密码", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //跳转到钱包密码界面
                                        Intent intent = new Intent(SelectPayMethodActivity.this, QianbaomimaSettingActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }).show();
                    } else if (assetsEntity != null && assetsEntity.getBasic() != null) {
                        if (assetsEntity.getBasic().getBalance() == null || assetsEntity.getBasic().getBalance().getAmount() < paymentAmout) {
                            new AlertDialog.Builder(mContext).setTitle("余额不足,请先充值！")
                                    .setPositiveButton("充值", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //跳转到钱包密码界面
                                            Intent intent = new Intent(SelectPayMethodActivity.this, LinshiChongzhiActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //DO NOTHING
                                }
                            }).show();
                        } else {
                            Intent intent = new Intent(SelectPayMethodActivity.this, QianbaoPayActivity.class);
                            intent.putExtra("transactionid", orderId);
                            intent.putExtra("zhifu_amount", paymentAmout);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        ToastUtil.showToast(mContext, "暂时不能用钱包支付,请选择其它支付方式.");
                    }
                }
            }
        });
    }


    private void offlinePay() {
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
//            contentObj.put(ProjectConstant.APPID,1);
//            contentObj.put(ProjectConstant.PLATFORM,"android");
//            contentObj.put(ProjectConstant.APP_VERSION, VersionUtils.getVerName(mContext));
            contentObj.put("orderid", orderId);
            requestParams.clear();
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.ORDER_OFFLINEPAY2.getOrderMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            turnToOrderFragment();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void turnToOrderFragment() {
        OrderFragment.startMe(mContext);
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