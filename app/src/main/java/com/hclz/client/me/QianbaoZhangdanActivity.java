package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.Qianbaozhangdan;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.me.adapter.QianbaozhangdanAdapter;
import com.hclz.client.me.listener.QianbaozhangdanCaozuoListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 16/4/26.
 */
public class QianbaoZhangdanActivity extends BaseActivity {

    LinearLayoutManager mManager;
    private RecyclerView rvQianbaozhangdan;
    private QianbaozhangdanAdapter mAdapter;
    private ArrayList<Qianbaozhangdan> mQianbaozhangdan;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, QianbaoZhangdanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_qianbaozhangdan);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setCommonTitle("账单");
        rvQianbaozhangdan = (RecyclerView) findViewById(R.id.rv_qianbaozhangdan);
    }

    @Override
    protected void initInstance() {
        configMap = HclzApplication.getData();
        mManager = new LinearLayoutManager(mContext);
        mAdapter = new QianbaozhangdanAdapter(mContext, new QianbaozhangdanCaozuoListener() {
            @Override
            public void onDaxueSelected(Qianbaozhangdan bill) {
                Intent intent = new Intent(
                        QianbaoZhangdanActivity.this,
                        SelectPayMethod3Activity.class);
                intent.putExtra("billId", bill.getBillid());
                intent.putExtra("payment_amount", bill.getPrice());
                intent.putExtra("zhifu_mid", SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
                startActivity(intent);
                SelectPayMethod3Activity.startMe(mContext,bill.getBillid(),bill.getPrice(),SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
                finish();
            }
        });
        rvQianbaozhangdan.setLayoutManager(mManager);
        rvQianbaozhangdan.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        requestQianbaozhangdan();
    }

    private void requestQianbaozhangdan() {
        JSONObject contentObj = null;
        try {
//            contentObj.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            contentObj.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.ASSETS_USER_BILLS_QUERY.getAssetsMethod(),
                    requestParams, new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mQianbaozhangdan = (ArrayList<Qianbaozhangdan>) JsonUtility.fromJson(obj.get("bills"),
                                    new TypeToken<List<Qianbaozhangdan>>() {
                                    });
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showContent() {
        mAdapter.setData(mQianbaozhangdan);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {

    }
}
