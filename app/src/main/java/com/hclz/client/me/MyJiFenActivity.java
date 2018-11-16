package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask.ResultHandler;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class MyJiFenActivity extends BaseActivity implements OnClickListener {

    private TextView tvJifen;
    private int amount = 0;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, MyJiFenActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_my_jifen);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        tvJifen = (TextView) findViewById(R.id.tv_jifen);
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.my_jifen);
        getJiFen();
    }

    private void getJiFen() {
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID,
                    SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.ASSETS_USER.getAssetsMethod(),
                    requestParams,
                    new ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            JsonObject obj1 = obj.get("basic").getAsJsonObject();
                            JsonObject obj2 = null;
                            if (obj1 != null && obj1.get("points") != null) {
                                obj2 = obj1.get("points").getAsJsonObject();
                            }
                            if (obj2 != null) {
                                if (obj2.get("amount") != null && !(obj2.get("amount") instanceof JsonNull)) {
                                    amount = obj2.get("amount").getAsInt();
                                }
                            }
                            tvJifen.setText(getString(R.string.jifen_num, amount));
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View v) {

    }

}
