package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.MD5;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by handsome on 16/5/12.
 */
public class QianbaomimaSettingActivity extends BaseAppCompatActivity implements View.OnClickListener {


    TextView tv_phonenum, tv_get_verifycode;
    EditText et_verifycode, et_newpwd, et_newpwd2;
    Button btn_next;

    private String phoneNum;
    private String verifyCode;
    private String newPwd;
    private String newPwd2;

    private TimeCount time;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, QianbaomimaSettingActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_qianbao_shezhimima);
    }

    @Override
    protected void initView() {

        tv_phonenum = (TextView) findViewById(R.id.tv_phonenum);
        tv_get_verifycode = (TextView) findViewById(R.id.tv_get_verifycode);
        et_verifycode = (EditText) findViewById(R.id.et_verifycode);
        et_newpwd = (EditText) findViewById(R.id.et_newpwd);
        et_newpwd2 = (EditText) findViewById(R.id.et_newpwd2);
        btn_next = (Button) findViewById(R.id.btn_next);
    }

    @Override
    protected void initInstance() {
        time = new TimeCount(60000, 1000);
    }

    @Override
    protected void initData() {
        setCommonTitle("钱包密码设置");
        if (!StringUtils.isEmpty(SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_PHONE))) {
            tv_phonenum.setText(SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_PHONE));
        }
    }

    @Override
    protected void initListener() {
        tv_get_verifycode.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_verifycode:
                phoneNum = tv_phonenum.getText().toString();
                getVerifyCode();
                break;
            case R.id.btn_next:
                phoneNum = tv_phonenum.getText().toString();
                verifyCode = et_verifycode.getText().toString();
                if (TextUtils.isEmpty(verifyCode)) {
                    et_verifycode.requestFocus();
                    et_verifycode.setError(getString(R.string.verifycode_empty));
                    return;
                }
                newPwd = et_newpwd.getText().toString();
                if (TextUtils.isEmpty(newPwd)) {
                    et_newpwd.setError(getString(R.string.new_pwd));
                    return;
                }
                newPwd2 = et_newpwd2.getText().toString();
                if (TextUtils.isEmpty(newPwd2) || !newPwd.equals(newPwd2)) {
                    et_newpwd.setError("两次输入不一致!");
                    return;
                }

                if (newPwd.length() != 6) {
                    et_newpwd.setError("密码必须是6位数字!");
                    return;
                }

                changePwd();
                break;
            default:
                break;
        }
    }

    private void changePwd() {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
//            contentObj.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            contentObj.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            contentObj.put("verifytype", "verifycode");
            contentObj.put("zhifu_passwd", MD5.GetMD5Code(newPwd));
            contentObj.put("verifycode", verifyCode);

            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.ASSETS_USER_ZHIFUPWD_SET.getAssetsMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
//                            User.AssetsEntity assets = JsonUtility.fromJson(obj.get("assetsettings"), User.AssetsEntity.class);
//                            SharedPreferencesUtil.saveUserAssetsSetting(mContext,assets);
                            finish();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getVerifyCode() {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
//            contentObj.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            contentObj.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.ASSETS_USER_VERIFYCODE_SEND.getAssetsMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            tv_get_verifycode.setBackgroundResource(R.drawable.btn_gray);
                            time.start(); //开始计时
                            ToastUtil.showToast(mContext, getString(R.string.verifycode_success));
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tv_get_verifycode.setBackgroundResource(R.drawable.btn_red);
            tv_get_verifycode.setText(getString(R.string.reverifycode));
            tv_get_verifycode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tv_get_verifycode.setClickable(false);
            tv_get_verifycode.setText(getString(R.string.remaining_time, millisUntilFinished / 1000));
        }
    }
}
