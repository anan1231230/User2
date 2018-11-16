package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask.ResultHandler;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FeedBackActivity extends BaseActivity implements OnClickListener {

    private EditText etFeedback;
    private Button btnSubmit;

    private String mFeedback;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, FeedBackActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_feedback);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setCommonTitle(R.string.feedback);

        etFeedback = (EditText) findViewById(R.id.et_feedback);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                mFeedback = etFeedback.getText().toString();
                if (TextUtils.isEmpty(mFeedback)) {
                    etFeedback.setError(getString(R.string.feedback_content));
                    return;
                }
                SubmitFeedback();
                break;
            default:
                break;
        }
    }

    private void SubmitFeedback() {
        requestParams = new HashMap<String, String>();
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
            content.put("advise", mFeedback);
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_FEEDBACK.getUserMethod(),
                    requestParams, new ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ToastUtil.showToast(mContext, getString(R.string.feedback_success));
                            finish();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException();
        }
    }

}
