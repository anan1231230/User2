package com.hclz.client.base.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hclz.client.R;

/**
 * Created by hjm on 16/8/9.
 */

public class ShareDialog {

    private Context context;

    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View view = null;
    private LinearLayout shareWxLayout, sharePyqLayout, copyUrlLayout;
    private TextView canncelTv;

    public interface ShareListener {
        void onShareWxClick();

        void onSharePyqClick();

        void onCopyUrlClick();
    }

    public ShareDialog(Context context) {
        this.context = context;
        initViews();
    }

    private void initViews() {
        if (popupWindow == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.share_dialog_layout, null);

            shareWxLayout = (LinearLayout) view.findViewById(R.id.share_iv_wx);
            sharePyqLayout = (LinearLayout) view.findViewById(R.id.share_iv_pyq);
            copyUrlLayout = (LinearLayout) view.findViewById(R.id.share_copy);

            RelativeLayout nullLayout = (RelativeLayout) view.findViewById(R.id.share_dialog_nulllayout);
            nullLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            canncelTv = (TextView) view.findViewById(R.id.share_cannel);

            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(00000000));
            popupWindow.setAnimationStyle(R.style.PopupAnimation);
        }
        canncelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showAsDropDown(View parent, final ShareListener shareListener) {
        if (popupWindow != null) {
            shareWxLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareListener.onShareWxClick();
                    dismiss();
                }
            });

            sharePyqLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareListener.onSharePyqClick();
                    dismiss();
                }
            });
            copyUrlLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareListener.onCopyUrlClick();
                    dismiss();
                }
            });
            popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.update();
        }

    }

    public void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }
}
