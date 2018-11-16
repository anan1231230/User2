package com.hclz.client.forcshop.jiedanguanli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.hclz.client.forcshop.jiedandetail.JiedanDetailActivity;
import com.hclz.client.forcshop.jiedanguanli.adapter.JiedanGuanliAdapter;
import com.hclz.client.forcshop.jiedanguanli.bean.JiedanguanliBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hjm on 16/8/25.
 */

public class JiedanHistoryActivity extends BaseActivity {

    ArrayList<JiedanGuanliAdapter.JiedanItem> mData;
    RelativeLayout rl_products;
    SwipeRefreshLayout swipe;
    RecyclerView rv_products;
    TextView tv_empty, txt_comm_head_rght;

    JiedanGuanliAdapter mAdapter;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from) {
        Intent intent = new Intent(from, JiedanHistoryActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_jiedan_history);
        super.onCreate(savedInstanceState);
    }

    public void refresh() {
        initData();
    }

    @Override
    protected void initView() {
        rl_products = (RelativeLayout) findViewById(R.id.rl_products);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.main, R.color.yellow, R.color.main,
                R.color.yellow);
        rv_products = (RecyclerView) findViewById(R.id.rv_products);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_empty.setText(Html.fromHtml("<u>暂无订单</u>"));
        txt_comm_head_rght = (TextView) findViewById(R.id.txt_comm_head_rght);
        setCommonTitle("历史订单");
    }

    @Override
    protected void initInstance() {
        mData = new ArrayList<>();
        final LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new JiedanGuanliAdapter(mContext, new JiedanGuanliAdapter.JiedanListener() {
            @Override
            public void onItemSelected(JiedanGuanliAdapter.JiedanItem item) {
                JiedanguanliBean itemSerialize = (JiedanguanliBean) item;
                JiedanDetailActivity.startMe(mContext, "chengdaiweisong", itemSerialize);
            }

            @Override
            public void onJiedan(JiedanGuanliAdapter.JiedanItem item) {

            }

            @Override
            public void onJieHuo(JiedanGuanliAdapter.JiedanItem item) {

            }

            @Override
            public void onQuehuo(JiedanGuanliAdapter.JiedanItem item) {

            }

            @Override
            public void onSonghuo(JiedanGuanliAdapter.JiedanItem item) {

            }

            @Override
            public void onSongda(JiedanGuanliAdapter.JiedanItem item) {

            }

        }, "finished");
        rv_products.setLayoutManager(manager);
        rv_products.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        getWeijiedan();
    }

    private void getWeijiedan() {
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            sanmiAsyncTask = new SanmiAsyncTask(mContext);
            sanmiAsyncTask.setHandler(handler);

            configMap = HclzApplication.getData();
            content = PostHttpUtil.prepareContents(configMap, mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
            content.put("cid", SharedPreferencesUtil.get(mContext, "cid"));
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.CSHOP_ORDER_CHECK_HISTORY.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            ArrayList<JiedanguanliBean> data = JsonUtility.fromJson(obj.get("orders"),
                                    new TypeToken<ArrayList<JiedanguanliBean>>() {
                                    });
                            mData = new ArrayList<JiedanGuanliAdapter.JiedanItem>();
                            for (JiedanguanliBean da : data) {
                                mData.add(da);
                            }
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showContent() {
        if (mData != null && !mData.isEmpty()) {
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
        tv_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }
}
