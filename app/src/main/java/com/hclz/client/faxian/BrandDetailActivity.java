package com.hclz.client.faxian;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.Order;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.DepthPageTransformer;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.view.WrapContentHeightViewPager;
import com.hclz.client.faxian.adapter.BrandAdapter;
import com.hclz.client.faxian.adapter.DecorationAdapter;
import com.hclz.client.faxian.bean.Brand;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.faxian.products.AddressIns;
import com.hclz.client.login.LoginActivity;
import com.hclz.client.me.MyAddressActivity;
import com.hclz.client.me.StaffDetailActivity;
import com.hclz.client.order.confirmorder.bean.address.NetAddress;
import com.hclz.client.order.confirmorder.bean.staff.NetStaff;
import com.hclz.client.shouye.ShouyeFragment;
import com.hclz.client.shouye.adapter.ProductAdapter;
import com.hclz.client.shouye.bean.ShouyeBean;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.view.Image3DSwitchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BrandDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView mBackIv, mTopIv, mCartIv;
    private int bmpW;// 动画图片宽度
    private GridLayoutManager mManager;
    private BrandAdapter mAdapter;
    ArrayList<ProductBean.ProductsBean> mData;
    RecyclerView recyclerView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private TextView mTitleTv, mEmptyTv;
    private Toolbar mToolbar;
    private ImageView mDorIv;
    private ImageView iv_search;
    RadioGroup rg_group;
    private ProductBean mProductBean;
    Brand brand;
    private DecorationAdapter decorationAdapter;
    private List<String> decorationList, storeList;
    private TextView tv_address, tv_cart, tv_change_address;
    LinearLayout ll_brand_product_list, ll_brand_story, ll_brand_image, ll_brand_product;
    TextView tv_brand_story, tv_brand_desc;
    ListView lv_brand_image;
    List<String> mBanner, mActivity;

    public static void startMe(Context from, Brand brand) {
        Intent intent = new Intent(from, BrandDetailActivity.class);
        if (brand != null) {
            intent.putExtra("brand", brand);
        }
        from.startActivity(intent);
    }

    public static void startMe(Context from) {
        Intent intent = new Intent(from, BrandDetailActivity.class);
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

        setContentView(R.layout.activity_brand_detail);
    }

    @Override
    protected void initView() {

        tv_brand_desc = (TextView) findViewById(R.id.tv_brand_desc);
        ll_brand_product_list = (LinearLayout) findViewById(R.id.ll_brand_product_list);
        ll_brand_product = (LinearLayout) findViewById(R.id.ll_brand_product);
        ll_brand_image = (LinearLayout) findViewById(R.id.ll_brand_image);
        ll_brand_story = (LinearLayout) findViewById(R.id.ll_brand_story);

        lv_brand_image = (ListView) findViewById(R.id.lv_brand_image);
        tv_brand_story = (TextView) findViewById(R.id.tv_brand_story);
        tv_change_address = (TextView) findViewById(R.id.tv_change_address);
        tv_cart = (TextView) findViewById(R.id.tv_cart);
        tv_address = (TextView) findViewById(R.id.tv_address);


        mBackIv = (ImageView) findViewById(R.id.iv_comm_head_left);
        mTopIv = (ImageView) findViewById(R.id.product_image);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mBackIv = (ImageView) findViewById(R.id.iv_comm_head_left);
        tv_cart = (TextView) findViewById(R.id.tv_cart);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mManager = new GridLayoutManager(mContext, 2);

        recyclerView.setLayoutManager(mManager);
        mAdapter = new BrandAdapter(mContext, new BrandAdapter.BrandListener() {
            @Override
            public void onItemSelected(int position, ProductBean.ProductsBean item) {
                ProductDetailActivity.startMe(mContext, item);
            }

            @Override
            public void addCart( ProductBean.ProductsBean item) {

            }
        });

        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(mAdapter);


    }

    private void getBrandDetail() {
        requestParams = new HashMap<>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            String mid = SharedPreferencesUtil.get(mContext,
                    ProjectConstant.APP_USER_MID);
            String appid = SharedPreferencesUtil.get(mContext,
                    ProjectConstant.APPID);
            String platform = SharedPreferencesUtil.get(mContext,
                    ProjectConstant.PLATFORM);
            String appversion = SharedPreferencesUtil.get(mContext,
                    ProjectConstant.APP_VERSION);

            if (!TextUtils.isEmpty(mid)) {
                contentObj.put(ProjectConstant.APP_USER_MID, mid);
            }
            if (!TextUtils.isEmpty(appid)) {
                contentObj.put(ProjectConstant.APPID, appid);
            }
            if (!TextUtils.isEmpty(platform)) {
                contentObj.put(ProjectConstant.PLATFORM, platform);
            }
            if (!TextUtils.isEmpty(appversion)) {
                contentObj.put(ProjectConstant.APP_VERSION, appversion);
            }
            contentObj.put("franchise_store",brand.getFid());
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.setIsShowDialog(false);

            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.BRAND_GOODS.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            mProductBean = JsonUtility.fromJson(result, ProductBean.class);
                            AddressIns.getInstance().setAddress(mProductBean.address);
                            mData = new ArrayList<ProductBean.ProductsBean>();
                            for (ProductBean.ProductsBean bean : mProductBean.products) {
                                mData.add(bean);
                            }
                            showContent();

                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showContent() {
        if (AddressIns.getInstance().getmAddress() != null) {
            tv_address.setText(AddressIns.getInstance().getmAddress().addr_detail);
        }


        mAdapter.setData(mData);
    }

    //======= 轮播图变量定义 START===========================================================
    View mPromotionHeader;
    private Image3DSwitchView imageSwitchView;
    private WrapContentHeightViewPager viPgrCarouselFigure;
    private AtomicInteger what = new AtomicInteger(0);
    private List<View> liHeadCarouselFigureView = new ArrayList<View>();
    // 存放轮播图下部的图片（圆点）
    private ImageView[] imgViewDots = null;
    private final WeakHandler viewHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            viPgrCarouselFigure.setCurrentItem(what.get());
            what.incrementAndGet();
            if (what.get() > imgViewDots.length - 1) {
                what.getAndAdd(-imgViewDots.length);
            }
            viewHandler.sendEmptyMessageDelayed(0, 5000);
            return true;
        }
    });
    // 轮播图的下部
    private LinearLayout lLytCarouselFigureBottomDot;
    WeakHandler dataHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (lLytCarouselFigureBottomDot != null) {
                lLytCarouselFigureBottomDot.removeAllViews();
            }
            if (liHeadCarouselFigureView != null) {
                liHeadCarouselFigureView.clear();
            }
            if (mBanner != null) {
                for (final String banner : mBanner) {
                    ImageView img = ImageUtility.createImageView(mContext);
                    ImageUtility.getInstance(mContext).showImage(banner, img);
                    liHeadCarouselFigureView.add(img);
                }
                // 设定头部轮播图图片的处理
                setViPgrCarouselFigure();
            }
            return true;
        }
    });

    //======= 轮播图变量定义 End============================================================
    private void initLunbotu() {
        viPgrCarouselFigure = (WrapContentHeightViewPager) findViewById(R.id.viPgr_carousel_figure);
        lLytCarouselFigureBottomDot = (LinearLayout) findViewById(R.id.lLyt_Carousel_Figure_bottom_dot);
    }

    @Override
    protected void initInstance() {
//        setCommonTitle("品牌特色");
//        ImageUtility.getInstance(mContext).showImage("http://img.hclz.me/data%2Fbb4f97a24b24f4b5f49f3c64a4b859d7.png", mTopIv);
        configMap = HclzApplication.getData();
        initLunbotu();
    }


    @Override
    protected void initData() {
        if (mIntent != null) {
            brand = (Brand) mIntent.getSerializableExtra("brand");
        }
        tv_brand_desc.setText(brand.introduction);
        tv_brand_story.setText(brand.brand_story);
        decorationList = brand.getDecoration_img();
        storeList = brand.getStore_img();
        decorationAdapter = new DecorationAdapter(mContext, decorationList);
        decorationAdapter.setEmptyString("暂时没有图片");
        lv_brand_image.setAdapter(decorationAdapter);
        mBanner = brand.getStore_img();
        fixListViewHeight(lv_brand_image);


        setCommonTitle(brand.getName());
//        String zujiString = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_ZUJIS);
//        mData = JsonUtility.fromJson(JsonUtility.parseArray(zujiString), new TypeToken<ArrayList<ProductBean.ProductsBean>>() {
//        });

        getBrandDetail();


        new BrandDetailActivity.DataThread().start();
//        if (mSwipeRefreshLayout.isRefreshing()) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
    }

    /**
     * 修复scrollview下listview只显示一条数据问题
     *
     * @param listView
     */
    public void fixListViewHeight(ListView listView) {

        // 如果没有设置数据适配器，则ListView没有子项，返回。

        ListAdapter listAdapter = listView.getAdapter();

        int totalHeight = 0;

        if (listAdapter == null) {

            return;

        }

        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {

            View listViewItem = listAdapter.getView(i, null, listView);

            // 计算子项View 的宽高

            listViewItem.measure(0, 0);

            // 计算所有子项的高度和

            totalHeight += listViewItem.getMeasuredHeight();

        }


        ViewGroup.LayoutParams params = listView.getLayoutParams();

        // listView.getDividerHeight()获取子项间分隔符的高度

        // params.height设置ListView完全显示需要的高度

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);

    }

    @Override
    protected void initListener() {
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                initData();
//            }
//        });
        tv_cart.setOnClickListener(this);
        tv_change_address.setOnClickListener(this);
        ll_brand_story.setOnClickListener(this);
        ll_brand_product.setOnClickListener(this);
        ll_brand_image.setOnClickListener(this);


        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mCartIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CartActivity.startMe(mContext);
//            }
//        });


    }

    private void removeShow() {
        tv_brand_story.setVisibility(View.GONE);
        lv_brand_image.setVisibility(View.GONE);
        ll_brand_product_list.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        removeShow();
        switch (v.getId()) {
            case R.id.ll_brand_image:
                lv_brand_image.setVisibility(View.VISIBLE);
                decorationAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_brand_story:
                tv_brand_story.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_brand_product:
                ll_brand_product_list.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_cart:
                CartActivity.startMe(mContext);
                break;
            case R.id.tv_change_address:
                if (TextUtils.isEmpty(SharedPreferencesUtil
                        .get(mContext, ProjectConstant.APP_USER_MID))) {
                    LoginActivity.startMe(mContext);
                } else {
                    Intent intent = new Intent(BrandDetailActivity.this,
                            MyAddressActivity.class);
                    intent.putExtra("from", ProductListWithPaixuActivity.class.getName());
                    startActivityForResult(intent, 100);
                }
                break;
        }
    }


    //    public class MyViewPagerAdapter extends PagerAdapter {
//        private List<View> mListViews;
//
//        public MyViewPagerAdapter(List<View> mListViews) {
//            this.mListViews = mListViews;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object)   {
//            container.removeView(mListViews.get(position));
//        }
//
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            container.addView(mListViews.get(position), 0);
//            return mListViews.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return  mListViews.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0==arg1;
//        }
//    }
//
//    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//
//        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
//        int two = one * 2;// 页卡1 -> 页卡3 偏移量
//        public void onPageScrollStateChanged(int arg0) {
//
//
//        }
//
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//
//        }
//
//        public void onPageSelected(int arg0) {
//            switch (arg0){
//                case 0:
//                    rg_group.check(R.id.text1);
//                    break;
//                case 1:
//                    rg_group.check(R.id.text2);
//                    break;
//                case 2:
//                    rg_group.check(R.id.text3);
//                    break;
//            }
//
////            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);//显然这个比较简洁，只有一行代码。
////            currIndex = arg0;
////            animation.setFillAfter(true);// True:图片停在动画结束位置
////            animation.setDuration(300);
////            imageView.startAnimation(animation);
//        }
//
//    }
//=================设定头部轮播图ViewPage的处理 START=======================================
    private void setViPgrCarouselFigure() {

        Log.d("轮播", "");

        // 对轮播图下部的图片（圆点）进行填充 开始-->
        imgViewDots = new ImageView[liHeadCarouselFigureView.size()];
        for (int i = 0; i < liHeadCarouselFigureView.size(); i++) {
            ImageView imgViewTemp = new ImageView(mContext);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8,
                    8);
            params.setMargins(0, 0, 8, 0);
            imgViewTemp.setLayoutParams(params);
            imgViewTemp.setPadding(0, 0, 0, 0);
            imgViewTemp.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgViewDots[i] = imgViewTemp;
            if (i == 0) {
                imgViewDots[i]
                        .setBackgroundResource(R.mipmap.banner_dian_focus);
            } else {
                imgViewDots[i]
                        .setBackgroundResource(R.mipmap.banner_dian_blur);
            }
            lLytCarouselFigureBottomDot.addView(imgViewDots[i]);
        }
        // 对轮播图下部的图片（圆点）进行填充 结束<--

        viPgrCarouselFigure
                .setPageTransformer(true, new DepthPageTransformer());
        viPgrCarouselFigure.setBackgroundColor(Color.WHITE);
        viPgrCarouselFigure.setAdapter(new CarouselFigureAdapter(
                liHeadCarouselFigureView));
        viPgrCarouselFigure
                .addOnPageChangeListener(new GuidePageChangeListener());
        viPgrCarouselFigure.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        if (viewHandler != null) {
            if (viewHandler.hasMessages(0)) {
                viewHandler.removeMessages(0);
            }
            what.set(0);
            viewHandler.sendEmptyMessageDelayed(0, 5000);
        }
    }


    //===============以下为获取本地配置文件中的轮播图并展示的功能=================================
    class DataThread extends Thread {
        @Override
        public void run() {
            dataHandler.sendEmptyMessage(0);
        }
    }

    //===========类 名:CarouselFigureAdapter 主要功能:轮播图填充的Adapter===================
    private final class CarouselFigureAdapter extends PagerAdapter {
        private List<View> views = null;

        public CarouselFigureAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }
    }

    //=============GuidePageChangeListener 主要功能:轮播图改变的Listener============================
    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            what.getAndSet(arg0);
            for (int i = 0; i < imgViewDots.length; i++) {
                imgViewDots[arg0]
                        .setBackgroundResource(R.mipmap.banner_dian_focus);
                if (arg0 != i) {
                    imgViewDots[i]
                            .setBackgroundResource(R.mipmap.banner_dian_blur);
                }
            }
        }
    }
    //=================设定头部轮播图ViewPage的处理 END=======================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            if (AddressIns.getInstance().getmAddress() != null) {
                tv_address.setText(AddressIns.getInstance().getmAddress().addr_detail);
                getBrandDetail();
            } else {
                tv_address.setText("潍坊市奎文区");
            }
        }
    }


}
