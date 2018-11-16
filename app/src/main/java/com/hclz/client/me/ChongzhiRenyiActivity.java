package com.hclz.client.me;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.Bill;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by handsome on 2016/12/7.
 */

public class ChongzhiRenyiActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private EditText et_feedback;
    private Button btn_submit;

    public static void startMe(Activity context){
        Intent intent = new Intent(context,ChongzhiRenyiActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chongzhirenyi);
    }

    @Override
    protected void initView() {

        et_feedback = (EditText) findViewById(R.id.et_feedback);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        setCommonTitle("充值任意金额");
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String feedback = et_feedback.getText().toString().trim();
        if (TextUtils.isEmpty(feedback)) {
            ToastUtil.showToast(mContext,"请输入充值金额");
            return;
        }

        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
            contentObj = PostHttpUtil.prepareContents(configMap,mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            JSONObject bill = new JSONObject();
            bill.put("billtype", "buycard");
            bill.put("cardtype", "balancecard");
            bill.put("recharge_amount", ((Integer.parseInt(feedback)) * 100));

            contentObj.put("bill", bill);

            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.ASSETS_USER_BILLS_RECHARGE.getAssetsMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            Bill bill = JsonUtility.fromJson(obj.get("bill"), Bill.class);
                            SelectPayMethod3Activity.startMe(mContext,bill.getBillid(),bill.getPrice(),SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
                            finish();
                        }

                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}
