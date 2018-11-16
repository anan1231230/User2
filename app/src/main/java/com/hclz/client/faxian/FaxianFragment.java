package com.hclz.client.faxian;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.MainActivity;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.location.LocationUtils;
import com.hclz.client.base.ui.BaseFragment;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.faxian.adapter.DiancanTypeAdapter;
import com.hclz.client.faxian.adapter.JiajuType2Adapter;
import com.hclz.client.faxian.adapter.JiajuType3Adapter;
import com.hclz.client.faxian.adapter.LaidianAdapter;
import com.hclz.client.faxian.adapter.PinPaiAdapter;
import com.hclz.client.faxian.adapter.PinPaiGridAdapter;
import com.hclz.client.faxian.adapter.TabFragmentAdapter;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.faxian.bean.TypeBean;
import com.hclz.client.faxian.view.FindViewPager;
import com.hclz.client.me.adapter.ZujiAdapter;
import com.hclz.client.shouye.adapter.ProductAdapter;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by handsome on 16/6/12.
 */
public class FaxianFragment extends BaseFragment {

    private View viWholeView;
    private SwipeRefreshLayout mSwipe;
    private ImageView iv_xiaoxi;

    //========标题栏
    private TabLayout mTabLayout;
    private FindViewPager mViewpager;
    private TypeBean mTypeBeanLaidian, mTypeBeanDiancan, mTypeBeanJiaju;
    private TabFragmentAdapter adapter;
    private View viewLaidian, viewDiancan, viewJiaju;
    private List<View> mViewList;
    private RecyclerView mRecyclerViewLaidian, mRecyclerViewDiancan, mRecyclerViewJiajuType2, mRecyclerViewJiajuType3;
    private LaidianAdapter laidianAdapter;
    //把来点换成、品牌特色需要更换adapter
//    private PinPaiAdapter laidianAdapter;
    private DiancanTypeAdapter diancanTypeAdapter;

    //家居建材
    private JiajuType2Adapter mJiajuType2Adapter;
    private JiajuType3Adapter mJiajuType3Adapter;
    private String mNote;
    private TextView tv_note;
    private GridLayoutManager mManager;

    private ImageView mSearchIv, mCartIV;
    private ImageView mDorIv;


    //增加品牌
//    GridLayoutManager pinpaiManager;
//    PinPaiGridAdapte、r pinpaiAdapter;
    public static void startMe(Context from) {
        SanmiConfig.isHaiwaiNeedRefresh = true;
        Intent intent = new Intent(from, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fragment", "FaxianFragment");
        from.startActivity(intent);
    }

    public static void startMeFromSelfActivity(Context from) {
        ((MainActivity) from).rdoHaiwai.setChecked(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viWholeView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_faxian, container, false);
        SanmiConfig.isHaiwaiNeedRefresh = false;
        return viWholeView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (SanmiConfig.isHaiwaiNeedRefresh && ((MainActivity) mContext).getCurrentVisibleFragment() == MainActivity.CURRENT_HWJP) {
            SanmiConfig.isHaiwaiNeedRefresh = false;
            initData();
        }
        showNumber();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FaxianFragment");
        if (SanmiConfig.isHaiwaiNeedRefresh && ((MainActivity) mContext).getCurrentVisibleFragment() == MainActivity.CURRENT_HWJP) {
            SanmiConfig.isHaiwaiNeedRefresh = false;
            initData();
        }
        showNumber();
    }

    @Override
    public void onStop() {
        super.onStop();
        MobclickAgent.onPageEnd("FaxianFragment");
        LocationUtils.getInstence().stop();
    }

    @Override
    protected void initView() {
        mSearchIv = (ImageView) viWholeView.findViewById(R.id.iv_search);
        mCartIV = (ImageView) viWholeView.findViewById(R.id.iv_cart);
        mDorIv = (ImageView) viWholeView.findViewById(R.id.notification_dot_iv);
        mSwipe = (SwipeRefreshLayout) viWholeView.findViewById(R.id.swipe);
        mSwipe.setColorSchemeResources(R.color.main, R.color.yellow, R.color.main,
                R.color.yellow);
        iv_xiaoxi = (ImageView) viWholeView.findViewById(R.id.notification_iv);
        mTabLayout = (TabLayout) viWholeView.findViewById(R.id.faxian_tablayout);
        mViewpager = (FindViewPager) viWholeView.findViewById(R.id.viewpager);

        viewLaidian = LayoutInflater.from(mContext).inflate(R.layout.fragment_tab, null);
        laidianAdapter = new LaidianAdapter(mContext, new LaidianAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position, TypeBean.SubsBean subsBean) {
                ProductListWithPaixuActivity.startMe(mContext, subsBean.getName(), subsBean.getCateid(), 0);
            }
        });


        mRecyclerViewLaidian = (RecyclerView) viewLaidian.findViewById(R.id.tab_recycleview);
        mRecyclerViewLaidian.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewLaidian.setAdapter(laidianAdapter);
/**
 * 来点换成品牌
 */
//        pinpaiManager = new GridLayoutManager(mContext, 3);
//        pinpaiAdapter = new PinPaiGridAdapter(mContext, new PinPaiGridAdapter.ZujiListener() {
//            @Override
//            public void onItemSelected(int position, TypeBean.SubsBean item) {
//                ProductListWithPaixuActivity.startMe(mContext, item.getName(), item.getCateid(), 0);
//            }
//        });
//        mRecyclerViewLaidian.setLayoutManager(pinpaiManager);
//        mRecyclerViewLaidian.setAdapter(pinpaiAdapter);


        viewDiancan = LayoutInflater.from(mContext).inflate(R.layout.fragment_tab, null);
        diancanTypeAdapter = new DiancanTypeAdapter(mContext, new DiancanTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TypeBean.SubsBean subsBean) {
                if (subsBean.getName().equals("蔬菜凉菜")) {
                    BrandListActivity.startMe(mContext);
                } else {
                    ProductListWithPaixuActivity.startMe(mContext, subsBean.getName(), subsBean.getCateid(), 0);
                }

            }
        });
        mRecyclerViewDiancan = (RecyclerView) viewDiancan.findViewById(R.id.tab_recycleview);
        mRecyclerViewDiancan.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewDiancan.setAdapter(diancanTypeAdapter);

        viewJiaju = LayoutInflater.from(mContext).inflate(R.layout.fragment_tab_jiaju, null);

        tv_note = (TextView) viewJiaju.findViewById(R.id.tv_note);

        mJiajuType2Adapter = new JiajuType2Adapter(mContext, new JiajuType2Adapter.Type2Listener() {
            @Override
            public void onItemSelected(TypeBean.SubsBean type2) {
                mJiajuType3Adapter.setData(type2.getSubs());
                mJiajuType3Adapter.notifyDataSetChanged();
            }
        });
        mJiajuType3Adapter = new JiajuType3Adapter(mContext, new JiajuType3Adapter.Type3Listener() {
            @Override
            public void onItemSelected(TypeBean.SubsBean type3) {
                //跳转到详情页
                ProductListWithShaixuanActivity.startMe(mContext, type3.getName(), type3.getCateid());
            }
        });
        mRecyclerViewJiajuType2 = (RecyclerView) viewJiaju.findViewById(R.id.rv_type2s);
        mRecyclerViewJiajuType2.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewJiajuType2.setAdapter(mJiajuType2Adapter);

        mRecyclerViewJiajuType3 = (RecyclerView) viewJiaju.findViewById(R.id.rv_type3s);
        mRecyclerViewJiajuType3.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerViewJiajuType3.setAdapter(mJiajuType3Adapter);

        //添加页卡视图

        mViewList = new ArrayList<>();
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mViewList.add(viewLaidian);
        mViewList.add(viewDiancan);
        mViewList.add(viewJiaju);
        adapter = new TabFragmentAdapter(mViewList, mContext);
        mViewpager.setAdapter(adapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewpager);//将TabLayout和ViewPager关联起来。


        //把来点和家具隐藏

        mTabLayout.setVisibility(View.GONE);
        mViewpager.setCurrentItem(1);
        mTabLayout.removeTab(mTabLayout.getTabAt(2));
        mTabLayout.removeTab(mTabLayout.getTabAt(0));

    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        configMap = HclzApplication.getData();
        if (((MainActivity) mContext).getCurrentVisibleFragment() == MainActivity.CURRENT_HWJP) {
            showLoadingDialog();
        }
        String user_type = SharedPreferencesUtil.get(mContext, "user_type");
        if ("buser".equals(user_type) || "euser".equals(user_type)) {
            getTypesB2b();
        } else {
            getTypes();
        }
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

    private void getTypes() {
        requestParams = new HashMap<>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
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
            mTypeBeanDiancan = null;
            mTypeBeanLaidian = null;
            mTypeBeanJiaju = null;
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.PRODUCT_TYPES.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            JsonObject obj1 = (JsonObject) obj.get("categories");
                            mTypeBeanDiancan = JsonUtility.fromJson("{\"subs\":" + obj1.get("diancan") + "}", TypeBean.class);
                            mTypeBeanLaidian = JsonUtility.fromJson("{\"subs\":" + obj1.get("laidian") + "}", TypeBean.class);
                            mTypeBeanJiaju = JsonUtility.fromJson("{\"subs\":" + obj1.get("jiaju") + "}", TypeBean.class);
                            JsonObject obj2 = (JsonObject) obj.get("note");
                            mNote = JsonUtility.getAsString(obj2.get("note_jiaju"));
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getTypesB2b() {
        requestParams = new HashMap<>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
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

            mTypeBeanDiancan = null;
            mTypeBeanLaidian = null;
            mTypeBeanJiaju = null;
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.PRODUCT_TYPES_B2B.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            JsonObject obj1 = (JsonObject) obj.get("categories");
                            mTypeBeanDiancan = JsonUtility.fromJson("{\"subs\":" + obj1.get("diancan") + "}", TypeBean.class);
                            mTypeBeanLaidian = JsonUtility.fromJson("{\"subs\":" + obj1.get("laidian") + "}", TypeBean.class);
                            mTypeBeanJiaju = JsonUtility.fromJson("{\"subs\":" + obj1.get("jiaju") + "}", TypeBean.class);
                            JsonObject obj2 = (JsonObject) obj.get("note");
                            mNote = JsonUtility.getAsString(obj2.get("note_jiaju"));
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    private void showContent() {
        showNumber();
        mTabLayout.setupWithViewPager(mViewpager);

        diancanTypeAdapter.setData(mTypeBeanDiancan == null ? null : mTypeBeanDiancan.getSubs());
//        pinpaiAdapter.setData(mTypeBeanLaidian.getSubs());
        laidianAdapter.setDatas(mTypeBeanLaidian.getSubs());
        mJiajuType2Adapter.setData(mTypeBeanJiaju == null ? null : mTypeBeanJiaju.getSubs());
        mJiajuType2Adapter.notifyDataSetChanged();
        mJiajuType3Adapter.setData(mTypeBeanJiaju == null ? null :
                (mTypeBeanJiaju.getSubs() == null || mTypeBeanJiaju.getSubs().size() == 0) ? null : mTypeBeanJiaju.getSubs().get(0).getSubs());
        mJiajuType3Adapter.notifyDataSetChanged();
        if (!TextUtils.isEmpty(mNote)) {
            tv_note.setText(mNote);
        }
        dissmissDialog();
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }

        String user_type = SharedPreferencesUtil.get(mContext, "user_type");
//        if (("buser".equals(user_type)||"euser".equals(user_type) )&& mTabLayout.getTabCount() == 3) {
//            mTabLayout.getTabAt(mTabLayout.getTabCount() == 1? 0:1).setText("B2B");
//            mTabLayout.setVisibility(View.GONE);
//            mViewpager.setCurrentItem(1);
//            mTabLayout.removeTab(mTabLayout.getTabAt(2));
//            mTabLayout.removeTab(mTabLayout.getTabAt(0));
//        } else {
//            mTabLayout.setVisibility(View.VISIBLE);
//        }
        //把来点和家具隐藏


    }

    @Override
    protected void setViewData() {

    }

    private void showLoadingDialog() {
        if (mSwipe.isRefreshing()) {
            return;
        }
        WaitingDialogControll.showLoadingDialog(mContext);
        handler.sendEmptyMessageDelayed(8888, 20000);
    }

    private void dissmissDialog() {
        handler.sendEmptyMessageDelayed(8888, 1000);
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
}
