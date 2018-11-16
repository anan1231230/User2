package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.Kitchen;
import com.hclz.client.base.bean.KitchenUser;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by handsome on 16/6/21.
 */
public class TuijianmaActivity extends BaseActivity {

    //    EditText et_tuijianma;
    TextView tv_submit;
    String erweima_result;
    private Kitchen mKitchenInfo;

    private KitchenUser kitchenUser;

    private CircleImageView mCiv,mUserCiv;
    private TextView mTitleTv, mPhoneTv, mAddressTv,mUserNameTv,mUserPhoneTv;
    private EditText et_tuijianma;
    private LinearLayout ll_cshop;

    private LinearLayout mUserLayout;

    /**
     * 页面跳转
     *
     * @param from
     * @param erweima_result
     */
    public static void startMe(Context from,String erweima_result){
        Intent intent = new Intent(from, TuijianmaActivity.class);
        intent.putExtra("erweima_result",erweima_result);
        from.startActivity(intent);
    }
    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, TuijianmaActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_tuijianma);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        et_tuijianma = (EditText) findViewById(R.id.et_tuijianma);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        ll_cshop = (LinearLayout) findViewById(R.id.ll_cshop);

        mCiv = (CircleImageView) findViewById(R.id.tuijianma_head_pic);
        mTitleTv = (TextView) findViewById(R.id.tuijianma_hehuoren_name);
        mPhoneTv = (TextView) findViewById(R.id.tuijianma_hehuoren_phone);
        mAddressTv = (TextView) findViewById(R.id.tuijianma_hehuoren_address);

        mUserLayout = (LinearLayout) findViewById(R.id.tuijianma_user_lllayout);
        mUserCiv = (CircleImageView) findViewById(R.id.tuijianma_head_pic_user);
        mUserNameTv = (TextView) findViewById(R.id.tuijianma_hehuoren_name_user);
        mUserPhoneTv = (TextView) findViewById(R.id.tuijianma_hehuoren_phone_user);
    }

    @Override
    protected void initInstance() {
        erweima_result = getIntent().getStringExtra("erweima_result");
    }

    @Override
    protected void initData() {
        setCommonTitle("推荐码");
        if (TextUtils.isEmpty(erweima_result)){
            et_tuijianma.setVisibility(View.VISIBLE);
            ll_cshop.setVisibility(View.GONE);
        } else {
            et_tuijianma.setVisibility(View.GONE);
            ll_cshop.setVisibility(View.VISIBLE);
            getTuijianmaWithHehuoren(erweima_result);
        }
    }

    @Override
    protected void initListener() {
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(erweima_result)){
                    bindWithTuijianma(et_tuijianma.getText().toString().trim());
                } else {
                    bindWithTuijianma(erweima_result);
                }
            }
        });
    }

    /**
     * 获取二维码相关合伙人的信息
     *
     * @param code 二维码识别的推荐码
     */
    private void getTuijianmaWithHehuoren(String code) {
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            content.put("code", code);
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.CSHOP_INFO_FOR_ERWEIMA.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mKitchenInfo = JsonUtility.fromJson(obj.get("cshop"),
                                    Kitchen.class);
                            if (!"{}".equals(obj.get("user").toString()) && !TextUtils.isEmpty(obj.get("user").toString())) {
                                kitchenUser = JsonUtility.fromJson(obj.get("user"), KitchenUser.class);
                            }
                            showContent();
                        }

                        @Override
                        public void callBackNotHehuoren(){
                            erweima_result = "";
                            et_tuijianma.setVisibility(View.VISIBLE);
                            ll_cshop.setVisibility(View.GONE);
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 展示推荐码相关的合伙人的信息
     */
    private void showContent() {
        ImageUtility.getInstance(mContext).showImage(mKitchenInfo.getAlbum_thumbnail()[0], mCiv,R.mipmap.ic_dianpu);
        mTitleTv.setText(mKitchenInfo.getTitle());
        mPhoneTv.setText(mKitchenInfo.getPhone());
        mAddressTv.setText(mKitchenInfo.getAddress());

        if (kitchenUser != null){
            mUserLayout.setVisibility(View.VISIBLE);
            ImageUtility.getInstance(mContext).showImage(mKitchenInfo.getAlbum_thumbnail()[0], mUserCiv);
            mUserNameTv.setText(kitchenUser.getMain_account().getNickname());
            mUserPhoneTv.setText(kitchenUser.getSub_accounts().get(0).getSid());
        }else {
            mUserLayout.setVisibility(View.GONE);
        }
    }

    private void bindWithTuijianma(String code) {
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            content.put("code", code);
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.CSHOP_USER_BIND.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ToastUtil.showToast(mContext, "绑定成功!");
                            finish();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
