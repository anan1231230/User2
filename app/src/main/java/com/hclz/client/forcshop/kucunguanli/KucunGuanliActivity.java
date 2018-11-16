package com.hclz.client.forcshop.kucunguanli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.forcshop.kucunguanli.adapter.KucunGuanliAdapter;
import com.hclz.client.forcshop.kucunguanli.bean.KucunGuanliBean;
import com.hclz.client.forcshop.kucunguanli.dialog.KucunGuanliDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by handsome on 16/7/7.
 */
public class KucunGuanliActivity extends BaseActivity {

    RelativeLayout rl_products;
    SwipeRefreshLayout swipe;
    RecyclerView rv_products;
    TextView tv_empty,txt_comm_head_rght;

    LinearLayoutManager mManager;
    KucunGuanliAdapter mAdapter;
    ArrayList<KucunGuanliAdapter.KucunItem> mData;
    Map<String,KucunGuanliAdapter.KucunItem> mDeltaMap;
    private KucunGuanliDialog mDialog;

    /**
     * 页面跳转,不需要产参数
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, KucunGuanliActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_kucunguanli);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        rl_products = (RelativeLayout) findViewById(R.id.rl_products);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.main, R.color.yellow, R.color.main,
                R.color.yellow);
        rv_products = (RecyclerView) findViewById(R.id.rv_products);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        txt_comm_head_rght = (TextView) findViewById(R.id.txt_comm_head_rght);
        setCommonTitle("库存管理");
        txt_comm_head_rght.setText("确认提交");
    }

    @Override
    protected void initInstance() {
        mDeltaMap = new HashMap<>();
        mData = new ArrayList<>();
        mDialog = new KucunGuanliDialog(mContext, new KucunGuanliDialog.KucunDialogListener() {
            @Override
            public void onConfirm(KucunGuanliAdapter.KucunItem item) {
                int position = getProductPosition(item.getPid());
                if (position>=0){
                    mData.get(position).setDelta(item.getDelta());
                    mData.get(position).setReason(item.getReason());
                    mAdapter.notifyItemChanged(position);
                }

                //用于暂存数据
                if (item.getDelta() >= 0){
                    if (mDeltaMap.containsKey(item.getPid())){
                        mDeltaMap.remove(item.getPid());
                    }
                } else {
                    mDeltaMap.put(item.getPid(),item);
                }
                if (mDeltaMap.size() <= 0){
                    txt_comm_head_rght.setVisibility(View.GONE);
                } else {
                    txt_comm_head_rght.setVisibility(View.VISIBLE);
                }
            }
        });
        mManager = new LinearLayoutManager(mContext);
        mAdapter = new KucunGuanliAdapter(mContext, new KucunGuanliAdapter.KucunListener() {
            @Override
            public void onItemSelected(View v,KucunGuanliAdapter.KucunItem item) {
                mDialog.setItem(item);
                mDialog.showAsDropDown(v);
            }
        });
        rv_products.setLayoutManager(mManager);
        rv_products.setAdapter(mAdapter);
    }

    public int getProductPosition(String pid) {
        int position = 0;
        for (KucunGuanliAdapter.KucunItem product : mData) {
            if (product.getPid().equals(pid == null ? "" : pid)) {
                return position;
            }
            position += 1;
        }
        return -1;
    }

    @Override
    protected void initData() {
        getKucun();
    }

    private void getKucun(){
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
            PostHttpUtil.prepareParams(requestParams, content.toString());
            if (swipe.isRefreshing()){
                sanmiAsyncTask.setIsShowDialog(false);
            }
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.CSHOP_INVENTORY_QUERY.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mDeltaMap.clear();
                            mData.clear();
                            txt_comm_head_rght.setVisibility(View.GONE);
                            ArrayList<KucunGuanliBean> data = JsonUtility.fromJson(obj.get("inventory"),
                                    new TypeToken<ArrayList<KucunGuanliBean>>() {
                                    });
                            mData = new ArrayList<KucunGuanliAdapter.KucunItem>();
                            for (KucunGuanliBean da:data){
                                if (da.getKucunliang() > 0){
                                    mData.add(da);
                                }
                            }
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateKucun(){
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

            JSONArray inventory_delta = new JSONArray();
            Iterator iter = mDeltaMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    KucunGuanliAdapter.KucunItem item = (KucunGuanliAdapter.KucunItem) entry.getValue();
                    JSONObject obj = new JSONObject();
                    obj.put("pid",item.getPid());
                    obj.put("delta",item.getDelta());
                    obj.put("desc",item.getReason());
                    inventory_delta.put(obj);
                    }
            content.put("inventory_delta",inventory_delta);
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.CSHOP_INVENTORY_ADJUST.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ToastUtil.showToast(mContext,"上传成功");
                            mDeltaMap.clear();
                            mData.clear();
                            txt_comm_head_rght.setVisibility(View.GONE);
                            JsonObject obj = JsonUtility.parse(result);
                            ArrayList<KucunGuanliBean> data = JsonUtility.fromJson(obj.get("inventory"),
                                    new TypeToken<ArrayList<KucunGuanliBean>>() {
                                    });
                            mData = new ArrayList<KucunGuanliAdapter.KucunItem>();
                            for (KucunGuanliBean da:data){
                                if (da.getKucunliang() > 0){
                                    mData.add(da);
                                }
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
    }

    @Override
    protected void initListener() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        txt_comm_head_rght.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateKucun();
            }
        });
    }
}
