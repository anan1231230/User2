package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.Bill;
import com.hclz.client.base.bean.Chongzhika;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.me.adapter.ChongzhiAdapter;
import com.hclz.client.me.listener.ChongzhiSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by handsome on 16/4/26.
 */
public class ChongzhiActivity extends BaseActivity {

    StaggeredGridLayoutManager mManager;
    private RecyclerView rvChongzhi;
    private ChongzhiAdapter mAdapter;
    private ArrayList<Chongzhika> mChongzhikas;
    private TextView txt_comm_head_rght;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, ChongzhiActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_chongzhi);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setCommonTitle("充值");
        rvChongzhi = (RecyclerView) findViewById(R.id.rv_chognzhi);
        txt_comm_head_rght = (TextView) findViewById(R.id.txt_comm_head_rght);
        txt_comm_head_rght.setText("输入金额");
        txt_comm_head_rght.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initInstance() {
        mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new ChongzhiAdapter(mContext, new ChongzhiSelectedListener() {
            @Override
            public void onDaxueSelected(Chongzhika content) {
                applyBill(content.getCardid());
            }
        });
        rvChongzhi.setLayoutManager(mManager);
        rvChongzhi.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        getChongzhikas();
    }

    private void getChongzhikas() {
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
            JSONArray array = new JSONArray();
            array.put("balancecard");
            content.put("cardtypes", array);
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.ASSETS_USER_CARDS_QUERY.getAssetsMethod(),
                    requestParams, new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            if (obj != null) {
                                mChongzhikas = JsonUtility.fromJson(
                                        obj.get("balancecards"),
                                        new TypeToken<ArrayList<Chongzhika>>() {
                                        });
                            }
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提交订单：1：资产购买订单提交：
     */
    private void applyBill(String cardid) {
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
            JSONObject bill = new JSONObject();
            bill.put("billtype", "buycard");
            bill.put("cardtype", "balancecard");
            bill.put("cardid", cardid);

            contentObj.put("bill", bill);

            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.ASSETS_USER_BILLS_BUYCARD.getAssetsMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            Bill bill = JsonUtility.fromJson(obj.get("bill"), Bill.class);
                            SelectPayMethod3Activity.startMe(mContext,bill.getBillid(),bill.getPrice(),SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
                            finish();
                        }

                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showContent() {
        mAdapter.setData(mChongzhikas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        txt_comm_head_rght.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChongzhiRenyiActivity.startMe(mContext);
            }
        });
    }
}
