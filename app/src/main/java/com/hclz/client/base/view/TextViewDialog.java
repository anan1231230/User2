package com.hclz.client.base.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.handler.WeakHandler;

public class TextViewDialog {

    private Context context;
    private String content;

    //是否显示按钮,默认不显示
    private boolean isShowButton = true;
    private Item item;
    private WeakHandler handler;
    private int msgWhat = 11;
    private int msgError = 0X101;
    private String gotoActivity = "LoginActivity";

    private onDialogClickListener mDialogClickListener;

    public TextViewDialog(Context context, String content, WeakHandler handler) {
        this.context = context;
        this.content = content;
        this.handler = handler;
    }

    public TextViewDialog(Context context, String content, int msgWhat, WeakHandler handler) {
        this.context = context;
        this.content = content;
        this.handler = handler;
        this.msgWhat = msgWhat;
    }

    public TextViewDialog(Context context, String content, boolean isShowButton, WeakHandler handler) {
        this.context = context;
        this.content = content;
        this.handler = handler;
        this.isShowButton = isShowButton;
    }

    public void showDialog() {
        //一个界面有2个网络请求的时候，不需要弹出第二遍
        final AlertDialog alert = new AlertDialog.Builder(context).create();
        item = new Item();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_textview, null);
        item.ll_buttons = (LinearLayout) view.findViewById(R.id.ll_buttons);
        item.iv_success = (ImageView) view.findViewById(R.id.iv_success);
        item.tv_content = (TextView) view.findViewById(R.id.tv_content);
        item.tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        item.tv_ok = (TextView) view.findViewById(R.id.tv_ok);

        item.tv_content.setText(content);
        if (isShowButton) {
            item.ll_buttons.setVisibility(View.VISIBLE);
            item.iv_success.setVisibility(View.GONE);
            item.tv_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (handler != null) {
                        Message msg = new Message();
                        msg.what = msgError;
                        handler.sendMessage(msg);
                    }
                    if (mDialogClickListener != null) {
                        mDialogClickListener.onCancelClick();
                    }
                    alert.cancel();
                }
            });
            item.tv_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (handler != null) {
                        Message msg = new Message();
                        msg.what = msgWhat;
                        Bundle bundle = new Bundle();
                        bundle.putString("gotoActivity", gotoActivity);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                    if (mDialogClickListener != null) {
                        mDialogClickListener.onSureClick();
                    }
                    alert.cancel();
                }
            });
        } else {
            item.ll_buttons.setVisibility(View.GONE);
            item.iv_success.setVisibility(View.VISIBLE);
        }
        alert.setView(view, 0, 0, 0, 0);
        alert.setCancelable(false);
        alert.show();
    }

    public void setDialogClickListener(onDialogClickListener mDialogClickListener) {
        this.mDialogClickListener = mDialogClickListener;
    }

    public void setGotoActivity(String gotoActivity) {
        this.gotoActivity = gotoActivity;
    }

    public interface onDialogClickListener {
        void onCancelClick();

        void onSureClick();
    }

    public class Item {
        LinearLayout ll_buttons;
        ImageView iv_success;
        TextView tv_content;
        TextView tv_cancel;
        TextView tv_ok;
    }

}
