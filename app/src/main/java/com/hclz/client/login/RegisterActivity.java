package com.hclz.client.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask.ResultHandler;
import com.hclz.client.base.bean.MainAccount;
import com.hclz.client.base.bean.SubAccount;
import com.hclz.client.base.cart.Cart;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.HclzConstant;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.log.LogUploadUtil;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.StartAlarmReceiver;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.util.Utility;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.faxian.products.AddressIns;
import com.hclz.client.me.MeFragment;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.merben.wangluodianhua.NetPhone;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends BaseActivity implements OnClickListener {

    private TextView tvGetVerifycode, tvLogin;
    private EditText etVerifycode, etPhoneNum, et_tuijianren;
    private String phoneNum, verifyCode;

    private TimeCount time;
    private String tuijianren;

    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (!tvLogin.isClickable()){
                tvLogin.setClickable(true);
            }
            WaitingDialogControll.dismissLoadingDialog();
            return true;
        }
    });

    /**
     * 页面跳转,不需要传递参数
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, RegisterActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        etPhoneNum = (EditText) findViewById(R.id.et_phonenum);
        etVerifycode = (EditText) findViewById(R.id.et_verifycode);
        tvGetVerifycode = (TextView) findViewById(R.id.tv_get_verifycode);
        et_tuijianren = (EditText) findViewById(R.id.et_tuijianren);
        tvLogin = (TextView) findViewById(R.id.tv_login);
    }

    @Override
    protected void initInstance() {
        time = new TimeCount(60000, 1000);
    }

    @Override
    protected void initData() {
        configMap = HclzApplication.getData();
        setCommonTitle(R.string.zhuce);
    }

    @Override
    protected void initListener() {
        tvGetVerifycode.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_verifycode:
                phoneNum = etPhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    etPhoneNum.requestFocus();
                    etPhoneNum.setError(getString(R.string.phone_empty));
                    return;
                }
                if (!Utility.isPhoneNO(phoneNum)) {
                    etPhoneNum.requestFocus();
                    etPhoneNum.setError(getString(R.string.phone_validate_fail));
                    return;
                }
                getVerifyCode();
                break;
            case R.id.tv_login:
                tvLogin.setClickable(false);
                mHandler.sendEmptyMessageDelayed(0,500);

                phoneNum = etPhoneNum.getText().toString();
                verifyCode = etVerifycode.getText().toString();
                tuijianren = et_tuijianren.getText().toString();
                if (TextUtils.isEmpty(phoneNum)) {
                    etPhoneNum.requestFocus();
                    etPhoneNum.setError(getString(R.string.phone_empty));
                    return;
                }
                if (!Utility.isPhoneNO(phoneNum)) {
                    etPhoneNum.requestFocus();
                    etPhoneNum.setError(getString(R.string.phone_validate_fail));
                    return;
                }
                if (TextUtils.isEmpty(verifyCode)) {
                    etVerifycode.requestFocus();
                    etVerifycode.setError(getString(R.string.verifycode_empty));
                    return;
                }
//                if (TextUtils.isEmpty(tuijianren)){
//                    et_tuijianren.requestFocus();
//                    et_tuijianren.setError(getString(R.string.tuijianren_empty));
//                    return;
//                }
//                if (!Utility.isPhoneNO(tuijianren)&&!("888888").equals(tuijianren.trim())) {
//                    et_tuijianren.requestFocus();
//                    et_tuijianren.setError(getString(R.string.tuijianren_real));
//                    return;
//                }
//                Android_6_Permission(mContext, Manifest.permission.READ_PHONE_STATE);
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        WaitingDialogControll.showLoadingDialog(mContext);
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put("phone", phoneNum);
            contentObj.put("introducer", et_tuijianren.getText().toString().trim());
            contentObj.put("verify_code", verifyCode);
            contentObj.put("device", CommonUtil.getDeviceObj(mContext));
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_REGIASTER.getUserMethod(), requestParams,
                    new ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            MainAccount mainAccount = JsonUtility.fromJson(obj.get("main_account"), MainAccount.class);
                            List<SubAccount> subAccounts = JsonUtility.fromJson(obj.get("sub_accounts"),
                                    new TypeToken<ArrayList<SubAccount>>() {
                                    });

                            SharedPreferencesUtil.save(mContext,ProjectConstant.APP_USER_INTRODUCER,obj.get("introducer").toString());
//                            register(Integer.parseInt(mainAccount.getMid() == null ? "" : mainAccount.getMid()));

                            SharedPreferencesUtil.saveUserBasic(mContext, mainAccount, subAccounts);
                            AddressIns.getInstance().setAddress(null);
                            LogUploadUtil.upload(et_tuijianren.getText().toString().trim());
                            //增加user_type判定,判断是 normal(普通用户) 还是 cshop(合伙人) 还是 dshop(城代)
                            String user_type = JsonUtility.fromJson(obj.get("user_type"), String.class);
                            String previous_user_type = SharedPreferencesUtil.get(mContext, "user_type");
                            SharedPreferencesUtil.save(mContext, "user_type", user_type);
                            if (user_type.equals("dshop")) {
                                ToastUtil.showToast(mContext, "当前账号为城市代理合伙人,商品价格已变,请重新添加购物车~");
                                SanmiConfig.isMallNeedRefresh = true;
                                SanmiConfig.isHaiwaiNeedRefresh = true;
                                SanmiConfig.isOrderNeedRefresh = true;
                                DiandiCart.getInstance().clear(mContext);
                            } else if (user_type.equals("buser")||user_type.equals("euser")) {
                                ToastUtil.showToast(mContext, "当前账号为B2B合伙人,商品价格已变,请重新添加购物车~");
                                SanmiConfig.isMallNeedRefresh = true;
                                SanmiConfig.isHaiwaiNeedRefresh = true;
                                SanmiConfig.isOrderNeedRefresh = true;
                                DiandiCart.getInstance().clear(mContext);
                            } else if (user_type.equals("cshop")) {
                                ToastUtil.showToast(mContext, "当前账号为社区合伙人,商品价格已变,请重新添加购物车~");
                                String cid = JsonUtility.fromJson(obj.get("cid"), String.class);
                                String code = JsonUtility.fromJson(obj.get("code"),String.class);
                                JsonObject cshop = JsonUtility.fromJson(obj.get("cshop"),JsonObject.class);
                                SharedPreferencesUtil.save(mContext,"cid",cid);
                                SharedPreferencesUtil.save(mContext,"code",code);
                                SharedPreferencesUtil.save(mContext,"cshop",cshop.toString());
                                SanmiConfig.isMallNeedRefresh = true;
                                SanmiConfig.isHaiwaiNeedRefresh = true;
                                SanmiConfig.isOrderNeedRefresh = true;
                                DiandiCart.getInstance().clear(mContext);
                            } else if (previous_user_type != null && !previous_user_type.equals(user_type)) {
                                ToastUtil.showToast(mContext, "当前账号为普通用户,之前为特殊用户,商品价格已变,请重新添加购物车~");
                                SanmiConfig.isMallNeedRefresh = true;
                                SanmiConfig.isHaiwaiNeedRefresh = true;
                                SanmiConfig.isOrderNeedRefresh = true;
                                DiandiCart.getInstance().clear(mContext);
                            }

                            if ("cshop".equals(user_type)){
                                StartAlarmReceiver.getInstence(mContext).startAlarmReceiver();
                            }else{
                                StartAlarmReceiver.getInstence(mContext).stopAlarmReceiver();
                            }

                            //友盟统计登录
                            if (subAccounts != null){
                                for (SubAccount subAccount : subAccounts) {
                                    if ("phone".equals(subAccount.getType())) {
                                        MobclickAgent.onProfileSignIn(subAccount.getSid());
                                    }
                                }
                            }

                            WaitingDialogControll.dismissLoadingDialog();


                            //注册时，注册网络电话
                            NetPhone.register(mContext, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_PHONE), null);

                            //跳转到之前的界面，无参数直接跳转到我的界面
                            HclzConstant.getInstance().isNeedRefresh = true;
                            MeFragment.startMe(mContext);
                            finish();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int REQUEST_CODE_LD = 1001;
    public void Android_6_Permission(Activity context, String permission) {
        //首先判断版本号是否大于等于6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检测权限是否开启
            if (!(context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)) {
                context.requestPermissions(new String[]{permission},REQUEST_CODE_LD);
            }else{
                login();
            }
        }else{
            login();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        login();
    }
    private void getVerifyCode() {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
//            contentObj.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            contentObj.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put("phone", phoneNum);
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_VERIFYCODE.getUserMethod(), requestParams,
                    new ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            tvGetVerifycode.setBackgroundResource(R.drawable.shape_btn_border);
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
