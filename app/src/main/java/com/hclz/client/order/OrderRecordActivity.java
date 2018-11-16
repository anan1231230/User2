package com.hclz.client.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.faxian.CartActivity;
import com.hclz.client.faxian.ProductDetailActivity;
import com.hclz.client.faxian.ProductSearchActivity;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.faxian.listener.AppBarStateChangeListener;
import com.hclz.client.faxian.products.AddressIns;
import com.hclz.client.login.LoginActivity;
import com.hclz.client.me.MyAddressActivity;
import com.hclz.client.shouye.adapter.ProductAdapter;
import com.hclz.client.shouye.newcart.DiandiCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;



public class OrderRecordActivity extends BaseAppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mManager;
    private ProductAdapter mAdapter;
    private String name, imgUrl,title;
    private TextView mTitleTv, mEmptyTv;
    private ProductBean mProductBean;
    private ImageView mBackIv, mTopIv, mCartIv;
    private AppBarLayout mAppBarLayout;
//    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mDorIv;
    private ImageView iv_search;
    private TextView tv_cart;

    private TextView tv_address,tv_change_address;

    public static void startMe(Context from, String name, String title, String imgUrl) {
        Intent intent = new Intent(from, OrderRecordActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("imgUrl", imgUrl);
        intent.putExtra("title", title);
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
        setContentView(R.layout.activity_order_record);
    }

    @Override
    protected void initView() {
        mEmptyTv = (TextView) findViewById(R.id.empty_tv);
//        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
//        mCollapsingToolbarLayout.setContentScrim(ContextCompat.getDrawable(mContext, R.mipmap.background_navagation));
//        mCollapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(mContext,R.color.red_title));
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mBackIv = (ImageView) findViewById(R.id.iv_comm_head_left);
        mTopIv = (ImageView) findViewById(R.id.product_image);
        mCartIv = (ImageView) findViewById(R.id.iv_comm_head_right);
        mDorIv = (ImageView) findViewById(R.id.notification_dot_iv);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_change_address = (TextView) findViewById(R.id.tv_change_address);
        tv_cart = (TextView) findViewById(R.id.tv_cart);
        setSupportActionBar(mToolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main, R.color.yellow, R.color.main,
                R.color.yellow);

        mManager = new GridLayoutManager(mContext, 2);
        ProductAdapter.ProductListener mListener = new ProductAdapter.ProductListener() {
            @Override
            public void onClick(ProductBean.ProductsBean productsBean) {
                ProductDetailActivity.startMe(mContext, productsBean);//跳转详情产品界面
            }

            @Override
            public void addCart(ProductBean.ProductsBean productsBean) {
                showNumber();//购物车显示有物品添加（右上角显示小点）
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
            title = mIntent.getStringExtra("title");
            name = mIntent.getStringExtra("name");
            imgUrl = mIntent.getStringExtra("imgUrl");
        }
        mTitleTv.setText(TextUtils.isEmpty("title") ? "好吃懒做" : title);
//        ImageUtility.getInstance(mContext).showImage(imgUrl, mTopIv);

    }

    private void getProducts() {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            if (AddressIns.getInstance().getmAddress() != null){
                contentObj.put("addressid",AddressIns.getInstance().getmAddress().getAddressId());
            }
            JSONArray array = new JSONArray();
            array.put(name);
            contentObj.put("tags", array);
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
                    ServerUrlConstant.ORDER_RECORD.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            mProductBean = JsonUtility.fromJson(result, ProductBean.class);
                            AddressIns.getInstance().setAddress(mProductBean.address);
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
        showAddress();

        if (mProductBean.products.isEmpty()) {
            mEmptyTv.setVisibility(View.VISIBLE);
        } else {
            // 校对倒计时
            long curTime = System.currentTimeMillis();
            for (ProductBean.ProductsBean itemInfo : mProductBean.products) {

                if (itemInfo.limit_price != null) {

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
        }

        if (mProductBean.products.isEmpty()) {
            mEmptyTv.setVisibility(View.VISIBLE);
        } else {
            mEmptyTv.setVisibility(View.GONE);
            mAdapter.setData(mProductBean.products);
            mAdapter.notifyDataSetChanged();
        }

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
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
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {//折叠状态
                    mTitleTv.setVisibility(View.VISIBLE);
                } else {
                    mTitleTv.setVisibility(View.GONE);
                }
            }
        });
        tv_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SharedPreferencesUtil
                        .get(mContext, ProjectConstant.APP_USER_MID))) {
                    LoginActivity.startMe(mContext);
                } else {
                    Intent intent = new Intent(OrderRecordActivity.this,MyAddressActivity.class);
                    intent.putExtra("from", OrderRecordActivity.class.getName());
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
