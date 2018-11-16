package com.hclz.client.base.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.hclz.client.R;
import com.hclz.client.base.config.SanmiConfig;

/***
 * 等待提示
 *
 * @author Administrator
 */
public class LoadingDialog extends Dialog {

//    /**
//     * 关联
//     */
//    private Context context;
//    /**
//     * 进度
//     */
//    private TextView cha, dao;
//    /**
//     * 动画
//     */
//    private Animation animcha;
//    private Animation animdao;

    public LoadingDialog(Context context) {
//        super(context, android.R.style.Theme);
        super(context, R.style.Dialog_Fullscreen);
        setOwnerActivity((Activity) context);
        setContentView(R.layout.loading_dialog);
        windowDeploy();
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        this.context = context;
//        setStatusBarEffect();
//        cha = (TextView) findViewById(R.id.cha);
//        dao = (TextView) findViewById(R.id.dao);
    }

//    private void setStatusBarEffect() {
//        /*add title bar background [START]*/
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//
//        SystemBarTintManager tintManager = new SystemBarTintManager((Activity) context);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.red_title);
//        /*add title bar background[END]*/
//    }
//
//    @TargetApi(19)
//    private void setTranslucentStatus(boolean on) {
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }

    //设置窗口显示
    public void windowDeploy() {
        Window window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.Dialog_Fullscreen); //设置窗口弹出动画
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setAttributes(wl);
    }

    /**
     * 显示
     */
    public void show() {
        super.show();
        SanmiConfig.isShowingLoadingDialog = true;
    }

//    public void play() {
//        animcha = AnimationUtils.loadAnimation(context, R.anim.cha_progress);
//        animcha.setInterpolator(new AccelerateInterpolator());
//        animcha.setRepeatCount(-1);
//        cha.startAnimation(animcha);
//        animcha.startNow();
//
//        animdao = AnimationUtils.loadAnimation(context, R.anim.dao_progress);
//        animdao.setInterpolator(new AccelerateInterpolator());
//        animdao.setRepeatCount(-1);
//        dao.startAnimation(animdao);
//        animdao.startNow();
//    }
//
//    public void stop() {
//        cha.clearAnimation();
//        dao.clearAnimation();
//    }

    /**
     * 消失
     */
    public void dismiss() {
        super.dismiss();
        SanmiConfig.isShowingLoadingDialog = false;
    }
}
