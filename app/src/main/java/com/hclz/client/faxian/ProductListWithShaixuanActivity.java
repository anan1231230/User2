package com.hclz.client.faxian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.hclz.client.faxian.adapter.DrawerLayoutAdapter;
import com.hclz.client.faxian.adapter.DrawerLayoutErjiAdapter;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.faxian.bean.StyleSelect;
import com.hclz.client.faxian.products.AddressIns;
import com.hclz.client.login.LoginActivity;
import com.hclz.client.me.MyAddressActivity;
import com.hclz.client.shouye.adapter.ProductAdapter;
import com.hclz.client.shouye.newcart.DiandiCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListWithShaixuanActivity extends BaseAppCompatActivity {

    private TextView mTitleTv;
    private TextView mEmptyTv;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mManager;
    private ProductAdapter mAdapter;
    private String tag, cateId;
    private ImageView mBackIv, mCartIv, mShaixuanIv;
    private ProductBean mProductBean;
    private List<ProductBean.ProductsBean> mProducts;

    private ImageView mDorIv;
    private TextView tv_cart;
    private DrawerLayout mDl_shaixuan;
    private TextView mDl_canncel, mDl_sure, mDl_clear, mDl_title;
    private ImageView twoDrawerBack_iv;
    private RecyclerView mDl_lv, mDl_two_lv;
    private LinearLayout oneLayout;
    private TextView tv_address,tv_change_address;

    private Map<String, List<String>> screenMap;
    private DrawerLayoutAdapter adapter;
    private DrawerLayoutErjiAdapter erjiAdapter;
    private TextView currentSytleTv;
    private PopupWindow popupWindow;
    private List<String> nameList = new ArrayList<>(), titleList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static void startMe(Context from, String name, String cateId) {
        Intent intent = new Intent(from, ProductListWithShaixuanActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("cateId", cateId);
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
        setContentView(R.layout.activity_product_jiaju);
    }

    @Override
    protected void initView() {
        mEmptyTv = (TextView) findViewById(R.id.empty_tv);
        mTitleTv = (TextView) findViewById(R.id.jiaju_procuct_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.jiaju_product_ryv);
        mBackIv = (ImageView) findViewById(R.id.jiaju_product_back);
        mCartIv = (ImageView) findViewById(R.id.iv_comm_head_right);
        mDorIv = (ImageView) findViewById(R.id.ification_dot_iv);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_change_address = (TextView) findViewById(R.id.tv_change_address);
        tv_cart = (TextView) findViewById(R.id.tv_cart);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main, R.color.yellow, R.color.main,
                R.color.yellow);

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

        //筛选
        mShaixuanIv = (ImageView) findViewById(R.id.iv_shaixuan);

        mDl_shaixuan = (DrawerLayout) findViewById(R.id.drawer_layout_one);
        mDl_shaixuan.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDl_shaixuan.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                mDl_shaixuan.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                nameList.clear();
                titleList.clear();
                mDl_shaixuan.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });

        mDl_canncel = (TextView) findViewById(R.id.drawer_canncel);
        mDl_sure = (TextView) findViewById(R.id.drawer_sure);
        mDl_clear = (TextView) findViewById(R.id.drawer_clear);
        mDl_lv = (RecyclerView) findViewById(R.id.drawer_lv);
        mDl_lv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DrawerLayoutAdapter(mContext);
        mDl_lv.setAdapter(adapter);

        oneLayout = (LinearLayout) findViewById(R.id.drawer_one);

    }

    @Override
    protected void initInstance() {
        configMap = HclzApplication.getData();
        mIntent = mContext.getIntent();
        if (mIntent != null) {
            tag = mIntent.getStringExtra("name");
            cateId = mIntent.getStringExtra("cateId");
        }
        mTitleTv.setText(tag == null ? "好吃懒做" : tag);
    }

    private void getProducts() {

        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            if (AddressIns.getInstance().getmAddress() != null){
                contentObj.put("addressid",AddressIns.getInstance().getmAddress().getAddressId());
            }
            contentObj.put("cateid", cateId);
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
                            if (mProductBean != null && mProductBean.products != null && mProductBean.products.size() > 0) {
                                screen();
                            } else {
                                mProductBean = new ProductBean();
                                mProductBean.products = new ArrayList<ProductBean.ProductsBean>();
                            }
                            mProducts = mProductBean.products;

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

        if (mProducts.isEmpty()) {
            mEmptyTv.setVisibility(View.VISIBLE);
        } else {
            mEmptyTv.setVisibility(View.GONE);
            mAdapter.setData(mProducts);
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
        mShaixuanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDl_shaixuan.isDrawerOpen(oneLayout)) {
                    mDl_shaixuan.closeDrawers();
                } else {
                    List<String> screen = new ArrayList<String>();
                    for (String type : screenMap.keySet()) {
                        screen.add(type);
                    }
                    adapter.setDatas(screen);
                    mDl_shaixuan.openDrawer(oneLayout);
                }
            }
        });
        mDl_canncel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDl_shaixuan.closeDrawer(oneLayout);
            }
        });
        mDl_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < nameList.size(); i++) {
                    StyleSelect.getInstence().setTrueMap(titleList.get(i), nameList.get(i));
                }
                Map<String, String> mapSelectScreen = StyleSelect.getInstence().getSelectScreen();
                mProducts = getProductsUsingScreenFromProducts(mapSelectScreen, (ArrayList<ProductBean.ProductsBean>) mProductBean.products);
                showContent();

                mDl_shaixuan.closeDrawer(oneLayout);
            }
        });
        mDl_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                StyleSelect.getInstence().initMap(screenMap);
            }
        });
        adapter.setOnItemClickListener(new DrawerLayoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                currentSytleTv = (TextView) ((RelativeLayout) v).getChildAt(1);
                openTwoDrawer(((TextView) ((RelativeLayout) v).getChildAt(0)).getText().toString(), v);
            }
        });

        tv_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SharedPreferencesUtil
                        .get(mContext, ProjectConstant.APP_USER_MID))) {
                    LoginActivity.startMe(mContext);
                } else {
                    Intent intent = new Intent(ProductListWithShaixuanActivity.this,
                            MyAddressActivity.class);
                    intent.putExtra("from", ProductListWithShaixuanActivity.class.getName());
                    startActivityForResult(intent, 100);
                }
            }
        });
        tv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.startMe(mContext);
            }
        });
    }

    private void openTwoDrawer(String title, View view) {
        View popupWindow_view = getLayoutInflater().inflate(R.layout.drawerlayout_erji_layout, null, false);

        twoDrawerBack_iv = (ImageView) popupWindow_view.findViewById(R.id.drawer_close);
        mDl_two_lv = (RecyclerView) popupWindow_view.findViewById(R.id.drawer_twotype_lv);
        mDl_two_lv.setLayoutManager(new LinearLayoutManager(this));
        erjiAdapter = new DrawerLayoutErjiAdapter(mContext);
        mDl_two_lv.setAdapter(erjiAdapter);

        List<String> datas = new ArrayList<>();
        datas.add("全部");
        datas.addAll(screenMap.get(title));
        erjiAdapter.setDatas(datas, title);

        erjiAdapter.setOnItemClickListener(new DrawerLayoutErjiAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(TextView name, ImageView right, int position, String title, String nameContent) {
                nameList.add(nameContent);
                titleList.add(title);
                for (int i = 0; i < mDl_two_lv.getChildCount(); i++) {
                    ((TextView) ((RelativeLayout) mDl_two_lv.getChildAt(i)).getChildAt(0)).setTextColor(Color.parseColor("#626262"));
                    ((RelativeLayout) mDl_two_lv.getChildAt(i)).getChildAt(1).setVisibility(View.GONE);
                }
                name.setTextColor(Color.parseColor("#df253d"));
                if (position == 0) {
                    currentSytleTv.setText("全部");
                    currentSytleTv.setTextColor(Color.parseColor("#626262"));
                } else {
                    currentSytleTv.setText(nameContent);
                    currentSytleTv.setTextColor(Color.parseColor("#df253d"));
                }
                popupWindow.dismiss();
            }
        });
        mDl_title = (TextView) popupWindow_view.findViewById(R.id.type_title);
        RelativeLayout emptyLayout = (RelativeLayout) popupWindow_view.findViewById(R.id.empty_layout);
        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        mDl_title.setText(title);
        twoDrawerBack_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(popupWindow_view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(00000000);
        popupWindow.setBackgroundDrawable(dw);
//        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(view, Gravity.RIGHT, 0, 0);
    }

    //筛选出家居的分类条件
    private void screen() {
        screenMap = new HashMap<>();
        for (ProductBean.ProductsBean product : mProductBean.products) {
            for (List<String> properties : product.properties) {
                if (!screenMap.containsKey(properties.get(0))) {
                    screenMap.put(properties.get(0), new ArrayList<String>());
                }
                if (!screenMap.get(properties.get(0)).contains(properties.get(1))) {
                    List<String> value = screenMap.get(properties.get(0));
                    value.add(properties.get(1));
                    screenMap.remove(properties.get(0));
                    screenMap.put(properties.get(0), value);
                }
            }
        }
        StyleSelect.getInstence().initMap(screenMap);
    }

    private ArrayList<ProductBean.ProductsBean> getProductsUsingScreenFromProducts(Map<String, String> map, ArrayList<ProductBean.ProductsBean> oldProducts) {
        ArrayList<ProductBean.ProductsBean> products = new ArrayList<>();
        if (map == null || map.size() <= 0) {
            return oldProducts;
        }
        List<String> mapScreenList = new ArrayList<>();
        for (String key : map.keySet()) {
            mapScreenList.add(key + "," + map.get(key));
        }
        for (ProductBean.ProductsBean product : oldProducts) {
            List<String> screenList = new ArrayList<>();
            for (List<String> list : product.properties) {
//                for (String key : map.keySet()) {
//                    if (list.get(0).equals(key) && list.get(1).equals(map.get(key))) {
//                        products.add(product);
//                    }
//                }
                screenList.add(list.get(0) + "," + list.get(1));
            }
            if (screenList.containsAll(mapScreenList)) {
                products.add(product);
            }
        }
        ArrayList<ProductBean.ProductsBean> finalProducts = new ArrayList<>();
        for (ProductBean.ProductsBean product2sEntity : products) {
            boolean isContains = false;
            for (ProductBean.ProductsBean product : finalProducts) {
                if (product2sEntity.pid.equals(product.pid)) {
                    isContains = true;
                    break;
                }
            }
            if (!isContains) {
                finalProducts.add(product2sEntity);
            }
        }
        return finalProducts;
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
