package com.hclz.client.faxian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.faxian.bean.Product;
import com.hclz.client.faxian.products.AddressIns;
import com.hclz.client.login.LoginActivity;
import com.hclz.client.me.MyAddressActivity;
import com.hclz.client.order.confirmorder.ConfirmOrder2Activity;
import com.hclz.client.order.confirmorder.bean.address.NetAddress;
import com.hclz.client.order.confirmorder.bean.hehuoren.NetHehuoren;
import com.hclz.client.shouye.adapter.ProductAdapter;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.faxian.bean.ProductBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ProductListWithPaixuActivity extends BaseAppCompatActivity {

    private TextView mTitleTv, mEmptyTv;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mManager;
    private ProductAdapter mAdapter;
    private String tag, cateId;
    private ImageView mBackIv, mCartIv;
    private ProductBean mProductBean;
    private ArrayList<ProductBean.ProductsBean> mProductszhengxu;
    private ArrayList<ProductBean.ProductsBean> mProductsdaoxu;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TabLayout mTabLayout;
    private int type = 0;//0发现列表,带tab;1首页icon列表,不带tab,2搜索列表,不带tab
    private ImageView mDorIv;
    private ImageView iv_search;
    private TextView tv_cart;



    private TextView tv_address,tv_change_address;

    public static void startMe(Context from, String name, String cateId, int type) {
        Intent intent = new Intent(from, ProductListWithPaixuActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("cateId", cateId);
        intent.putExtra("type", type);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mAdapter) {
            mAdapter.startRefreshTime();
        }
        showNumber();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mAdapter) {
            mAdapter.cancelRefreshTime();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mAdapter) {
            mAdapter.cancelRefreshTime();
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product);
    }

    @Override
    protected void initView() {
        mEmptyTv = (TextView) findViewById(R.id.empty_tv);
        mTitleTv = (TextView) findViewById(R.id.product_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mBackIv = (ImageView) findViewById(R.id.iv_comm_head_left);
        mCartIv = (ImageView) findViewById(R.id.iv_comm_head_right);
        mDorIv = (ImageView) findViewById(R.id.notification_dot_iv);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_change_address = (TextView) findViewById(R.id.tv_change_address);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        tv_cart = (TextView) findViewById(R.id.tv_cart);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main, R.color.yellow, R.color.main,
                R.color.yellow);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText("人气排序"));
        mTabLayout.addTab(mTabLayout.newTab().setText("价格排序⇡"));
        mTabLayout.addTab(mTabLayout.newTab().setText("价格排序⇣"));

        mManager = new GridLayoutManager(mContext, 2);
        ProductAdapter.ProductListener mListener = new ProductAdapter.ProductListener() {
            @Override
            public void onClick(ProductBean.ProductsBean productsBean) {
                ProductDetailActivity.startMe(mContext, productsBean);
            }

            @Override
            public void addCart(ProductBean.ProductsBean productsBean) {
                showNumber();
            }
        };
        getScreenSize();
        mAdapter = new ProductAdapter(mContext, mListener,mScreenWidth,mScreenHeight);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.isHeader(position) ? mManager.getSpanCount() : 1;
            }
        });
    }

    @Override
    protected void initInstance() {
        configMap = HclzApplication.getData();
        mIntent = mContext.getIntent();
        if (mIntent != null) {
            tag = mIntent.getStringExtra("name");
            cateId = mIntent.getStringExtra("cateId");
            type = mIntent.getIntExtra("type", 0);
        }
        mTitleTv.setText(tag == null ? "好吃懒做" : tag);
        if (type == 0) {
            mTabLayout.setVisibility(View.VISIBLE);
        } else {
            mTabLayout.setVisibility(View.GONE);
        }
    }

    private void getProducts() {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            if (AddressIns.getInstance().getmAddress() != null){
                contentObj.put("addressid",AddressIns.getInstance().getmAddress().getAddressId());
            }
            if (type == 0) {
                contentObj.put("cateid", cateId);
            } else if (type == 1) {
                JSONArray array = new JSONArray();
                array.put(cateId);
                contentObj.put("tags", array);
            } else {
                JSONArray array = new JSONArray();
                array.put(cateId);
                contentObj.put("keywords", array);
            }
            String mid = SharedPreferencesUtil.get(mContext,
                    ProjectConstant.APP_USER_MID);
            String sessionid = SharedPreferencesUtil.get(mContext,
                    ProjectConstant.APP_USER_SESSIONID);
            if (!TextUtils.isEmpty(mid)) {
                contentObj.put(ProjectConstant.APP_USER_MID, mid);
            }
            if (!TextUtils.isEmpty(sessionid)) {
                contentObj.put(ProjectConstant.APP_USER_SESSIONID, sessionid);
            }
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.setIsShowDialog(false);
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.PRODUCT_LIST.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            mProductBean = JsonUtility.fromJson(result, ProductBean.class);
                            AddressIns.getInstance().setAddress(mProductBean.address);
                            mProductszhengxu = new ArrayList<ProductBean.ProductsBean>();
                            mProductsdaoxu = new ArrayList<ProductBean.ProductsBean>();
                            for (ProductBean.ProductsBean bean : mProductBean.products) {
                                mProductszhengxu.add(bean);
                            }
                            Collections.sort(mProductszhengxu);
                            for (ProductBean.ProductsBean bean : mProductszhengxu) {
                                mProductsdaoxu.add(0, bean);
                            }
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    private int mScreenWidth,mScreenHeight;
    private void getScreenSize() {
        Display d = mContext.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
    }


    private void showNumber() {
        if (mDorIv == null) {
            return;
        }
        if (!DiandiCart.getInstance().isEmpty()) {
            mDorIv.setVisibility(View.VISIBLE);
        } else {
            mDorIv.setVisibility(View.GONE);
        }
    }

    private void showContent() {

        showNumber();

        if (mProductBean.products.isEmpty()) {
            mEmptyTv.setVisibility(View.VISIBLE);
        } else {
            // 校对倒计时
            long curTime = System.currentTimeMillis();
            for (ProductBean.ProductsBean itemInfo : mProductBean.products) {

                if (itemInfo.limit_price != null){

                    if (itemInfo.limit_price.start_time > itemInfo.limit_price.current_time) {
                        itemInfo.limit_price.count_down = itemInfo.limit_price.start_time - itemInfo.limit_price.current_time;
                        itemInfo.limit_price.isStarted = false;
                    } else if (itemInfo.limit_price.end_time > itemInfo.limit_price.current_time) {
                        itemInfo.limit_price.count_down = itemInfo.limit_price.end_time - itemInfo.limit_price.current_time;
                        itemInfo.limit_price.isStarted = true;
                    } else {
                        itemInfo.limit_price = null;
                    }
                }
                if (itemInfo.limit_price != null) {
                    itemInfo.limit_price.end_time_local = curTime + itemInfo.limit_price.count_down;
                }
            }

            mEmptyTv.setVisibility(View.GONE);
            mAdapter.setData(mProductBean.products);
        }

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        showAddress();
    }

    private void showAddress(){
        if (AddressIns.getInstance().getmAddress() != null){
            tv_address.setText(AddressIns.getInstance().getmAddress().addr_detail);
        }
    }

    @Override
    protected void initData() {
        getProducts();
    }

    @Override
    protected void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCartIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.startMe(mContext);
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ("价格排序⇣".equals(tab.getText())){
                    mAdapter.setData(mProductsdaoxu);
                } else if ("价格排序⇡".equals(tab.getText())){
                    mAdapter.setData(mProductszhengxu);
                } else {
                    mAdapter.setData(mProductBean.products);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tv_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SharedPreferencesUtil
                        .get(mContext, ProjectConstant.APP_USER_MID))) {
                    LoginActivity.startMe(mContext);
                } else {
                    Intent intent = new Intent(ProductListWithPaixuActivity.this,
                            MyAddressActivity.class);
                    intent.putExtra("from", ProductListWithPaixuActivity.class.getName());
                    startActivityForResult(intent, 100);
                }
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductSearchActivity.startMe(mContext);
            }
        });
        tv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.startMe(mContext);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            if (AddressIns.getInstance().getmAddress() != null){
                tv_address.setText(AddressIns.getInstance().getmAddress().addr_detail);
                getProducts();
            } else {
                tv_address.setText("潍坊市奎文区");
            }
        }
    }
}
