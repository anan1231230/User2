package com.hclz.client.faxian.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.WindowSizeUtil;
import com.hclz.client.shouye.newcart.DiandiCartItem;

public class CartDialog {

    private DiandiCartItem mItem;
    private int mPosition;
    private Context context;

    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View view = null;
    private CartListener mListener;
    private LinearLayout ll_title;
    private TextView tv_title;
    private TextView tv_note;
    private EditText et_num;
    private TextView tv_confirm;

    public CartDialog(Context context, CartListener listener) {
        this.context = context;
        this.mListener = listener;
        initViews();
    }

    public void setItem(DiandiCartItem item,int position) {
        mItem = item;
        showContent();
    }

    private void initViews() {
        if (popupWindow == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_cart, null);

            ll_title = (LinearLayout) view.findViewById(R.id.ll_title);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_note = (TextView) view.findViewById(R.id.tv_note);
            et_num = (EditText) view.findViewById(R.id.et_num);
            tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);

            popupWindow = new PopupWindow(view,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (WindowSizeUtil.getHeight(context) - 100) / 2);
            popupWindow
                    .setInputMethodMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
            popupWindow
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.transparent)));
            popupWindow.setAnimationStyle(R.style.PopupAnimation);
        }
    }

    private void showContent() {
        et_num.setText(mItem.num+"");
        tv_title.setText(mItem.name);
        tv_note.setText("最低起订量："+ mItem.minimal_quantity+",最低增量："+mItem.minimal_plus);
        ll_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConfirm(mItem, TextUtils.isEmpty(et_num.getText())?0:Integer.parseInt(et_num.getText().toString()),mPosition);
                dismiss();
            }
        });
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

    public static interface CartListener {
        public void onConfirm(DiandiCartItem item,int num,int position);
    }

}
