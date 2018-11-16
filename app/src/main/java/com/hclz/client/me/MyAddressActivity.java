package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import com.hclz.client.faxian.ProductListWithPaixuActivity;
import com.hclz.client.faxian.ProductListWithShaixuanActivity;
import com.hclz.client.faxian.ProductListWithTitlePicActivity;
import com.hclz.client.faxian.products.AddressIns;
import com.hclz.client.me.adapter.AddressAdapter;
import com.hclz.client.order.confirmorder.ConfirmOrder2Activity;
import com.hclz.client.order.confirmorder.bean.address.NetAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAddressActivity extends BaseActivity implements OnClickListener {

    private ListView lvAddress;
    private LinearLayout llAdd;

    private AddressAdapter mAdapter;

    private String defaultAddressId;

    private String fromActivity;
    private List<NetAddress> addresses;

    /**
     * 页面跳转
     *
     * @param from
     * @param fromActivity 来自那个activity
     */
    public static void startMe(Context from, String fromActivity) {
        Intent intent = new Intent(from, MyAddressActivity.class);
        intent.putExtra("from", fromActivity);
        from.startActivity(intent);
    }

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from) {
        Intent intent = new Intent(from, MyAddressActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_my_address);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAddress();
    }

    @Override
    protected void initView() {
        lvAddress = (ListView) findViewById(R.id.lv_address);
        llAdd = (LinearLayout) findViewById(R.id.ll_add);
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.my_address);
        getAllAddress();
        if (mIntent != null) {
            fromActivity = mIntent.getStringExtra("from");
        }
    }

    @Override
    protected void initListener() {
        llAdd.setOnClickListener(this);
        lvAddress.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                AddressIns.getInstance().setAddress(addresses.get(arg2));
                if (!TextUtils.isEmpty(fromActivity)) {
                    if (fromActivity.equals(ConfirmOrder2Activity.class.getName())) {
                        Intent intent = new Intent(MyAddressActivity.this,
                                ConfirmOrder2Activity.class);
                        setResult(100, intent);
                        finish();
                    } else if (fromActivity.equals(ProductListWithPaixuActivity.class.getName())) {
                        Intent intent = new Intent(MyAddressActivity.this,
                                ProductListWithPaixuActivity.class);
                        setResult(100, intent);
                        finish();
                    } else if (fromActivity.equals(ProductListWithShaixuanActivity.class.getName())) {
                        Intent intent = new Intent(MyAddressActivity.this,
                                ProductListWithShaixuanActivity.class);
                        setResult(100, intent);
                        finish();
                    } else if (fromActivity.equals(ProductListWithTitlePicActivity.class.getName())) {
                        Intent intent = new Intent(MyAddressActivity.this,
                                ProductListWithTitlePicActivity.class);
                        intent.putExtra("address", addresses.get(arg2));
                        setResult(100, intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add:
                AddressActivity.startMe(mContext, null);
                break;
            default:
                break;
        }
    }

    private void getAllAddress() {
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
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.USER_ADDRESS_LIST.getUserMethod(),
                    requestParams, new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            if (obj != null) {
                                //获取地址
                                addresses = JsonUtility.fromJson(obj.get("addresses"), new TypeToken<List<NetAddress>>() {
                                });
                                for (NetAddress netAddress : addresses) {
                                    if ("1".equals(netAddress.addressid)) {
                                        defaultAddressId = netAddress.addressid;
                                    }
                                }
                            }
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected void showContent() {
        mAdapter = new AddressAdapter(mContext, (ArrayList<NetAddress>) addresses);
        mAdapter.setEmptyString(R.string.addresses_empty);
        lvAddress.setAdapter(mAdapter);
    }

}
