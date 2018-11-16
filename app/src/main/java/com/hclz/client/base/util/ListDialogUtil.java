package com.hclz.client.base.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by hjm on 16/7/20.
 */

public class ListDialogUtil {

    private Context context;
    private AlertDialog alertDialog;

    public ListDialogUtil(Context context) {
        this.context = context;
    }

    public interface OnListDialogItemClickListener {
        void onListDialogItemClick(int which);
    }

    public interface OnListDialogDismissListener {
        void onListDialogDismiss();
    }

    public void showListDialog(String title, String[] list, final OnListDialogItemClickListener onListDialogItemClickListener) {
        showListDialog(title, list, onListDialogItemClickListener, null, true);
    }

    public void showListDialog(String title, String[] list, final OnListDialogItemClickListener onListDialogItemClickListener, boolean cancelable) {
        showListDialog(title, list, onListDialogItemClickListener, null, cancelable);
    }

    public void showListDialog(String title, String[] list, final OnListDialogItemClickListener onListDialogItemClickListener, final OnListDialogDismissListener onListDialogDismissListener, boolean cancelable) {
        if (alertDialog != null && alertDialog.isShowing()) {
            //do nothing
        } else {
            alertDialog = new AlertDialog.Builder(context).setTitle(title).setItems(list, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onListDialogItemClickListener.onListDialogItemClick(which);
                    stopListDialog();
                }
            }).create();

            alertDialog.setCancelable(cancelable);
            alertDialog.setCanceledOnTouchOutside(cancelable);

            if (onListDialogDismissListener != null) {
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        onListDialogDismissListener.onListDialogDismiss();
                    }
                });
            }
            alertDialog.show();
        }
    }


    public void stopListDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
