package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask.ResultHandler;
import com.hclz.client.base.bean.MainAccount;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangeUserInfoActivity extends BaseActivity implements OnClickListener {
    private TextView tvCommHeadRight;
    private EditText etNicheng;

    private String niCheng;
    private MainAccount account;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from, MainAccount account){
        Intent intent = new Intent(from, ChangeUserInfoActivity.class);
        intent.putExtra("mainAccount", account);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_change_userinfo);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        tvCommHeadRight = (TextView) findViewById(R.id.txt_comm_head_rght);
        tvCommHeadRight.setVisibility(View.VISIBLE);
        etNicheng = (EditText) findViewById(R.id.et_nicheng);
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.edit_nicheng);
        tvCommHeadRight.setText(getString(R.string.ok));
        if (mIntent != null) {
            account = (MainAccount) mIntent.getSerializableExtra("mainAccount");
        }
    }

    @Override
    protected void initListener() {
        tvCommHeadRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_comm_head_rght:
                niCheng = etNicheng.getText().toString();
                if (TextUtils.isEmpty(niCheng)) {
                    etNicheng.setError(getString(R.string.new_nicheng));
                    return;
                }
                changeUserInfo();
                break;
            default:
                break;
        }
    }

    private void changeUserInfo() {
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
            JSONObject mainAccount = new JSONObject();
            mainAccount.put(ProjectConstant.APP_USER_MID, account.getMid());
            mainAccount.put("nickname", niCheng);
            mainAccount.put("default_addreddid", account.getDefault_addressid());
            content.put("main_account", mainAccount);
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_UPDATE.getUserMethod(), requestParams,
                    new ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            MainAccount mainAccount = JsonUtility.fromJson(obj.get("main_account"), MainAccount.class);
                            SharedPreferencesUtil.save(mContext, ProjectConstant.APP_USER_MID, mainAccount.getMid());
                            SharedPreferencesUtil.save(mContext, ProjectConstant.APP_USER_SESSIONID, mainAccount.getSessionid());
                            finish();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
