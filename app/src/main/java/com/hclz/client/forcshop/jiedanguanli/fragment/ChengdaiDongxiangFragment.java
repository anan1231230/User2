package com.hclz.client.forcshop.jiedanguanli.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseVFragment;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.forcshop.jiedandetail.JiedanDetailActivity;
import com.hclz.client.forcshop.jiedanguanli.JiedanGuanliActivity;
import com.hclz.client.forcshop.jiedanguanli.adapter.JiedanGuanliAdapter;
import com.hclz.client.forcshop.jiedanguanli.bean.BadgeBean;
import com.hclz.client.forcshop.jiedanguanli.bean.JiedanguanliBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by handsome on 16/7/7.
 */
public class ChengdaiDongxiangFragment extends BaseVFragment {

    ArrayList<JiedanGuanliAdapter.JiedanItem> mData;
    View viWholeView;
    RelativeLayout rl_products;
    SwipeRefreshLayout swipe;
    RecyclerView rv_products;
    TextView tv_empty,txt_comm_head_rght;

    JiedanGuanliAdapter mAdapter;

    public static ChengdaiDongxiangFragment newInstance() {
        ChengdaiDongxiangFragment fragment = new ChengdaiDongxiangFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viWholeView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_jiedanguanli, container, false);
        return viWholeView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void refresh(){
        initData();
    }

    @Override
    protected void initView() {
        rl_products = (RelativeLayout) viWholeView.findViewById(R.id.rl_products);
        swipe = (SwipeRefreshLayout) viWholeView.findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.main, R.color.yellow, R.color.main,
                R.color.yellow);
        rv_products = (RecyclerView) viWholeView.findViewById(R.id.rv_products);
        tv_empty = (TextView) viWholeView.findViewById(R.id.tv_empty);
        tv_empty.setText(Html.fromHtml("<u>暂无订单</u>"));
        txt_comm_head_rght = (TextView) viWholeView.findViewById(R.id.txt_comm_head_rght);
    }

    @Override
    protected void initInstance() {
        mData = new ArrayList<>();
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new JiedanGuanliAdapter(getActivity(), new JiedanGuanliAdapter.JiedanListener() {
            @Override
            public void onItemSelected(JiedanGuanliAdapter.JiedanItem item) {
                JiedanguanliBean itemSerialize = (JiedanguanliBean) item;
                JiedanDetailActivity.startMe(mContext,"chengdaiweisong",itemSerialize);
            }

            @Override
            public void onJiedan(final JiedanGuanliAdapter.JiedanItem item) {
                new AlertDialog.Builder(mContext).setTitle("您确认接单吗！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                opRequest("jiedan",item);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }

            @Override
            public void onJieHuo(final JiedanGuanliAdapter.JiedanItem item) {
                new AlertDialog.Builder(mContext).setTitle("您确认接货吗！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                opRequest("jiehuo",item);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }

            @Override
            public void onQuehuo(final JiedanGuanliAdapter.JiedanItem item) {
                new AlertDialog.Builder(mContext).setTitle("您确认缺货吗！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                opRequest("quehuo",item);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }

            @Override
            public void onSonghuo(final JiedanGuanliAdapter.JiedanItem item) {
                new AlertDialog.Builder(mContext).setTitle("您确认送货吗！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                opRequest("songhuo",item);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

            }

            @Override
            public void onSongda(final JiedanGuanliAdapter.JiedanItem item) {
                new AlertDialog.Builder(mContext).setTitle("您确认送达吗！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                opRequest("songda",item);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        },"chengdaidongxiang");
        rv_products.setLayoutManager(manager);
        rv_products.setAdapter(mAdapter);
    }

    private void opRequest(String op,JiedanGuanliAdapter.JiedanItem item){

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
            mContext = this.getActivity();
            sanmiAsyncTask = new SanmiAsyncTask(mContext);
            sanmiAsyncTask.setHandler(handler);

            configMap = HclzApplication.getData();
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(getActivity(), ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(getActivity(),
                            ProjectConstant.APP_USER_SESSIONID));
            content.put("cid",SharedPreferencesUtil.get(mContext,"cid"));
            content.put("orderid",item.getOrderId());
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.setIsShowDialog(true);
            sanmiAsyncTask.excutePosetRequest(url, requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ((JiedanGuanliActivity)mContext).refresh();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initData() {
        getWeijiedan();
    }

    private void getWeijiedan(){
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            mContext = this.getActivity();
            sanmiAsyncTask = new SanmiAsyncTask(mContext);
            sanmiAsyncTask.setHandler(handler);

            configMap = HclzApplication.getData();
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(getActivity(), ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(getActivity(),
                            ProjectConstant.APP_USER_SESSIONID));
            content.put("cid",SharedPreferencesUtil.get(mContext,"cid"));
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.CSHOP_ORDER_CHECK_UNDELIVERED.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            ArrayList<JiedanguanliBean> data = JsonUtility.fromJson(obj.get("orders"),
                                    new TypeToken<ArrayList<JiedanguanliBean>>() {
                            });
                            mData = new ArrayList<JiedanGuanliAdapter.JiedanItem>();
                            for (JiedanguanliBean da:data){
                                mData.add(da);
                            }
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showContent(){
        if (mData != null && !mData.isEmpty()){
            tv_empty.setVisibility(View.GONE);
            rl_products.setVisibility(View.VISIBLE);
            mAdapter.setData(mData);
            mAdapter.notifyDataSetChanged();
        } else {
            tv_empty.setVisibility(View.VISIBLE);
            rl_products.setVisibility(View.GONE);
        }
        swipe.setRefreshing(false);
        BadgeBean.getInstence().badges[0] = mData.size()+"";
        ((JiedanGuanliActivity)mContext).setBadge();
    }

    @Override
    protected void setViewData() {

    }

    @Override
    protected void setListener() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        tv_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }
}
