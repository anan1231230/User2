package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.MainAccount;
import com.hclz.client.base.bean.SubAccount;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChageTuijianrenPhoneActivity extends BaseActivity implements View.OnClickListener {

    private TextView mPhoneTv;
    private EditText mPhoneEdt;
    private Button mPhoneBtn;
    private MainAccount mainAccount;
    private ArrayList<SubAccount> subAccounts;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, ChageTuijianrenPhoneActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_chage_tuijianren_phone);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mPhoneTv = (TextView) findViewById(R.id.change_tuiijaren_phone);
        mPhoneEdt = (EditText) findViewById(R.id.change_tuiijaren_edt);
        mPhoneBtn = (Button) findViewById(R.id.change_tuiijaren_button);
    }

    @Override
    protected void initInstance() {
        configMap = HclzApplication.getData();
    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.introducer);
        String introducer = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_INTRODUCER);
        if (TextUtils.isEmpty(introducer) || "\"\"".equals(introducer)) {
            mPhoneTv.setText("您目前尚未设置推荐人");
        } else {
            mPhoneTv.setText("您目前绑定的推荐人是:" + introducer);
        }
    }

    @Override
    protected void initListener() {
        mPhoneBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_tuiijaren_button:
                String introducer = mPhoneEdt.getText().toString().trim();
                if (TextUtils.isEmpty(introducer)) {
                    mPhoneEdt.requestFocus();
                    mPhoneEdt.setError("请输入推荐码或者手机号");
                    return;
                }
                if (!(introducer.length() <= 11)) {
                    mPhoneEdt.requestFocus();
                    mPhoneEdt.setError(getString(R.string.tuijianren_real));
                    return;
                }
                setIntroducer(introducer);
                break;
            default:
                break;
        }
    }

    /**
     * 设置推荐人手机号
     *
     * @param introducer 推荐人手机号
     */
    private void setIntroducer(final String introducer) {
        JSONObject content = null;
        try {
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID,
                    SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
            content.put("introducer", introducer);
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_CHANGE_INTRODUCER.getUserMethod(),
                    requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mainAccount = JsonUtility.fromJson(obj.get("main_account"), MainAccount.class);
                            subAccounts = JsonUtility.fromJson(obj.get("sub_accounts"),
                                    new TypeToken<ArrayList<SubAccount>>() {
                                    });
                            //增加user_type判定,判断是 normal(普通用户) 还是 cshop(合伙人) 还是 dshop(城代)
                            String user_type = JsonUtility.fromJson(obj.get("user_type"), String.class);
                            SharedPreferencesUtil.save(mContext, "user_type", user_type);
                            SharedPreferencesUtil.save(mContext, ProjectConstant.APP_USER_INTRODUCER, obj.get("introducer").toString());
                            mPhoneTv.setText("您目前设置的推荐人手机号或推荐码是:" + obj.get("introducer").toString());
                            mPhoneEdt.setText("");
                            ToastUtil.showToast(mContext, "修改推荐人成功!");
                            SharedPreferencesUtil.saveUserBasic(mContext, mainAccount, subAccounts);
                            finish();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
