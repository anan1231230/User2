package com.hclz.client.shouye;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.MainActivity;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.ui.BaseFragment;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.DepthPageTransformer;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.base.view.WrapContentHeightViewPager;
import com.hclz.client.faxian.bean.Product;
import com.hclz.client.shouye.adapter.ProductAdapter;
import com.hclz.client.shouye.bean.ShouyeBean;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.view.Image3DSwitchView;
import com.hclz.client.faxian.CartActivity;
import com.hclz.client.faxian.ProductListWithTitlePicActivity;
import com.hclz.client.faxian.ProductListWithPaixuActivity;
import com.hclz.client.faxian.ProductDetailActivity;
import com.hclz.client.faxian.ProductSearchActivity;
import com.hclz.client.faxian.bean.ProductBean;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ShouyeFragment extends BaseFragment implements View.OnClickListener {

    View mWholeView;
    RecyclerView rv_products;
    GridLayoutManager mManager;
    ProductAdapter mAdapter;
    SwipeRefreshLayout mSwipe;
    ImageView iv_xiaoxi;

    ShouyeBean mBanner, mActivity, mIcon, mLifeShow, mTheme;

    //======header内容
    ImageView iv_miaosha, iv_huodong, iv_rexiaocaipin, iv_jinrixinwei, iv_qingjingmeishi, iv_xingfujiayan, iv_haiwaijingpin, iv_huodongbankuai;
    CardView cv_miaosha, cv_huodong, cv_rexiaocaipin, cv_jinrixinwei, cv_qingjingmeishi, cv_xingfujiayan, cv_haiwaijingpin, cv_huodongbankuai;
    LinearLayout iconOneLayout, iconTwoLayout, iconThreeLayout, iconFourLayout, iconFiveLayout;
    List<LinearLayout> mLayoutList;
    TextView lifeshow_title_tv, theme_title_tv, tuijian_title_tv;
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
                for (final ShouyeBean.ElementsBean banner : mBanner.getElements()) {
                    ImageView img = ImageUtility.createImageView(mContext);
                    ImageUtility.getInstance(mContext).showImage(banner.getImage(), img);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProductListWithTitlePicActivity.startMe(mContext, banner.getContent(),banner.getName(), banner.getImage());
                        }
                    });
                    liHeadCarouselFigureView.add(img);
                }
                // 设定头部轮播图图片的处理
                setViPgrCarouselFigure();
            }
            return true;
        }
    });
    //======= 轮播图变量定义 End============================================================
    private int mScreenWidth, mScreenHeight;
    private boolean isFromCreate;
    private ProductBean mProductBean;

    private ImageView mSearchIv, mCartIV;
    private ImageView mDorIv;

    public ShouyeFragment() {
    }

    public static void startMe(Context from) {
        SanmiConfig.isMallNeedRefresh = true;
        Intent intent = new Intent(from, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fragment", "ShouyeFragment");
        from.startActivity(intent);
    }

    public static void startMeFromSelfActivity(Context from) {
        ((MainActivity) from).rdoMall.setChecked(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isFromCreate = true;
        return mWholeView = inflater.inflate(R.layout.fragment_shouye, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (SanmiConfig.isMallNeedRefresh && ((MainActivity) mContext).getCurrentVisibleFragment() == MainActivity.CURRENT_HCLZ) {
            SanmiConfig.isMallNeedRefresh = false;
            initData();
        }
        showNumber();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("DianCanFragment");
        if (isFromCreate) {
            isFromCreate = false;
            return;
        }
        if (SanmiConfig.isMallNeedRefresh && ((MainActivity) mContext).getCurrentVisibleFragment() == MainActivity.CURRENT_HCLZ) {
            SanmiConfig.isMallNeedRefresh = false;
            initData();
        }
        showNumber();
    }

    @Override
    public void onStop() {
        super.onStop();
        MobclickAgent.onPageEnd("DianCanFragment");
    }

    @Override
    protected void initView() {
        mSearchIv = (ImageView) mWholeView.findViewById(R.id.iv_search);
        mCartIV = (ImageView) mWholeView.findViewById(R.id.iv_cart);
        mDorIv = (ImageView) mWholeView.findViewById(R.id.notification_dot_iv);
        rv_products = (RecyclerView) mWholeView.findViewById(R.id.rv_products);
        mSwipe = (SwipeRefreshLayout) mWholeView.findViewById(R.id.srf);
        mSwipe.setColorSchemeResources(R.color.main, R.color.yellow, R.color.main,
                R.color.yellow);
        iv_xiaoxi = (ImageView) mWholeView.findViewById(R.id.notification_iv);
    }

    @Override
    protected void initInstance() {
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
        rv_products.setLayoutManager(mManager);
        rv_products.setAdapter(mAdapter);
        rv_products.setItemAnimator(new DefaultItemAnimator());
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.isHeader(position) ? mManager.getSpanCount() : 1;
            }
        });
        initHeader(rv_products);
    }

    @Override
    protected void initData() {

        configMap = HclzApplication.getData();
        if (((MainActivity) mContext).getCurrentVisibleFragment() == MainActivity.CURRENT_HCLZ) {
            showLoadingDialog();
        }

        getShouYeInfo();
    }

    @Override
    protected void setViewData() {

    }

    @Override
    protected void setListener() {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        iv_xiaoxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mContext, "暂无消息");
            }
        });
        mSearchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductSearchActivity.startMe(mContext);
            }
        });
        mCartIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.startMe(mContext);
            }
        });
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

        showHeader();

        mAdapter.setHeaderView(mPromotionHeader);

        tuijian_title_tv.setText(TextUtils.isEmpty(mProductBean.block_name) ? "推荐商品" : mProductBean.block_name);
        mAdapter.setData(mProductBean.products);
        mAdapter.notifyDataSetChanged();

        new DataThread().start();
        dissmissDialog();
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
    }

    private void showHeader() {

        //lifeshow填充数据
        imageSwitchView.removeAllViewsInLayout();
        if (mLifeShow != null) {
            theme_title_tv.setText(TextUtils.isEmpty(mLifeShow.getBlock_name()) ? "吃货腔调" : mLifeShow.getBlock_name());
            for (final ShouyeBean.ElementsBean lifeshow : mLifeShow.getElements()) {
                ImageView tmpView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.item_lifeshow2, null);
                LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(
                        mScreenWidth * 3 / 5, mScreenWidth * 3 / 5 * 2 / 5);
                tmpView.setLayoutParams(imgparams);
                imageSwitchView.addView(tmpView);
                ImageUtility.getInstance(mContext).showImage(lifeshow.getImage(), tmpView);
                tmpView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProductListWithTitlePicActivity.startMe(mContext, lifeshow.getContent(),lifeshow.getName(), lifeshow.getImage());
                    }
                });
            }
            imageSwitchView.setOnImageSwitchListener(new Image3DSwitchView.OnImageSwitchListener() {
                @Override
                public void onImageSwitch(int currentImage) {
                    // TODO
                }
            });
            imageSwitchView.setCurrentImage(2);
            imageSwitchView.scrollToNext();
            imageSwitchView.scrollToPrevious();
        }
        //icon填充数据
        if (mIcon != null) {
            for (int i = 0; i < mIcon.getElements().size(); i++) {

                final ShouyeBean.ElementsBean bean = mIcon.getElements().get(i);
                LinearLayout layout = mLayoutList.get(i);

                ImageUtility.getInstance(mContext).showImage(bean.getImage(), ((ImageView) layout.getChildAt(0)));
                ((TextView) mLayoutList.get(i).getChildAt(1)).setText(bean.getName());
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ProductListWithPaixuActivity.startMe(mContext, bean.getContent(), bean.getContent(), 1);
                    }
                });
            }
        }

        //activity填充数据
        if (mActivity != null) {
            ImageUtility.getInstance(mContext).showImage(mActivity.getElements().get(0).getImage(), iv_miaosha);
            cv_miaosha.setOnClickListener(this);
            ImageUtility.getInstance(mContext).showImage(mActivity.getElements().get(1).getImage(), iv_huodong);
            cv_huodong.setOnClickListener(this);
            ImageUtility.getInstance(mContext).showImage(mActivity.getElements().get(2).getImage(), iv_rexiaocaipin);
            cv_rexiaocaipin.setOnClickListener(this);
            ImageUtility.getInstance(mContext).showImage(mActivity.getElements().get(3).getImage(), iv_jinrixinwei);
            cv_jinrixinwei.setOnClickListener(this);
        }

        //theme填充数据
        if (mTheme != null) {
            theme_title_tv.setText(TextUtils.isEmpty(mTheme.getBlock_name()) ? "吃货情怀" : mTheme.getBlock_name());

            ImageUtility.getInstance(mContext).showImage(mTheme.getElements().get(0).getImage(), iv_qingjingmeishi);
            cv_qingjingmeishi.setOnClickListener(this);
            ImageUtility.getInstance(mContext).showImage(mTheme.getElements().get(1).getImage(), iv_xingfujiayan);
            cv_xingfujiayan.setOnClickListener(this);
            ImageUtility.getInstance(mContext).showImage(mTheme.getElements().get(2).getImage(), iv_haiwaijingpin);
            cv_haiwaijingpin.setOnClickListener(this);
            ImageUtility.getInstance(mContext).showImage(mTheme.getElements().get(3).getImage(), iv_huodongbankuai);
            cv_huodongbankuai.setOnClickListener(this);
        }
    }

    private void getScreenSize() {
        Display d = mContext.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
    }

    private void initHeader(RecyclerView view) {
        mPromotionHeader = LayoutInflater.from(mContext).inflate(R.layout.fragment_shouye_header, view, false);
        initLunbotu();

        getScreenSize();
        LinearLayout.LayoutParams imgparams_miaosha = new LinearLayout.LayoutParams(
                (int) (mScreenWidth / 2 - CommonUtil.convertDpToPixel(11, mContext)), (int) ((mScreenWidth - CommonUtil.convertDpToPixel(22, mContext)) * 300 / 1024));
        LinearLayout.LayoutParams imgparams_huodong = new LinearLayout.LayoutParams(
                (int) (mScreenWidth / 2 - CommonUtil.convertDpToPixel(11, mContext)), (int) ((mScreenWidth - CommonUtil.convertDpToPixel(22, mContext)) * 300 / 1024));
        LinearLayout.LayoutParams imgparams_full = new LinearLayout.LayoutParams(
                (int) (mScreenWidth - CommonUtil.convertDpToPixel(16, mContext)), (int) ((mScreenWidth - CommonUtil.convertDpToPixel(16, mContext)) * 512 / 1024));
        imgparams_miaosha.setMargins((int) CommonUtil.convertDpToPixel(8, mContext), (int) CommonUtil.convertDpToPixel(6, mContext), (int) CommonUtil.convertDpToPixel(3, mContext), 0);
        imgparams_huodong.setMargins((int) CommonUtil.convertDpToPixel(3, mContext), (int) CommonUtil.convertDpToPixel(6, mContext), (int) CommonUtil.convertDpToPixel(8, mContext), 0);
        imgparams_full.setMargins((int) CommonUtil.convertDpToPixel(8, mContext), (int) CommonUtil.convertDpToPixel(6, mContext), (int) CommonUtil.convertDpToPixel(8, mContext), 0);
        iv_miaosha = (ImageView) mPromotionHeader.findViewById(R.id.iv_miaosha);
        iv_huodong = (ImageView) mPromotionHeader.findViewById(R.id.iv_huodongqu);
        iv_rexiaocaipin = (ImageView) mPromotionHeader.findViewById(R.id.iv_rexiaocaipin);
        iv_jinrixinwei = (ImageView) mPromotionHeader.findViewById(R.id.iv_jinrixinwei);
        iv_qingjingmeishi = (ImageView) mPromotionHeader.findViewById(R.id.iv_qingjingmeishi);
        iv_xingfujiayan = (ImageView) mPromotionHeader.findViewById(R.id.iv_xingfujiayan);
        iv_haiwaijingpin = (ImageView) mPromotionHeader.findViewById(R.id.iv_haiwaijingpin);
        iv_huodongbankuai = (ImageView) mPromotionHeader.findViewById(R.id.iv_huodongbankuai);
        cv_miaosha = (CardView) mPromotionHeader.findViewById(R.id.cv_miaosha);
        cv_huodong = (CardView) mPromotionHeader.findViewById(R.id.cv_huodong);
        cv_rexiaocaipin = (CardView) mPromotionHeader.findViewById(R.id.cv_rexiao);
        cv_jinrixinwei = (CardView) mPromotionHeader.findViewById(R.id.cv_jinrixinwei);
        cv_qingjingmeishi = (CardView) mPromotionHeader.findViewById(R.id.cv_qingjingmeishi);
        cv_xingfujiayan = (CardView) mPromotionHeader.findViewById(R.id.cv_xingfujiayan);
        cv_haiwaijingpin = (CardView) mPromotionHeader.findViewById(R.id.cv_haiwaijingpin);
        cv_huodongbankuai = (CardView) mPromotionHeader.findViewById(R.id.cv_huodongbankuai);
        cv_miaosha.setLayoutParams(imgparams_miaosha);
        cv_huodong.setLayoutParams(imgparams_huodong);
        cv_rexiaocaipin.setLayoutParams(imgparams_full);
        cv_jinrixinwei.setLayoutParams(imgparams_full);
        cv_qingjingmeishi.setLayoutParams(imgparams_full);
        cv_xingfujiayan.setLayoutParams(imgparams_full);
        cv_haiwaijingpin.setLayoutParams(imgparams_full);
        cv_huodongbankuai.setLayoutParams(imgparams_full);

        mLayoutList = new ArrayList<>();
        iconOneLayout = (LinearLayout) mPromotionHeader.findViewById(R.id.icon_one);
        mLayoutList.add(iconOneLayout);
        iconTwoLayout = (LinearLayout) mPromotionHeader.findViewById(R.id.icon_two);
        mLayoutList.add(iconTwoLayout);
        iconThreeLayout = (LinearLayout) mPromotionHeader.findViewById(R.id.icon_three);
        mLayoutList.add(iconThreeLayout);
        iconFourLayout = (LinearLayout) mPromotionHeader.findViewById(R.id.icon_four);
        mLayoutList.add(iconFourLayout);
        iconFiveLayout = (LinearLayout) mPromotionHeader.findViewById(R.id.icon_five);
        mLayoutList.add(iconFiveLayout);

        lifeshow_title_tv = (TextView) mPromotionHeader.findViewById(R.id.lifeshow_title);
        theme_title_tv = (TextView) mPromotionHeader.findViewById(R.id.theme_title);
        tuijian_title_tv = (TextView) mPromotionHeader.findViewById(R.id.tuijian_product);

        imageSwitchView = (Image3DSwitchView) mPromotionHeader.findViewById(R.id.image_switch_view);
        LinearLayout.LayoutParams imageSeichParams = new LinearLayout.LayoutParams(
                mScreenWidth, mScreenWidth / 4);
        imageSwitchView.setLayoutParams(imageSeichParams);
    }

    private void initLunbotu() {
        viPgrCarouselFigure = (WrapContentHeightViewPager) mPromotionHeader
                .findViewById(R.id.viPgr_carousel_figure);
        lLytCarouselFigureBottomDot = (LinearLayout) mPromotionHeader
                .findViewById(R.id.lLyt_Carousel_Figure_bottom_dot);
    }

    private void getShouYeInfo() {
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
            String zujiString = SharedPreferencesUtil.get(mContext,ProjectConstant.APP_ZUJIS);
            JSONArray jsonArray = new JSONArray();
            ArrayList<String> pids=new ArrayList<>();
            if (!TextUtils.isEmpty(zujiString)) {
                ArrayList<ProductBean.ProductsBean> zujis = JsonUtility.fromJson(JsonUtility.parseArray(zujiString),new TypeToken<ArrayList<ProductBean.ProductsBean>>(){});
                if (zujis != null && zujis.size()>0) {
                    for (int i=0;i< (zujis.size()>3?3:zujis.size()-1);i++){
                        jsonArray.put(zujis.get(i).pid);
                        pids.add(zujis.get(i).pid);
                    }
                    contentObj.put("pids",pids);
                }
            }
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.setIsShowDialog(false);
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.SHOUYE_INFO.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mBanner = JsonUtility.fromJson(obj.get("banners"), new TypeToken<ShouyeBean>() {
                            });
                            mActivity = JsonUtility.fromJson(obj.get("activities"), new TypeToken<ShouyeBean>() {
                            });
                            mIcon = JsonUtility.fromJson(obj.get("icons"), new TypeToken<ShouyeBean>() {
                            });
                            mLifeShow = JsonUtility.fromJson(obj.get("lifeshows"), new TypeToken<ShouyeBean>() {
                            });
                            mTheme = JsonUtility.fromJson(obj.get("themes"), new TypeToken<ShouyeBean>() {
                            });
                            mProductBean = JsonUtility.fromJson(obj.get("recommend"), ProductBean.class);
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //=================设定头部轮播图ViewPage的处理 START=======================================
    private void setViPgrCarouselFigure() {
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

    @Override
    public void onClick(View v) {
        String content = null, imageUrl = null, name = null;
        int i = 0;
        switch (v.getId()) {
            case R.id.cv_miaosha:
                i = 0;
                content = mActivity.getElements().get(0).getContent();
                imageUrl = mActivity.getElements().get(0).getImage();
                name = mActivity.getElements().get(0).getName();
                break;
            case R.id.cv_huodong:
                content = mActivity.getElements().get(1).getContent();
                imageUrl = mActivity.getElements().get(1).getImage();
                name = mActivity.getElements().get(1).getName();
                break;
            case R.id.cv_rexiao:
                content = mActivity.getElements().get(2).getContent();
                imageUrl = mActivity.getElements().get(2).getImage();
                name = mActivity.getElements().get(2).getName();
                break;
            case R.id.cv_jinrixinwei:
                content = mActivity.getElements().get(3).getContent();
                imageUrl = mActivity.getElements().get(3).getImage();
                name = mActivity.getElements().get(3).getName();
                break;
            case R.id.cv_qingjingmeishi:
                content = mTheme.getElements().get(0).getContent();
                imageUrl = mTheme.getElements().get(0).getImage();
                name = mTheme.getElements().get(0).getName();
                break;
            case R.id.cv_xingfujiayan:
                content = mTheme.getElements().get(1).getContent();
                imageUrl = mTheme.getElements().get(1).getImage();
                name = mTheme.getElements().get(1).getName();
                break;
            case R.id.cv_haiwaijingpin:
                content = mTheme.getElements().get(2).getContent();
                imageUrl = mTheme.getElements().get(2).getImage();
                name = mTheme.getElements().get(2).getName();
                break;
            case R.id.cv_huodongbankuai:
                content = mTheme.getElements().get(3).getContent();
                imageUrl = mTheme.getElements().get(3).getImage();
                name = mTheme.getElements().get(3).getName();
                break;
        }
        ProductListWithTitlePicActivity.startMe(mContext, content,name, imageUrl);
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

    private void dissmissDialog() {
        handler.sendEmptyMessageDelayed(8888, 1000);
        SanmiConfig.isMallNeedRefresh = false;
    }

    private void showLoadingDialog() {
        if (mSwipe.isRefreshing()) {
            return;
        }
        WaitingDialogControll.showLoadingDialog(mContext);
        handler.sendEmptyMessageDelayed(8888, 20000);
    }
}
