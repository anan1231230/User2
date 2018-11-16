package com.hclz.client.me;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.location.LocationUtils;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.me.adapter.SelectHehuorenAdapter;
import com.hclz.client.order.confirmorder.ConfirmOrder2Activity;
import com.hclz.client.order.confirmorder.bean.hehuoren.NetHehuoren;

import java.util.ArrayList;


/**
 * Created by handsome on 16/6/21.
 */
public class SelectHehuorenActivity extends BaseActivity {

    TextView tv_empty;
    ImageView iv_back;
    EditText et_search;
    RecyclerView rv_hehuoren;
    LinearLayoutManager mLayoutManager;
    SelectHehuorenAdapter mAdapter;
    SelectHehuorenAdapter.HehuorenSelectListener mListener;
    NetHehuoren mKitchenSelected;
    ArrayList<NetHehuoren> mKitchens;
    private boolean isFromCreate = false;

    private static final int REQUEST_CODE = 100;
    private static final int CAMERA_CODE = 99;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from) {
        Intent intent = new Intent(from, SelectHehuorenActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_select_hehuoren);
        isFromCreate = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFromCreate) {
            initData();
        } else {
            isFromCreate = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationUtils.getInstence().stop();
    }

    @Override
    protected void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_search = (EditText) findViewById(R.id.et_search);
        rv_hehuoren = (RecyclerView) findViewById(R.id.rv_hehuoren);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
    }

    @Override
    protected void initInstance() {
        configMap = HclzApplication.getData();
        mLayoutManager = new LinearLayoutManager(mContext);
        mListener = new SelectHehuorenAdapter.HehuorenSelectListener() {
            @Override
            public void onHehuorenSelected(final NetHehuoren item) {
                new AlertDialog.Builder(mContext).setTitle("您确定要换配送人吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mKitchenSelected = item;
                                Intent intent = new Intent(SelectHehuorenActivity.this, ConfirmOrder2Activity.class);
                                intent.putExtra("kitchen", mKitchenSelected);
                                setResult(400, intent);
                                finish();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DO NOTHING
                    }
                }).show();
            }
        };
        mAdapter = new SelectHehuorenAdapter(mContext, mListener);
        rv_hehuoren.setLayoutManager(mLayoutManager);
        rv_hehuoren.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        getHehuoren();
    }

    private void getHehuoren() {
        Intent intent = getIntent();
        mKitchens = (ArrayList<NetHehuoren>) intent.getSerializableExtra("kitchens");
        showContent();
    }

    private void showContent() {
        if (mKitchens == null || mKitchens.size() <= 0) {
            tv_empty.setVisibility(View.VISIBLE);
        } else {
            tv_empty.setVisibility(View.GONE);
        }
        mAdapter.setData(mKitchens);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
                return true;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void search() {
        String keyword = et_search.getText().toString().trim();
        if (TextUtils.isEmpty(keyword)) {
            mAdapter.setData(mKitchens);
        } else {
            ArrayList<NetHehuoren> tmp = new ArrayList<>();
            if (!mKitchens.isEmpty()) {
                for (NetHehuoren netHehuoren : mKitchens) {
                    if (netHehuoren.title.contains(keyword)) {
                        tmp.add(netHehuoren);
                    } else if (netHehuoren.phone.contains(keyword)) {
                        tmp.add(netHehuoren);
                    } else if (netHehuoren.contact.contains(keyword)) {
                        tmp.add(netHehuoren);
                    }
                }
            }
            mAdapter.setData(tmp);
        }
        mAdapter.notifyDataSetChanged();
    }
}
