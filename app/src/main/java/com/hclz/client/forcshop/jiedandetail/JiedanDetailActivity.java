package com.hclz.client.forcshop.jiedandetail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.forcshop.jiedandetail.adapter.ProductInOrderAdapter;
import com.hclz.client.forcshop.jiedanguanli.adapter.JiedanGuanliAdapter;
import com.hclz.client.forcshop.jiedanguanli.bean.JiedanguanliBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JiedanDetailActivity extends BaseAppCompatActivity {

    TextView tv_title_name,tv_title_num,tv_time,tv_phone,tv_address,tv_money,tv_confirm,tv_quehuo;
    ImageView iv_contact_phone,iv_contact_map;
    RecyclerView rv_products;

    LinearLayoutManager mManager;
    ProductInOrderAdapter mAdapter;

    JiedanguanliBean mItem;

    String mStatus;

    public static void startMe(Context from, String status, JiedanguanliBean item){
        Intent intent = new Intent(from, JiedanDetailActivity.class);
        intent.putExtra("status",status);
        intent.putExtra("item",item);
        from.startActivity(intent);
    }

    public static void startMe(Context from, JiedanguanliBean item){
        Intent intent = new Intent(from, JiedanDetailActivity.class);
        intent.putExtra("item",item);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_cshop_order_detail);
    }

    @Override
    protected void initView() {
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        tv_title_num = (TextView) findViewById(R.id.tv_title_num);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_confirm = (TextView)findViewById(R.id.tv_confirm);
        tv_quehuo = (TextView) findViewById(R.id.tv_quehuo);
        iv_contact_map = (ImageView) findViewById(R.id.iv_contact_map);
        iv_contact_phone = (ImageView) findViewById(R.id.iv_contact_phone);
        rv_products = (RecyclerView) findViewById(R.id.rv_products);
    }

    @Override
    protected void initInstance() {
        mManager = new LinearLayoutManager(mContext);
        mAdapter = new ProductInOrderAdapter(mContext);
        rv_products.setLayoutManager(mManager);
        rv_products.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mIntent = mContext.getIntent();
        if (mIntent != null){
            mItem = (JiedanguanliBean) mIntent.getSerializableExtra("item");
            mStatus = mIntent.getStringExtra("status");
        }

        if (mItem != null){
            showContent();
        }
    }

    private void showContent(){
        setCommonTitle(mItem.getOrderId());
        tv_time.setText("下单时间:"+mItem.ct);
        tv_phone.setText("联系电话:"+mItem.getPhone());
        tv_address.setText("送货地址:"+(mItem.address.address==null?"":mItem.address.address) + (mItem.address.detail==null?"":(","+mItem.address.detail)));
        tv_money.setText("消费金额:¥"+CommonUtil.getMoney(mItem.getPrice()));
        ArrayList<ProductInOrderAdapter.ProductInOrderItem> items = new ArrayList<>();
        for (JiedanguanliBean.ProductsEntity product: mItem.products){
            items.add(product);
        }

        if ("finished".equals(mStatus)){
            tv_confirm.setVisibility(View.GONE);
            tv_quehuo.setVisibility(View.GONE);
        }else if ("chengdaiweisong".equals(mStatus)) {
            tv_confirm.setVisibility(View.GONE);
            tv_quehuo.setVisibility(View.GONE);
        } else {
            switch (mItem.getStatus()){
                case 20:
                case 21:
                    tv_confirm.setText("接单");
                    tv_quehuo.setVisibility(View.VISIBLE);
                    break;
                case 41:
                case 49:
                    tv_confirm.setText("接货");
                    tv_quehuo.setVisibility(View.GONE);
                    break;
                case 50:
                    tv_confirm.setText("送货");
                    tv_quehuo.setVisibility(View.GONE);
                    break;
                case 51:
                    tv_confirm.setText("送达");
                    tv_quehuo.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
        mAdapter.setData(items);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void initListener() {
        iv_contact_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItem == null || mItem.getPhone() == null && mItem.getPhone().length() < 11) {
                    ToastUtil.showToast(mContext,"电话号码不正确");
                    return;
                }
                Uri uri = Uri.parse("tel:" + mItem.getPhone());
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                PackageManager pm = getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission("android.permission.CALL_PHONE", "com.hclz.client"));
                if (permission) {
                    mContext.startActivity(intent);
                }else {
                    ToastUtil.showToast(mContext,"当前您的系统设置此应用没有拨打电话权限!请设置允许此应用可拨打电话!");
                }

            }
        });

        iv_contact_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mContext,"项目研发中,敬请期待!");
            }
        });

        tv_quehuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setTitle("您确认缺货吗！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                opRequest("quehuo",mItem);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext).setTitle("您确认"+ tv_confirm.getText() +"吗！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if ("接单".equals(tv_confirm.getText())){
                                    opRequest("jiedan",mItem);
                                } else if ("接货".equals(tv_confirm.getText())){
                                    opRequest("jiehuo",mItem);
                                } else if ("送货".equals(tv_confirm.getText())){
                                    opRequest("songhuo",mItem);
                                } else if ("送达".equals(tv_confirm.getText())){
                                    opRequest("songda",mItem);
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }
    private void opRequest(final String op,JiedanGuanliAdapter.JiedanItem item){

        String url = ServerUrlConstant.CSHOP_ORDER_OP_CONFIRM.getShopMethod();
        if ("jiedan".equals(op) || "jiehuo".equals(op)){
            url = ServerUrlConstant.CSHOP_ORDER_OP_CONFIRM.getShopMethod();
        } else if ("quehuo".equals(op)){
            url = ServerUrlConstant.CSHOP_ORDER_OP_TODSHOP.getShopMethod();
        } else if ("songhuo".equals(op)){
            url = ServerUrlConstant.CSHOP_ORDER_OP_SENDOUT.getShopMethod();
        } else if ("songda".equals(op)){
            url = ServerUrlConstant.CSHOP_ORDER_OP_REACHED.getShopMethod();
        }
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            content.put("cid",SharedPreferencesUtil.get(mContext,"cid"));
            content.put("orderid",item.getOrderId());
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(url, requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            tv_quehuo.setVisibility(View.GONE);
                            if ("quehuo".equals(op)){
                                finish();
                            } else {
                                if (tv_confirm.getText().equals("接货")) {
                                    tv_confirm.setText("送货");
                                } else if (tv_confirm.getText().equals("接单")) {
                                    tv_confirm.setText("送货");
                                } else if (tv_confirm.getText().equals("送货")) {
                                    tv_confirm.setText("送达");
                                } else if (tv_confirm.getText().equals("送达")) {
                                    finish();
                                }
                            }
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
