package com.hclz.client.base.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hclz.client.R;

/**
 * Created by hjm on 16/7/26.
 */

public class SingleButtonDialog {

    private AlertDialog alertDialog;
    private static SingleButtonDialog singleButtonDialog;
    private TextView contentTv,okButtonTv;

    private SingleButtonDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_button_dialog,null);
        contentTv = (TextView) view.findViewById(R.id.single_button_dialog_content);
        okButtonTv = (TextView) view.findViewById(R.id.single_button_dialog_ok);
        alertDialog = new AlertDialog.Builder(context).setView(view).create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public static SingleButtonDialog getInstence(Context context) {
        if (singleButtonDialog != null) {
            return singleButtonDialog;
        } else {
            return new SingleButtonDialog(context);
        }
    }

    public void showDialog(String content, final SingleButtonDialog.OnPositiveListener onPositiveListener) {
        contentTv.setText(content);
        okButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPositiveListener!=null){
                    onPositiveListener.OnPositive();
                }
                stopDialog();
            }
        });
        alertDialog.show();
    }

    public void stopDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public interface OnPositiveListener{
        void OnPositive();
    }
}
