package com.hclz.client.me;        /**
 * Created by handsome on 2016/12/20.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.hclz.client.R;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

public class KefuActivity extends BaseAppCompatActivity {


    private FrameLayout container;


    private EaseChatFragment mChatFragment;
    String toChatUsername;
    int chatType;

    public static void startMe(Activity context, String id, int chatType) {
        Intent intent = new Intent(context, KefuActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", chatType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_kefu);
    }

    @Override
    protected void initView() {
        container = (FrameLayout) findViewById(R.id.container);
    }

    @Override
    protected void initInstance() {
        try {
            EMClient.getInstance().login(SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID), SharedPreferencesUtil.get(mContext,ProjectConstant.APP_HX_PASSWD), new EMCallBack() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        if (mIntent != null) {
            toChatUsername = mIntent.getStringExtra("id");
            chatType = mIntent.getIntExtra("type", 1);
            mChatFragment = new EaseChatFragment();
            Bundle args = new Bundle();
            args.putInt(EaseConstant.EXTRA_CHAT_TYPE, chatType);
            args.putString(EaseConstant.EXTRA_USER_ID, toChatUsername);
            mChatFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.container, mChatFragment).commit();
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
}
