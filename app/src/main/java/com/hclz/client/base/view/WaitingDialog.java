package com.hclz.client.base.view;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hclz.client.R;

/***
 * 等待提示
 *
 * @author Administrator
 */
public class WaitingDialog extends Dialog {

    /**
     * 关联
     */
    private Context context;
    /**
     * 进度
     */
    private TextView image;
    /**
     * 动画
     */
    private Animation anim;

    public WaitingDialog(Context context) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.waiting_layout);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        this.context = context;
        image = (TextView) findViewById(R.id.waiting_img);
    }

    /**
     * 显示
     */
    public void show() {
        anim = AnimationUtils.loadAnimation(context, R.anim.progresscrl);
        anim.setDuration(1500);
        image.startAnimation(anim);
        anim.startNow();
        super.show();
    }

    /**
     * 消失
     */
    public void dismiss() {
        image.clearAnimation();
        super.dismiss();
    }
}
