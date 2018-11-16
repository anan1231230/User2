package com.hclz.client.forcshop.kucunguanli.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.WindowSizeUtil;
import com.hclz.client.forcshop.kucunguanli.adapter.KucunGuanliAdapter;

public class KucunGuanliDialog {

    private KucunGuanliAdapter.KucunItem mProduct;
    private Context context;

    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View view = null;
    private RelativeLayout rl_title;
    private TextView tv_title,tv_delta,tv_queding;
    private EditText et_inventory;
    private RelativeLayout rl_plus,rl_minus;
    private RadioGroup rg_reason;
    private RadioButton rb_guoqi,rb_xianxia,rb_xiaohao,rb_qita;
    private String mReason = "";

    private KucunDialogListener mListener;

    public KucunGuanliDialog(Context context,KucunDialogListener listener) {
        this.context = context;
        this.mListener = listener;
        initViews();
    }

    public void setItem(KucunGuanliAdapter.KucunItem item){
        mProduct = item;
        showContent();
    }

    private void initViews() {
        if (popupWindow == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_kucunguanli, null);

            rl_title = (RelativeLayout) view.findViewById(R.id.rl_title);
            tv_queding = (TextView) view.findViewById(R.id.tv_queding);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_delta = (TextView) view.findViewById(R.id.tv_delta);
            et_inventory = (EditText) view.findViewById(R.id.et_inventory);
            rg_reason = (RadioGroup) view.findViewById(R.id.rg_reason);
            rb_guoqi = (RadioButton) view.findViewById(R.id.rb_guoqi);
            rb_xianxia = (RadioButton) view.findViewById(R.id.rb_xianxia);
            rb_xiaohao = (RadioButton) view.findViewById(R.id.rb_xiaohao);
            rb_qita = (RadioButton) view.findViewById(R.id.rb_qita);
            rl_plus = (RelativeLayout) view.findViewById(R.id.rl_plus);
            rl_minus = (RelativeLayout) view.findViewById(R.id.rl_minus);


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
    }

    private void showContent(){
        et_inventory.setText((mProduct.getKucunliang() + mProduct.getDelta())+"");
        tv_delta.setText("变更量:"+mProduct.getDelta());
        tv_title.setText(mProduct.getName()+" "+mProduct.getNameAppend());
        rl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        rl_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inventoryNow = 0;
                if (TextUtils.isEmpty(et_inventory.getText().toString())){
                    inventoryNow = 0;
                } else {
                    inventoryNow = Integer.parseInt(et_inventory.getText().toString());
                }
                inventoryNow = inventoryNow + 1;
                if (inventoryNow < 0) inventoryNow = 0;
                if (inventoryNow > mProduct.getKucunliang()) inventoryNow = mProduct.getKucunliang();
                mProduct.setDelta(inventoryNow - mProduct.getKucunliang());
                et_inventory.setText(inventoryNow+"");
                tv_delta.setText("变更量:"+mProduct.getDelta());
            }
        });
        rl_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inventoryNow = 0;
                if (TextUtils.isEmpty(et_inventory.getText().toString())){
                    inventoryNow = 0;
                } else {
                    inventoryNow = Integer.parseInt(et_inventory.getText().toString());
                }
                inventoryNow = inventoryNow - 1;
                if (inventoryNow < 0) inventoryNow = 0;
                if (inventoryNow > mProduct.getKucunliang()) inventoryNow = mProduct.getKucunliang();
                mProduct.setDelta(inventoryNow - mProduct.getKucunliang());
                et_inventory.setText(inventoryNow+"");
                tv_delta.setText("变更量:"+mProduct.getDelta());
            }
        });
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConfirm(mProduct);
                dismiss();
            }
        });
        rg_reason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_guoqi:
                        mProduct.setReason(rb_guoqi.getText().toString());
                        break;
                    case R.id.rb_xianxia:
                        mProduct.setReason(rb_xianxia.getText().toString());
                        break;
                    case R.id.rb_xiaohao:
                        mProduct.setReason(rb_xiaohao.getText().toString());
                        break;
                    case R.id.rb_qita:
                        mProduct.setReason(rb_qita.getText().toString());
                        break;
                }
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

    public static interface KucunDialogListener{
        public void onConfirm(KucunGuanliAdapter.KucunItem item);
    }

}
