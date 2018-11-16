package com.hclz.client.base.photo;

import android.app.Activity;

/**
 * 等待对话框
 */
public class SelectPicControll {

    private static final String TAG = SelectPicControll.class.getSimpleName();
    /**
     * 等待对话框
     **/
    private static SelectPicDialog selectPicDialog;

    /**
     * 显示等待对话框
     */
    public static void showPicDialog(final Activity context, SelectPicDialog.OnCustomDialogListener listener, String title) {
        final SelectPicDialog selectPicDialog = new SelectPicDialog(context, title);
        selectPicDialog.setCustomDialogListner(listener);
        selectPicDialog.show();
    }

    /**
     * 隐藏等待对话框
     */
    public static void dismissWaitingDialog() {
        if (selectPicDialog != null) {
            selectPicDialog.dismiss();
            selectPicDialog = null;
        }
    }
}
