package com.hclz.client.faxian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.hclz.client.base.view.OutStockDialog;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.faxian.adapter.SearchHistoryListViewAdapter;
import com.hclz.client.faxian.view.WordWrapView;
import com.hclz.client.me.MyAddressActivity;
import com.hclz.client.order.confirmorder.ConfirmOrder2Activity;
import com.hclz.client.order.confirmorder.bean.products.NetProduct;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.newcart.DiandiCartItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by hjm on 16/8/16.
 */

public class ProductSearchActivity extends BaseActivity {

    private EditText mSearchEdt;
    private TextView mSearchBtnTv, mHistoryEmptyTv, mClearTv;
    private ArrayList<String> data;
    private WordWrapView mWordWrapView;
    private ImageView mBackIv;
    private ListView mListView;
    private SearchHistoryListViewAdapter adapter;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from) {
        Intent intent = new Intent(from, ProductSearchActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_product_search);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSearchEdt.setText("");
        initData();
    }

    @Override
    protected void initView() {
        mSearchBtnTv = (TextView) findViewById(R.id.tv_btnsearch);
        mSearchEdt = (EditText) findViewById(R.id.et_search);
        mHistoryEmptyTv = (TextView) findViewById(R.id.search_history_empty);
        mClearTv = (TextView) findViewById(R.id.search_clear);
        mWordWrapView = (WordWrapView) findViewById(R.id.word_wrap);
        mBackIv = (ImageView) findViewById(R.id.search_back_iv);
        mListView = (ListView) findViewById(R.id.history_list);
        adapter = new SearchHistoryListViewAdapter(mContext, new SearchHistoryListViewAdapter.SearchHistoryListener() {
            @Override
            public void onDeleteClick(int position) {

                data.remove(position);
                adapter.notifyDataSetChanged();

                String reData="";
//                StringBuilder sb = new StringBuilder();//此处用sb有bug，会越界
                for (String str : data) {
                    reData+=str+",";
//                    sb.append(",").append(str);
                }
                SharedPreferencesUtil.save(mContext, "searchHistory", reData.substring(0,reData.length()-1));
            }

            @Override
            public void onSelected(int position, String tag) {
                mSearchEdt.setText(tag);
            }
        });
        mListView.setAdapter(adapter);
    }

    @Override
    protected void initInstance() {
        configMap = HclzApplication.getData();
    }

    @Override
    protected void initData() {
        WaitingDialogControll.showLoadingDialog(mContext);
        getHotSearchKey();
    }

    List<String> mTuijian = new ArrayList<>();

    private void getHotSearchKey(){

        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.HOT_SEARCH_KEY.getOrderMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            List<String> tuijian = JsonUtility.fromJson(obj.get("keys"), new TypeToken<List<String>>() {
                            });
                            if (tuijian != null){
                                mTuijian = tuijian;
                            }
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            WaitingDialogControll.dismissLoadingDialog();
            throw new RuntimeException(e);
        }
    }

    private void showContent() {
        //获取历史纪录
        String searchHistory = SharedPreferencesUtil.get(mContext, "searchHistory");
        //判断历史纪录是否有,有则显示清空按钮,否则隐藏清空按钮
        if (TextUtils.isEmpty(searchHistory)) {
            mClearTv.setVisibility(View.GONE);
            mHistoryEmptyTv.setVisibility(View.VISIBLE);
            data = new ArrayList<>();
        } else {
            mClearTv.setVisibility(View.VISIBLE);
            mHistoryEmptyTv.setVisibility(View.GONE);
            //展示历史纪录
            String[] historyData = searchHistory.split(",");
            ArrayList<String> list = new ArrayList<>(Arrays.asList(historyData));
            if (list.size() > 20) {
                data = new ArrayList<>(list.subList(0, 20));
            } else {
                data = new ArrayList<String>(list);
            }
        }
        adapter.setMapList(data);

        showTuijian();
        WaitingDialogControll.dismissLoadingDialog();
    }

    private void showTuijian(){
        mWordWrapView.removeAllViews();
        int count = 0;
        for (String search : mTuijian) {
            if (count >= 8){
                return;
            }
            count ++;
            final TextView textview = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_search, null);
            textview.setText(search);
            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchEdt.setText(textview.getText());
                }
            });
            mWordWrapView.addView(textview);
        }
    }

    @Override
    protected void initListener() {
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
                return true;
            }
        });
        mSearchBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        mClearTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空历史纪录
                SharedPreferencesUtil.save(mContext, "searchHistory", "");
                adapter.setMapList(new ArrayList<String>());
                mClearTv.setVisibility(View.GONE);
                mHistoryEmptyTv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void search() {
        String keyword = mSearchEdt.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            ToastUtil.showToast(mContext, "请输入关键字");
        } else {
            addHistory(keyword);
        }
    }

    private void addHistory(String keyword) {
        //增加历史纪录
//        String history = (TextUtils.isEmpty(SharedPreferencesUtil.get(mContext, "searchHistory")) ? "" : SharedPreferencesUtil.get(mContext, "searchHistory"));
//        String[] historyData = history.split(",");
//        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(historyData));
//        if (arrayList.contains(keyword)) {
//            arrayList.remove(keyword);
//        }
//        StringBuilder sb = new StringBuilder();
//        for (String str : arrayList) {
//            sb.append(",").append(str);
//        }
//        String searchHistory = keyword + sb;
//        SharedPreferencesUtil.save(mContext, "searchHistory", searchHistory);



        String history = (TextUtils.isEmpty(SharedPreferencesUtil.get(mContext, "searchHistory")) ? "" : SharedPreferencesUtil.get(mContext, "searchHistory"))+",";
        history=(history.indexOf(keyword)>-1)?keyword+","+history.replace(keyword+",",""):keyword+","+history;
        SharedPreferencesUtil.save(mContext, "searchHistory", history);



        ProductListWithPaixuActivity.startMe(mContext, keyword, keyword, 2);
    }
}
