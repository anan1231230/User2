package com.hclz.client.order;        /**
 * Created by handsome on 2016/12/16.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.Cshop;
import com.hclz.client.base.bean.Order;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.view.CircleImageView;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.faxian.CartActivity;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.order.adapter.OrderProductAdapter;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.newcart.DiandiCartItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private CircleImageView iv_pic;
    private TextView tv_arrive_time;
    private TextView tv_status;
    private TextView tv_quxiao;
    private TextView tv_caozuo;
    private RecyclerView rv_products;
    private TextView tv_peisongfei;
    private TextView tv_youhui;
    private TextView tv_shifu;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_address;
    private TextView tv_order_id;
    private TextView tv_pay_type;
    private TextView tv_order_time;
    private LinearLayout ll_mainpanel;

    private LinearLayoutManager mManager;
    private OrderProductAdapter mAdapter;

    private Order mOrder;
    private Cshop mCshop;

    private List<ProductBean.ProductsBean> plist=new ArrayList<>();

    public static void startMe(Activity context, Order order) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("order", order);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void initView() {
        setCommonTitle("订单详情");
        iv_pic = (CircleImageView) findViewById(R.id.iv_pic);
        tv_arrive_time = (TextView) findViewById(R.id.tv_arrive_time);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_quxiao = (TextView) findViewById(R.id.tv_quxiao);
        tv_caozuo = (TextView) findViewById(R.id.tv_caozuo);
        rv_products = (RecyclerView) findViewById(R.id.rv_products);
        tv_peisongfei = (TextView) findViewById(R.id.tv_peisongfei);
        tv_youhui = (TextView) findViewById(R.id.tv_youhui);
        tv_shifu = (TextView) findViewById(R.id.tv_shifu);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_order_id = (TextView) findViewById(R.id.tv_order_id);
        tv_pay_type = (TextView) findViewById(R.id.tv_pay_type);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        ll_mainpanel = (LinearLayout) findViewById(R.id.ll_mainpanel);
    }

    @Override
    protected void initInstance() {
        mManager = new LinearLayoutManager(mContext);
        mAdapter = new OrderProductAdapter(mContext);
        rv_products.setLayoutManager(mManager);
        rv_products.setAdapter(mAdapter);
        configMap = HclzApplication.getData();
    }

    @Override
    protected void initData() {
        if (mIntent != null) {
            mOrder = (Order) mIntent.getSerializableExtra("order");
            if (mOrder != null) {
                WaitingDialogControll.showLoadingDialog(mContext);
                getCshopInfo();
            }
        }
    }

    private void showContent() {

        mAdapter.setData((ArrayList<Order.ProductsBean>) mOrder.products);
        if (mCshop != null && mCshop.album_thumbnail != null && mCshop.album_thumbnail.size() > 0){
            ImageUtility.getInstance(mContext).showImage(mCshop.album_thumbnail.get(0), iv_pic);
        }
        tv_arrive_time.setText(mOrder.ct);
        tv_status.setText(mOrder.status_detail.get(mOrder.status_detail.size() - 1).desc);
        tv_peisongfei.setText("￥" + CommonUtil.getMoney(mOrder.freight_amount));
        tv_youhui.setText("-￥" + CommonUtil.getMoney(0));
        tv_shifu.setText("实付￥" + CommonUtil.getMoney(mOrder.payment_amount));
        tv_name.setText(mOrder.address.receiver);
        tv_phone.setText(mOrder.address.receiver_phone);
        tv_address.setText(mOrder.address.addr_detail);
        tv_order_id.setText(mOrder.orderid);
        tv_pay_type.setText("线上支付");
        tv_order_time.setText(mOrder.ct);

        if (mOrder.operation != null) {
            if (mOrder.operation.size() == 1) {
                tv_quxiao.setVisibility(View.GONE);
                tv_caozuo.setVisibility(View.VISIBLE);
                tv_caozuo.setText(mOrder.operation.get(0).display);
            } else if (mOrder.operation.size() == 2) {
                tv_quxiao.setVisibility(View.VISIBLE);
                tv_caozuo.setVisibility(View.VISIBLE);
                tv_quxiao.setText(mOrder.operation.get(0).display);
                tv_caozuo.setText(mOrder.operation.get(1).display);
            }
        } else {
            tv_quxiao.setVisibility(View.GONE);
            tv_caozuo.setVisibility(View.GONE);
        }
        dialogHandler.sendEmptyMessageDelayed(1000,8000);
    }

    WeakHandler dialogHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            WaitingDialogControll.dismissLoadingDialog();
            return true;
        }
    });

    @Override
    protected void initListener() {
        tv_status.setOnClickListener(this);
        tv_caozuo.setOnClickListener(this);
        tv_quxiao.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String op = "";
        switch (view.getId()) {
            case R.id.tv_status:
                WuliuListActivity.startMe(mContext,mOrder);
                break;
            case R.id.tv_caozuo:
                op = mOrder.operation.get(mOrder.operation.size() - 1).op;
                if (!TextUtils.isEmpty(op)){
                    onOp(op,tv_caozuo.getText().toString());
                }
                break;
            case R.id.tv_quxiao:
                op = mOrder.operation.get(0).op;
                if (!TextUtils.isEmpty(op)) {
                    onOp(op, tv_quxiao.getText().toString());
                }
                break;
        }
    }
    private void onOp(final String op,String show){
        if ("删除订单".equals(show)){
            new AlertDialog.Builder(mContext).setTitle("您确定要删除订单吗?")
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            op(op);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //DO NOTHING
                }
            }).show();
        } else if ("取消订单".equals(show)) {
            new AlertDialog.Builder(mContext).setTitle("您确定要取消订单吗?")
                    .setPositiveButton("取消订单", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            op(op);
                        }
                    }).setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //DO NOTHING
                }
            }).show();
        } else if ("去支付".equals(show)) {
            SelectPayMethodActivity.startMe(mContext,mOrder.orderid,mOrder.payment_amount,mOrder.mid);
        }else if("再来一单".equals(show)){
            anotherOrder();
        } else {
            op(op);
        }
    }
    private void anotherOrder(){
        JSONArray jsonArray = new JSONArray();

        for(int i=0;i<mOrder.products.size();i++){
            jsonArray.put(mOrder.products.get(i).pid);
        }


        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            content.put("pids",jsonArray);
            content.put("addressid", "51303096-147650285251");
            PostHttpUtil.prepareParams(requestParams, content.toString());

            sanmiAsyncTask.setIsShowDialog(false);
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.ORDER_ANOTHER.getOrderMethod(),
                    requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerFailed(String result) {
                            super.callBackForServerFailed(result);

                        }

                        @Override
                        protected void callBackForGetDataFailed(
                                String result) {
                            super.callBackForGetDataFailed(result);
                        }

                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            //staffes = JsonUtility.fromJson(obj.get("staff"), new TypeToken<List<NetStaff>>() {
                            plist=  JsonUtility.fromJson(obj.get("products"),new TypeToken<List<ProductBean.ProductsBean>>(){});

                            add(plist);

                        }

                        @Override
                        public boolean callBackSessionError() {
                            return false;
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private void add(List<ProductBean.ProductsBean> olist){
        int count=0;

        for(int i=0;i<olist.size();i++){
            Integer num = 0;
            if (DiandiCart.getInstance().contains(olist.get(i).pid)) {//如果购物车中有此类商品，num等于购物车数量
                DiandiCartItem cartItem = DiandiCart.getInstance().get(olist.get(i).pid);
                num = cartItem.num;
            }
            if(olist.get(i).inventory>0){
                if(olist.get(i).inventory>mOrder.products.get(i).deal_count){
                    num+=mOrder.products.get(i).deal_count;
                }
            }
//            Log.d("测试+",olist.get(i).name+num);
            DiandiCartItem newItem = new DiandiCartItem(olist.get(i), num);
            count+=num;
            DiandiCart.getInstance().put(newItem, mContext);
        }
        if(count>0){
            CartActivity.startMe(mContext);
        }else{
            ToastUtil.showToast(mContext,getResources().getString(R.string.product_num_empty));
        }


    }

    private void op(String op) {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            contentObj.put("orderid", mOrder.orderid);
            contentObj.put("op", op);
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.setIsShowDialog(true);
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.ORDER_STATUS_OP.getOrderMethod(),
                    requestParams, new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ToastUtil.showToast(mContext, "操作成功!");
                            getOrderInfo();//刷新单个订单信息
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getOrderInfo() {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            contentObj.put("orderid", mOrder.orderid);
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.setIsShowDialog(true);
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.ORDER_GET.getOrderMethod(),
                    requestParams, new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mOrder = JsonUtility.fromJson(obj.get("order"),Order.class);
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCshopInfo() {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            contentObj.put("orderid", mOrder.orderid);
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.setIsShowDialog(false);
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.CSHOP_GET.getOrderMethod(),
                    requestParams, new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mCshop = JsonUtility.fromJson(obj.get("cshop"),Cshop.class);
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}