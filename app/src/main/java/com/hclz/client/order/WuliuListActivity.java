package com.hclz.client.order;        /**
 * Created by handsome on 2016/12/27.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hclz.client.R;
import com.hclz.client.base.bean.Order;
import com.hclz.client.base.bean.StatusDetail;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.order.adapter.WuliuListAdapter;

import java.util.ArrayList;

public class WuliuListActivity extends BaseAppCompatActivity {

    RecyclerView rv_wuliulist;
    LinearLayoutManager mManager;
    WuliuListAdapter mAdapter;
    ArrayList<Order.StatusDetailBean> mData;

    public static void startMe(Activity context,Order order) {
        Intent intent = new Intent(context, WuliuListActivity.class);
        intent.putExtra("order",order);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wuliulist);
    }

    @Override
    protected void initView() {
        rv_wuliulist = (RecyclerView) findViewById(R.id.rv_wuliulist);
        setCommonTitle("订单跟踪");
    }

    @Override
    protected void initInstance() {
        mManager = new LinearLayoutManager(mContext);
        mAdapter = new WuliuListAdapter(mContext, new WuliuListAdapter.WuliuListListener() {
            @Override
            public void onItemSelected(int position, Order.StatusDetailBean item) {
                //TODO
            }
        });
        rv_wuliulist.setLayoutManager(mManager);
        rv_wuliulist.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (mIntent != null) {
            Order order = (Order) mIntent.getSerializableExtra("order");
            if (order != null){
                mData = new ArrayList<>();
                for (Order.StatusDetailBean detail:order.status_detail){
                    mData.add(0,detail);
                }
                showContent();
            }
        }

    }

    private void showContent() {
        mAdapter.setData(mData);
    }


    @Override
    protected void initListener() {

    }
}