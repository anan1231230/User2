package com.hclz.client.base.photo;

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
public class SelectPicDialog extends Dialog {
    String mTitle;
    OnCustomDialogListener mOnCustomDialogListener;
    TextView mTake, mSelect, tvTitle;

    public SelectPicDialog(Context context, String title) {
        super(context, R.style.Dialog_FS);
        mTitle = title;
    }

    public void setCustomDialogListner(OnCustomDialogListener onCustomDialogListener) {
        mOnCustomDialogListener = onCustomDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_pic);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(mTitle);
        mTake = (TextView) findViewById(R.id.take);
        mSelect = (TextView) findViewById(R.id.select);

        mTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomDialogListener.take();
                dismiss();
            }
        });

        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCustomDialogListener.select();
                dismiss();
            }
        });

    }

    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener {
        void take();

        void select();
    }
}
