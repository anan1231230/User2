package com.hclz.client.base.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.hclz.client.R;
import com.hclz.client.base.util.WindowSizeUtil;
import com.hclz.client.kitchen.adapter.CartsAdapter;

public class CartsListViewDialog {

    private Context context;

    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View view = null;
    private ImageView iv_title;
    private ListView lv_content;
    private CartsAdapter mAdapter;

    public CartsListViewDialog(Context context, CartsAdapter adapter) {
        this.context = context;
        this.mAdapter = adapter;
        initViews();
    }

    private void initViews() {
        if (popupWindow == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_listview2, null);

            iv_title = (ImageView) view.findViewById(R.id.iv_title);
            lv_content = (ListView) view.findViewById(R.id.lv_content);

            popupWindow = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    (WindowSizeUtil.getHeight(context) - 100) / 2);
            popupWindow
                    .setInputMethodMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
            popupWindow
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setBackgroundDrawable(new ColorDrawable(context
                    .getResources().getColor(R.color.transparent)));
            popupWindow.setAnimationStyle(R.style.PopupAnimation);
        }
        mAdapter.setEmptyString(R.string.cart_empty);
        lv_content.setAdapter(mAdapter);
        iv_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
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

}
