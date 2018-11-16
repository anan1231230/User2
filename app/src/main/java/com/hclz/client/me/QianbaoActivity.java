package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.me.bean.BasicEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class QianbaoActivity extends BaseAppCompatActivity {

    private Toolbar toolbar;
    private TextView tvYue;
    private Button btnChongzhi;
    private SwipeRefreshLayout mSwipe;
    private BasicEntity basicEntity;

    private boolean isFromCreate = false;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, QianbaoActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromCreate = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFromCreate) {
            isFromCreate = false;
        } else {
            initData();
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_qianbao);
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);

        tvYue = (TextView) findViewById(R.id.tv_yue);
        btnChongzhi = (Button) findViewById(R.id.btn_chognzhi);

        mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_qianbao, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效
    private void setIconEnable(Menu menu, boolean enable) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initInstance() {
        configMap = HclzApplication.getData();
    }

    @Override
    protected void initData() {
        getYue();
    }


    private void getYue() {
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
                    ServerUrlConstant.ASSETS_USER.getAssetsMethod(),
                    requestParams, new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            basicEntity = JsonUtility.fromJson(obj.get("basic"), BasicEntity.class);
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showContent() {
        tvYue.setText(basicEntity.getBalance() == null ? "0.00" : CommonUtil.getMoney(basicEntity.getBalance().getAmount()));
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonBackClick(v);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_zhangdan:
                        QianbaoZhangdanActivity.startMe(mContext);
                        break;
                    case R.id.action_shezhimima:
                        QianbaomimaSettingActivity.startMe(mContext);
                        break;
                }
                return true;
            }
        });
        btnChongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QianbaoActivity.this, ChongzhiActivity.class);
                startActivity(intent);
            }
        });

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }
}
