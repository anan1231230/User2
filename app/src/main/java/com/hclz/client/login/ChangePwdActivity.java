package com.hclz.client.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.MD5;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.StartAlarmReceiver;
import com.hclz.client.base.util.ToastUtil;
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

public class ChangePwdActivity extends BaseActivity implements OnClickListener {

    String phoneNum;
    String verifyCode;
    String newPwd;
    private EditText etNewpwd;
    private Button btnOk;

    /**
     * 页面跳转
     *
     * @param from
     * @param phoneNum 电话号码
     * @param verifyCode 验证码
     */
    public static void startMe(Context from, String phoneNum, String verifyCode){
        Intent intent = new Intent(from, ChangePwdActivity.class);
        intent.putExtra("phoneNum",phoneNum);
        intent.putExtra("verifyCode",verifyCode);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_change_pwd);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        etNewpwd = (EditText) findViewById(R.id.et_newpwd);
        btnOk = (Button) findViewById(R.id.btn_ok);
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.change_pwd);
        if (mIntent != null) {
            phoneNum = mIntent.getStringExtra("phoneNum");
            verifyCode = mIntent.getStringExtra("verifyCode");
        }
    }

    @Override
    protected void initListener() {
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                newPwd = etNewpwd.getText().toString();
                if (TextUtils.isEmpty(newPwd)) {
                    etNewpwd.setError(getString(R.string.new_pwd));
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
//            contentObj.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            contentObj.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put("phone", phoneNum);
            contentObj.put("passwd", MD5.GetMD5Code(newPwd));
            contentObj.put("verify_code", verifyCode);

            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_FORGET_PWD.getUserMethod(), requestParams,
                    new ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            MainAccount mainAccount = JsonUtility.fromJson(obj.get("main_account"), MainAccount.class);
                            List<SubAccount> subAccounts = JsonUtility.fromJson(obj.get("sub_accounts"),
                                    new TypeToken<ArrayList<SubAccount>>() {
                                    });

                            SharedPreferencesUtil.save(mContext,ProjectConstant.APP_USER_INTRODUCER,obj.get("introducer").toString());

                            SharedPreferencesUtil.saveUserBasic(mContext, mainAccount, subAccounts);

                            AddressIns.getInstance().setAddress(null);

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

                            NetPhone.register(mContext, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_PHONE), null);

                            //跳转到之前的界面，无参数直接跳转到我的界面
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

}
