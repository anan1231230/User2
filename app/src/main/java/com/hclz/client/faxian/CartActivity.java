package com.hclz.client.faxian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.faxian.adapter.CartAdapter;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.faxian.dialog.CartDialog;
import com.hclz.client.login.LoginActivity;
import com.hclz.client.order.confirmorder.ConfirmOrder2Activity;
import com.hclz.client.shouye.ShouyeFragment;
import com.hclz.client.shouye.adapter.ProductAdapter;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.newcart.DiandiCartItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends BaseAppCompatActivity {

    private RecyclerView mRecyclerView;
    private RelativeLayout cartLayout;
    private TextView mTotlePrice, mSubmitTv, tv_empty;
    private LinearLayout llEmpty;
    private CartDialog mDialog;

    private RecyclerView rv_cainixihuan;
    private ProductAdapter mCainiXihuanAdapter;
    private GridLayoutManager mCainixihuanManager;

    private LinearLayout ll_haimaile;

    private ArrayList<ProductBean.ProductsBean> mData;

    public static void startMe(Context from) {
        Intent intent = new Intent(from, CartActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showContent();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cart);
    }

    @Override
    protected void initView() {
        llEmpty = (LinearLayout) findViewById(R.id.ll_empty);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_empty.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        cartLayout = (RelativeLayout) findViewById(R.id.cart_layout);
        mTotlePrice = (TextView) findViewById(R.id.cart_totle_price_tv);
        mSubmitTv = (TextView) findViewById(R.id.btn_submit);
        rv_cainixihuan = (RecyclerView) findViewById(R.id.rv_cainixihuan);
        ll_haimaile = (LinearLayout) findViewById(R.id.ll_haimaile);
        String user_type = SharedPreferencesUtil.get(mContext, "user_type");
    }

    private void showContent() {
        if (DiandiCart.getInstance().get().isEmpty()) {
            cartLayout.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            ll_haimaile.setVisibility(View.GONE);
            rv_cainixihuan.setVisibility(View.GONE);
            return;
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            cartLayout.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
        mTotlePrice.setText("￥" + CommonUtil.getMoney(DiandiCart.getInstance().getTotlePrice()));
    }

    @Override
    protected void initInstance() {
        setCommonTitle("购物袋");
        configMap = HclzApplication.getData();
        mDialog = new CartDialog(mContext, new CartDialog.CartListener() {
            @Override
            public void onConfirm(DiandiCartItem item, int num, int position) {
                if (num < item.minimal_quantity) {
                    num = 0;
                } else if (num > item.inventory) {
                    num = (item.inventory - item.minimal_quantity) / item.minimal_plus * item.minimal_plus + item.minimal_quantity;
                } else {
                    num = (num - item.minimal_quantity) / item.minimal_plus * item.minimal_plus + item.minimal_quantity;
                }
                item.num = num;
                mAdapter.notifyDataSetChanged();
                showContent();
                ToastUtil.showToastCenter(mContext, "已根据最低起订量和最低增量对数量进行了修正");
            }
        });

        getScreenSize();

        mCainixihuanManager = new GridLayoutManager(mContext, 2);
        mCainiXihuanAdapter = new ProductAdapter(mContext, new ProductAdapter.ProductListener() {
            @Override
            public void onClick(ProductBean.ProductsBean productsBean) {
                ProductDetailActivity.startMe(mContext, productsBean);
            }

            @Override
            public void addCart(ProductBean.ProductsBean productsBean) {
                mAdapter.notifyDataSetChanged();
                showContent();
            }
        }, mScreenWidth, mScreenHeight);

        rv_cainixihuan.setLayoutManager(mCainixihuanManager);
        rv_cainixihuan.setAdapter(mCainiXihuanAdapter);

    }


    CartAdapter mAdapter;

    @Override
    protected void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new CartAdapter(mContext, new CartAdapter.OnNumberChangeListener() {
            @Override
            public void numberChanger() {
                showContent();
            }

            @Override
            public void onEdited(View v, DiandiCartItem item, int position) {
                mDialog.setItem(item, position);
                mDialog.showAsDropDown(v);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        showContent();
        if (!DiandiCart.getInstance().isEmpty()) {
            getRelatedProducts();
        } else {
            ll_haimaile.setVisibility(View.GONE);
        }
    }

    private void getRelatedProducts() {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            String mid = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID);
            String sessionid = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID);
            if (!TextUtils.isEmpty(mid)) {
                contentObj.put(ProjectConstant.APP_USER_MID, mid);
            }
            if (!TextUtils.isEmpty(sessionid)) {
                contentObj.put(ProjectConstant.APP_USER_SESSIONID, sessionid);
            }
            String zujiString = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_ZUJIS);
            ArrayList<String> pids = new ArrayList<>();
            if (!TextUtils.isEmpty(zujiString)) {
                ArrayList<DiandiCartItem> zujis = (ArrayList<DiandiCartItem>) DiandiCart.getInstance().get();
                if (zujis != null && zujis.size() > 0) {
                    for (int i = 0; i < (zujis.size() > 3 ? 3 : zujis.size() - 1); i++) {
                        pids.add(zujis.get(i).pid);
                    }
                    contentObj.put("pids", pids);
                }
            }
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.setIsShowDialog(false);
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.PRODUCTS_RECOMMENDS.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mData = JsonUtility.fromJson(obj.get("products"), new TypeToken<ArrayList<ProductBean.ProductsBean>>() {
                            });
                            mCainiXihuanAdapter.setData(mData);
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initListener() {
        mSubmitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mid = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID);
                if (DiandiCart.getInstance().getTotlePrice() <= 0) {
                    ToastUtil.showToast(mContext, "购物车为空，请先购买东西~");
                    return;
                }
                if (!TextUtils.isEmpty(mid)) {//判断是否登录
                    if (!DiandiCart.getInstance().isEmpty()) {
                        if ("buser".equals(SharedPreferencesUtil.get(mContext, "user_type")) || "euser".equals(SharedPreferencesUtil.get(mContext, "user_type"))) {
                            if (DiandiCart.getInstance().getTotlePrice() < 50000) {
                                ToastUtil.showToast(mContext, "商户下单需要超过500元~");
                            } else {
                                ConfirmOrder2Activity.startMe(mContext, 1);
                            }
                        } else {
                            ConfirmOrder2Activity.startMe(mContext, 1);
                        }
                    } else {
                        ToastUtil.showToast(mContext, getString(R.string.select_product));
                    }
                } else {
                    LoginActivity.startMe(mContext);
                }
            }
        });

        tv_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShouyeFragment.startMe(mContext);
            }
        });
    }

    private int mScreenWidth, mScreenHeight;

    private void getScreenSize() {
        Display d = mContext.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
    }
}
