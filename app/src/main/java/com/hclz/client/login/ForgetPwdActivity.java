package com.hclz.client.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask.ResultHandler;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgetPwdActivity extends BaseActivity implements OnClickListener {

    private EditText etPhonenum, etVerifycode;
    private TextView tvGetVerifycode;
    private Button btnNext;

    private String phoneNum;
    private String verifyCode;

    private TimeCount time;

    /**
     * 页面跳转,不需要传递参数
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, ForgetPwdActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_forget_pwd);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        etPhonenum = (EditText) findViewById(R.id.et_phonenum);
        etVerifycode = (EditText) findViewById(R.id.et_verifycode);
        tvGetVerifycode = (TextView) findViewById(R.id.tv_get_verifycode);
        btnNext = (Button) findViewById(R.id.btn_next);
    }

    @Override
    protected void initInstance() {
        time = new TimeCount(60000, 1000);
    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.forget_pwd);
    }

    @Override
    protected void initListener() {
        tvGetVerifycode.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_verifycode:
                phoneNum = etPhonenum.getText().toString();
                if (TextUtils.isEmpty(phoneNum)) {
                    etPhonenum.requestFocus();
                    etPhonenum.setError(getString(R.string.phone_empty));
                    return;
                }
                if (!Utility.isPhoneNO(phoneNum)) {
                    etPhonenum.requestFocus();
                    etPhonenum.setError(getString(R.string.phone_validate_fail));
                    return;
                }
                getVerifyCode();
                break;
            case R.id.btn_next:
                phoneNum = etPhonenum.getText().toString();
                verifyCode = etVerifycode.getText().toString();
                if (TextUtils.isEmpty(phoneNum)) {
                    etPhonenum.requestFocus();
                    etPhonenum.setError(getString(R.string.phone_empty));
                    return;
                }
                if (!Utility.isPhoneNO(phoneNum)) {
                    etPhonenum.requestFocus();
                    etPhonenum.setError(getString(R.string.phone_validate_fail));
                    return;
                }
                if (TextUtils.isEmpty(verifyCode)) {
                    etVerifycode.requestFocus();
                    etVerifycode.setError(getString(R.string.verifycode_empty));
                    return;
                }
                ChangePwdActivity.startMe(mContext,phoneNum,verifyCode);
                finish();
                break;
            default:
                break;
        }
    }

    private void getVerifyCode() {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
//            contentObj.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            contentObj.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put("phone", phoneNum);
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_VERIFYCODE.getUserMethod(), requestParams,
                    new ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            tvGetVerifycode.setBackgroundResource(R.drawable.shape_btn_border_gray);
                            time.start();//开始计时
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
            tvGetVerifycode.setBackgroundResource(R.drawable.btn_red);
            tvGetVerifycode.setText(getString(R.string.reverifycode));
            tvGetVerifycode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tvGetVerifycode.setClickable(false);
            tvGetVerifycode.setText(getString(R.string.remaining_time, millisUntilFinished / 1000));
        }
    }

}
