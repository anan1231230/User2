package com.hclz.client.base.wxapi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask.ResultHandler;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.view.WaitingDialogControll;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 2) {
                ToastUtil.showToast(mContext, "授权成功");
                finish();
            }
        }

        ;
    };
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.waiting_layout);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        api.handleIntent(mIntent, this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onReq(BaseReq arg0) {

    }

    @Override
    public void onResp(BaseResp resp) {
//        SendAuth.Resp sendAuthResp = (Resp) resp;
//        if (sendAuthResp.errCode == 0) {
//            code = sendAuthResp.code;
//            sendToHclz();
//        }
//        ToastUtil.showToast(mContext, sendAuthResp.code + "");
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "微信分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "微信分享被取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "微信分享失败";
                break;
            default:
                result = "未知错误";
                break;
        }
        ToastUtil.showToast(mContext,result);
        WaitingDialogControll.dismissWaitingDialog();
        finish();
    }

    protected void sendToHclz() {
        new Thread() {
            @Override
            public void run() {
                try {
                    configMap = HclzApplication.getData();
                    requestParams = new HashMap<String, String>();
                    JSONObject content = null;
//                    content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//                    content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
                    content = PostHttpUtil.prepareContents(configMap,mContext);
                    content.put(ProjectConstant.APP_USER_MID,
                            SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
                    content.put(ProjectConstant.APP_USER_SESSIONID,
                            SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
                    content.put("code", code);
                    PostHttpUtil.prepareParams(requestParams, content.toString());
                    sanmiAsyncTask.setIsShowDialog(false);
                    sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_BIND_WX.getUserMethod(),
                            requestParams, new ResultHandler() {
                                @Override
                                public void callBackForServerSuccess(String result) {
                                    Message message = handler.obtainMessage();
                                    message.what = 2;
                                    handler.sendMessage(message);
                                }
                            });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

}
