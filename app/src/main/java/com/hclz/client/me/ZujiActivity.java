package com.hclz.client.me;        /**
 * Created by handsome on 2016/12/19.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.faxian.ProductDetailActivity;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.me.adapter.ZujiAdapter;

import java.util.ArrayList;


public class ZujiActivity extends BaseAppCompatActivity {

    RecyclerView rv_zuji;
    GridLayoutManager mManager;
    ZujiAdapter mAdapter;
    ArrayList<ProductBean.ProductsBean> mData;

    public static void startMe(Activity context) {
        Intent intent = new Intent(context, ZujiActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_zuji);
    }

    @Override
    protected void initView() {
        rv_zuji = (RecyclerView) findViewById(R.id.rv_zuji);
        setCommonTitle("我的👣");
    }

    @Override
    protected void initInstance() {
        mManager = new GridLayoutManager(mContext, 2);
        mAdapter = new ZujiAdapter(mContext, new ZujiAdapter.ZujiListener() {
            @Override
            public void onItemSelected(int position, ProductBean.ProductsBean item) {
                ProductDetailActivity.startMe(mContext, item);
            }

            @Override
            public void delHistory(final int position, final ProductBean.ProductsBean laidianProduct) {
                //删除该浏览记录
                new AlertDialog.Builder(mContext).setTitle("您确定要删除订单吗?")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String productsString = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_ZUJIS);
                                ArrayList<ProductBean.ProductsBean> products=JsonUtility.fromJson(JsonUtility.parseArray(productsString),new TypeToken<ArrayList<ProductBean.ProductsBean>>(){});
                                for(int i=0;i<products.size();i++){
                                    if(laidianProduct.pid.equals(products.get(i).pid)){
                                        products.remove(i);
                                    }
                                }
                                Gson gson = new Gson();
                                String  zujiString = gson.toJson(products);
                                SharedPreferencesUtil.save(mContext,ProjectConstant.APP_ZUJIS,zujiString);
                                getZujiDatas();

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DO NOTHING
                    }
                }).show();
            }

        });
        rv_zuji.setLayoutManager(mManager);
        rv_zuji.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (mIntent != null) {
            getZujiDatas();
        }

    }

    private void getZujiDatas() {
        String zujiString = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_ZUJIS);
        if (!TextUtils.isEmpty(zujiString)) {
            mData = JsonUtility.fromJson(JsonUtility.parseArray(zujiString), new TypeToken<ArrayList<ProductBean.ProductsBean>>() {
            });

        }
        mAdapter.setData(mData);
    }

    private void showContent() {
        mAdapter.setData(mData);
    }


    @Override
    protected void initListener() {

    }
}