package com.hclz.client.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.MainActivity;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask.ResultHandler;
import com.hclz.client.base.bean.MainAccount;
import com.hclz.client.base.bean.SubAccount;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.HclzConstant;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.MD5;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.StartAlarmReceiver;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.util.Utility;
import com.hclz.client.base.ver.VersionUtils;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.faxian.products.AddressIns;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.merben.wangluodianhua.NetPhone;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private EditText etPhoneNum, etPwd;
    private TextView tvForgetPwd, tvZhuCe ,tvVersion;
    private Button btnLogin;

    private String phoneNum, pwd;

    private String fromActivity;
    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (!btnLogin.isClickable()){
                btnLogin.setClickable(true);
            }
            WaitingDialogControll.dismissLoadingDialog();
            return true;
        }
    });

    /**
     * 页面跳转
     *
     * @param from
     * @param fromString 来自哪个页面
     */
    public static void startMe(Context from, String fromString){
        Intent intent = new Intent(from, LoginActivity.class);
        intent.putExtra("from",fromString);
        from.startActivity(intent);
    }
    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, LoginActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        etPhoneNum = (EditText) findViewById(R.id.et_phonenum);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        tvZhuCe = (TextView) findViewById(R.id.tv_zhuce);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvVersion = (TextView) findViewById(R.id.login_version);
    }

    @Override
    protected void initInstance() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initData() {
        setCommonTitle("登录");
        if (mIntent != null) {
            fromActivity = mIntent.getStringExtra("from");
        }
        tvVersion.setText("当前版本:"+ VersionUtils.getVerName(mContext));
    }

    @Override
    protected void initListener() {
        tvForgetPwd.setOnClickListener(this);
        tvZhuCe.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void login() {
        WaitingDialogControll.showLoadingDialog(mContext);
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put("phone", phoneNum);
            contentObj.put("passwd", MD5.GetMD5Code(pwd));
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.USER_LOGIN.getUserMethod(), requestParams,
                    new ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            // 登陆成功逻辑
                            // 将main_account、sub_accounts保存到配置文件
                            JsonObject obj = JsonUtility.parse(result);
                            MainAccount mainAccount = JsonUtility.fromJson(
                                    obj.get("main_account"), MainAccount.class);
                            List<SubAccount> subAccounts = JsonUtility.fromJson(obj.get("sub_accounts"),
                                    new TypeToken<ArrayList<SubAccount>>() {
                                    });
                            AddressIns.getInstance().setAddress(null);
                            SharedPreferencesUtil.save(mContext,ProjectConstant.APP_USER_INTRODUCER,obj.get("introducer").toString());
                            SharedPreferencesUtil.saveUserBasic(mContext, mainAccount, subAccounts);
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
                            }else if (user_type.equals("cshop")) {
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

                            //网络电话登录
                            NetPhone.register(mContext, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_PHONE), null);


                            // 跳转到之前的界面，无参数直接跳转到我的界面
                            HclzConstant.getInstance().isNeedRefresh = true;
                            if (!TextUtils.isEmpty(fromActivity)) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("fragment", fromActivity);
                                startActivity(intent);
                            }
                            finish();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_pwd:
                ForgetPwdActivity.startMe(mContext);
                break;
            case R.id.tv_zhuce:
                RegisterActivity.startMe(mContext);
                finish();
                break;
            case R.id.btn_login:
                btnLogin.setClickable(false);
                mHandler.sendEmptyMessageDelayed(0,500);

                phoneNum = etPhoneNum.getText().toString();
                pwd = etPwd.getText().toString();
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
                if (TextUtils.isEmpty(pwd)) {
                    etPwd.requestFocus();
                    etPwd.setError(getString(R.string.verifycode_empty));
                    return;
                }
                login();
                break;
            default:
                break;
        }
    }

}
