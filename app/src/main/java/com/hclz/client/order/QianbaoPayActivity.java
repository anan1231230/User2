package com.hclz.client.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.gridpasswordview.GridPasswordView;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.MD5;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.kitchen.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by handsome on 16/5/13.
 */
public class QianbaoPayActivity extends BaseAppCompatActivity {

    private GridPasswordView pswView;
    private TextView tv_tishi;
    private ImageView iv_comm_head_left;
    private int zhifu_amount;
    private String transactionid;
    private String passwd;
    private User.AssetsEntity assetsEntity;

    /**
     * 页面跳转
     *
     * @param from
     * @param transactionid
     * @param zhifu_amount 支付金额
     */
    public static void startMe(Context from,String transactionid,String zhifu_amount){
        Intent intent = new Intent(from, QianbaoPayActivity.class);
        intent.putExtra("transactionid",transactionid);
        intent.putExtra("zhifu_amount",zhifu_amount);
        from.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_qianbaopay);
    }

    @Override
    protected void initView() {

        pswView = (GridPasswordView) findViewById(R.id.pswView);
        tv_tishi = (TextView) findViewById(R.id.tv_tishi);
        iv_comm_head_left = (ImageView) findViewById(R.id.iv_comm_head_left);
        setCommonTitle("钱包支付");
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        mIntent = mContext.getIntent();
        if (mIntent == null) {
            ToastUtil.showToast(mContext, "参数出错,请重新提交");
            finish();
        } else {
            transactionid = mIntent.getStringExtra("transactionid");
            zhifu_amount = mIntent.getIntExtra("zhifu_amount", 0);
            if (TextUtils.isEmpty(transactionid) || zhifu_amount == 0) {
                ToastUtil.showToast(mContext, "参数出错,请重新提交");
                finish();
            } else {
                getQianbaoInfo();
            }
        }
    }

    @Override
    protected void initListener() {
        iv_comm_head_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setTitle("你是否要取消支付?")
                        .setPositiveButton("取消支付", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //跳转到钱包密码界面
                                turnToOrderFragment();
                            }
                        }).setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        });
        pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (psw.length() == 6) {
                    passwd = psw;
                    zhifu();
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
    }

    private void getQianbaoInfo() {
        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
//            contentObj.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            contentObj.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
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
                            if (assetsEntity != null && assetsEntity.getAssetsettings() != null) {
                                if (assetsEntity.getAssetsettings().is_freeze()) {
                                    tv_tishi.setText("钱包账户被冻结");
                                    pswView.setEnabled(false);
                                } else if (assetsEntity.getAssetsettings().getPasswd_error_count() > 0 && assetsEntity.getAssetsettings().getPasswd_error_count_remain() > 0) {
                                    tv_tishi.setText("您还有" + assetsEntity.getAssetsettings().getPasswd_error_count_remain() + "次机会!");
                                    pswView.setEnabled(true);
                                } else {
                                    pswView.setEnabled(true);
                                }

                            }
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

    private void zhifu() {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
//            contentObj.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            contentObj.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));

            JSONObject bill = new JSONObject();
            bill.put("zhifutype", "balance");
            bill.put("transactionid", transactionid);
            bill.put("zhifu_amount", zhifu_amount);

            contentObj.put("bill", bill);
            contentObj.put("zhifu_passwd", MD5.GetMD5Code(passwd));
            contentObj.put("zhifu_target", "海外精品");

            JSONObject optional = new JSONObject();
            optional.put("zhifu_mid", SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            optional.put("billtype", "order");
            contentObj.put("optional", optional);

            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.ASSETS_USER_ZHIFU.getAssetsMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ToastUtil.showToast(mContext, "支付成功");
                            turnToOrderFragment();
                        }


                        @Override
                        protected void callBackForGetDataFailed(String result) {
                            pswView.clearPassword();
                            getQianbaoInfo();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
