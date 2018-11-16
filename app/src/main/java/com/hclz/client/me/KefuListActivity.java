package com.hclz.client.me;        /**
 * Created by handsome on 2016/12/20.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.me.adapter.KefuListAdapter;
import com.hclz.client.me.bean.KefuListBean;
import com.hyphenate.easeui.EaseConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class KefuListActivity extends BaseAppCompatActivity {

    LinearLayoutManager mManager;
    KefuListAdapter mAdapter;
    ArrayList<KefuListBean> mData;
    private RecyclerView rv_kefulist;
    private SwipeRefreshLayout swipe;

    public static void startMe(Activity context) {
        Intent intent = new Intent(context, KefuListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_kefulist);
    }

    @Override
    protected void initView() {
        rv_kefulist = (RecyclerView) findViewById(R.id.rv_kefulist);
        setCommonTitle("客服列表");
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
    }

    @Override
    protected void initInstance() {
        mManager = new LinearLayoutManager(mContext);
        mAdapter = new KefuListAdapter(mContext, new KefuListAdapter.KefuListListener() {
            @Override
            public void onItemSelected(int position, KefuListBean item) {
                KefuActivity.startMe(mContext, item.id, EaseConstant.CHATTYPE_SINGLE);
            }
        });
        rv_kefulist.setLayoutManager(mManager);
        rv_kefulist.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        getKefuListDatas();
    }

    private void getKefuListDatas() {
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
            content = PostHttpUtil.prepareContents(configMap, mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.HCLZ_IM_KEFU.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mData = JsonUtility.fromJson(obj.get("kefus"),
                                    new TypeToken<ArrayList<KefuListBean>>() {
                                    });
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showContent() {
        mAdapter.setData(mData);
        swipe.setRefreshing(false);
    }


    @Override
    protected void initListener() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getKefuListDatas();
            }
        });
    }
}