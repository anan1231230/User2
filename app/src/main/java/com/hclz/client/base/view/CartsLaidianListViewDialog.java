package com.hclz.client.base.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.cart.Cart;
import com.hclz.client.base.util.WindowSizeUtil;
import com.hclz.client.faxian.adapter.CartsLaidianAdapter;


public class CartsLaidianListViewDialog {

    private Context context;

    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View view = null;
    private RelativeLayout rl_title;
    private TextView tv_qingkong;
    private ListView lv_content;
    private CartsLaidianAdapter mAdapter;

    public CartsLaidianListViewDialog(Context context, CartsLaidianAdapter adapter) {
        this.context = context;
        this.mAdapter = adapter;
        initViews();
    }

    private void initViews() {
        if (popupWindow == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_listview3, null);

            rl_title = (RelativeLayout) view.findViewById(R.id.rl_title);
            tv_qingkong = (TextView) view.findViewById(R.id.tv_qingkong);
            lv_content = (ListView) view.findViewById(R.id.lv_content);

            popupWindow = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    (WindowSizeUtil.getHeight(context) - 100) / 2);
            popupWindow
                    .setInputMethodMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
            popupWindow
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.transparent)));
            popupWindow.setAnimationStyle(R.style.PopupAnimation);
        }
        mAdapter.setEmptyString(R.string.cart_empty);
        lv_content.setAdapter(mAdapter);
        rl_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        tv_qingkong.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart.getInstance().clear(context);
                mAdapter.clear();
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

}
