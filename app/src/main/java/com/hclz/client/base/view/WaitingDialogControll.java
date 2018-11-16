package com.hclz.client.base.view;

import android.content.Context;

import com.hclz.client.base.config.SanmiConfig;

/**
 * 等待对话框
 */
public class WaitingDialogControll {
    /**
     * 等待对话框
     **/
    private static WaitingDialog waitingDialog;
    private static LoadingDialog loadingDialog;
    private static WaitingDialog waitingDialog2;

    /**
     * 显示等待对话框
     */
    public static void showWaitingDialog(Context context) {
        if (!SanmiConfig.isPulling) {
            if (waitingDialog == null) {
                waitingDialog = new WaitingDialog(context);
                waitingDialog.show();
            }
        }
    }

    /**
     * 隐藏等待对话框
     */
    public static void dismissWaitingDialog() {
        if (waitingDialog != null) {
            waitingDialog.dismiss();
            waitingDialog = null;
        }
    }

    /**
     * 显示加载对话框
     */
    public static void showLoadingDialog(Context context) {
        if (SanmiConfig.isFirstLoad) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(context);
                loadingDialog.show();
            }
        } else if (!SanmiConfig.isPulling) {
            if (waitingDialog2 == null) {
                waitingDialog2 = new WaitingDialog(context);
                waitingDialog2.show();
            }
        }
    }


    /**
     * 隐藏加载对话框
     */
    public static void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
            SanmiConfig.isFirstLoad = false;
        }
        if (waitingDialog2 != null) {
            waitingDialog2.dismiss();
            waitingDialog2 = null;
        }
    }

//    /**
//     * 播放动画
//     */
//    public static void playAnim() {
//        if (SanmiConfig.isFirstLoad && loadingDialog != null && loadingDialog.isShowing()) {
//            loadingDialog.play();
//        }
//    }

}
