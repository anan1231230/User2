package com.hclz.client.me.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.hclz.client.R;
import com.hclz.client.base.util.WindowSizeUtil;
import com.hclz.client.forcshop.kucunguanli.adapter.KucunGuanliAdapter;

public class PhoneOrMessageDialog {

    private KucunGuanliAdapter.KucunItem mProduct;
    private Context context;

    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View view = null;


    private ImageView iv_phone;
    private ImageView iv_message;

    private PhoneOrMessageDialogListener mListener;



    public PhoneOrMessageDialog(Context context, PhoneOrMessageDialogListener listener) {
        this.context = context;
        this.mListener = listener;
        initViews();
    }
    private void initViews() {
        if (popupWindow == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_phone_or_message, null);

            iv_phone = (ImageView) view.findViewById(R.id.iv_phone);
            iv_message = (ImageView) view.findViewById(R.id.iv_message);

            popupWindow = new PopupWindow(view,
                    view.getWidth(),
                    view.getHeight());
            popupWindow
                    .setInputMethodMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
            popupWindow
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.transparent)));
            popupWindow.setAnimationStyle(R.style.PopupAnimation);

            iv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onPhone();
                    dismiss();
                }
            });
            iv_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onMessage();
                    dismiss();
                }
            });
        }
    }

    public void showAsDropDown(View parent) {
        if (popupWindow != null) {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            popupWindow.showAtLocation(
                    parent,
                    Gravity.NO_GRAVITY,
                    location[0], location[1] - popupWindow.getHeight());
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

    public static interface PhoneOrMessageDialogListener {
        public void onPhone();
        public void onMessage();
    }

}
