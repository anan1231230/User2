package com.hclz.client.base.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hclz.client.R;

/**
 * Created by Administrator on 2015/10/16.
 */
public class SelectDialog extends Dialog {
    TextView tv_message, tv_phone;
    SelectListener mListener;

    public SelectDialog(Context context, SelectListener listener) {
        super(context, R.style.Dialog_FS);
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select);
        ((TextView) findViewById(R.id.tv_title)).setText("客户服务");
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_phone = (TextView) findViewById(R.id.tv_phone);

        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.phone();
                dismiss();
            }
        });

        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.message();
                dismiss();
            }
        });

    }

    //定义回调事件，用于dialog的点击事件
    public interface SelectListener {
        void message();

        void phone();
    }
}
