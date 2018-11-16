package com.hclz.client.faxian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.hclz.client.faxian.adapter.BrandListAdapter;
import com.hclz.client.faxian.bean.Brand;
import com.hclz.client.faxian.bean.BrandBean;
import com.hclz.client.faxian.bean.TypeBean;
import com.hclz.client.me.StaffDetailActivity;
import com.hclz.client.me.adapter.AddressAdapter;
import com.hclz.client.me.bean.KefuListBean;
import com.hclz.client.order.confirmorder.bean.address.NetAddress;
import com.hclz.client.order.confirmorder.bean.staff.NetStaff;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BrandListActivity extends BaseAppCompatActivity {

    LinearLayout ll_brand_list;
    TextView tv_zjf, tv_hzbc;
    List<Brand> brandList;
    ListView lv_brand_list;
    BrandListAdapter mAdapter;
    public static void startMe(Context from) {
        Intent intent = new Intent(from, BrandListActivity.class);
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
        setContentView(R.layout.activity_brand_list);
    }

    @Override
    protected void initView() {
        ll_brand_list = (LinearLayout) findViewById(R.id.ll_brand_list);
        lv_brand_list= (ListView) findViewById(R.id.lv_brand_list);
        lv_brand_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BrandDetailActivity.startMe(mContext, brandList.get(position));
            }
        });
    }

    private void showContent() {
        mAdapter = new BrandListAdapter(mContext, (ArrayList<Brand>)brandList);
        mAdapter.setEmptyString("暂无加盟信息");
        lv_brand_list.setAdapter(mAdapter);
    }

    @Override
    protected void initInstance() {
        setCommonTitle("品牌特色");
        configMap = HclzApplication.getData();

    }


    @Override
    protected void initData() {
        getBrandList();
    }

    private void getBrandList() {
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
                contentObj.put(ProjectConstant.APPID, mid);
            }
            if (!TextUtils.isEmpty(platform)) {
                contentObj.put(ProjectConstant.PLATFORM, mid);
            }
            if (!TextUtils.isEmpty(appversion)) {
                contentObj.put(ProjectConstant.APP_VERSION, mid);
            }
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.setIsShowDialog(false);

            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.BRAND_LIST.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                             brandList = JsonUtility.fromJson(obj.get("franchisee_list"), new TypeToken<List<Brand>>() {
                            });
                            if(brandList!=null&&brandList.size()>0){
                                for(Brand a:brandList){
                                    Log.d("测试",a.getName());
                                }
                                showContent();
                            }

                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initListener() {
        ll_brand_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandDetailActivity.startMe(mContext);
            }
        });
    }

}
