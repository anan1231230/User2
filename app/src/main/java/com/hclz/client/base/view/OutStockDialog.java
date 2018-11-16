package com.hclz.client.base.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.shouye.newcart.DiandiCart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hjm on 16/7/21.
 */

public class OutStockDialog {

    private AlertDialog alertDialog;
    private static OutStockDialog outStockDialog = new OutStockDialog();
    public final static int OUT_STOCK = 1;
    public final static int PRICE_CHANGE = 2;

    private OutStockDialog() {
    }

    public static OutStockDialog getInstance() {
        return outStockDialog;
    }

    public void showDialog(Context context, String title, Map<String, Integer> map, int type) {
        showDialog(context, title, map, type, 1);
    }

    public void showDialog(Context context, String title, Map<String, Integer> map, int type, int isJiaju) {
        View view = LayoutInflater.from(context).inflate(R.layout.out_stock_dialog_layout, null);
        TextView titleContent = (TextView) view.findViewById(R.id.title);
        LinearLayout listLayout = (LinearLayout) view.findViewById(R.id.list_layout);
        if (type == OUT_STOCK) {//缺货提醒{当前以下产品库存不足}
            titleContent.setText("当前以下产品库存不足:");
            for (String key : map.keySet()) {
                LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.out_stock_item_layout, null);
                TextView name = (TextView) itemLayout.findViewById(R.id.name);
                name.setText(DiandiCart.getInstance().get(key).name);
                TextView num = (TextView) itemLayout.findViewById(R.id.num);
                num.setText("只剩 " + map.get(key) + " 件");
                listLayout.addView(itemLayout);
            }
        } else {//PRICE_CHANGE 价格变动提醒{当前以下产品价格变动}
            titleContent.setText("当前以下产品价格变动:");
            for (String key : map.keySet()) {
                LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.out_stock_item_layout, null);
                TextView name = (TextView) itemLayout.findViewById(R.id.name);
                name.setText(DiandiCart.getInstance().get(key).name);
                TextView num = (TextView) itemLayout.findViewById(R.id.num);
                num.setText("现价 " + CommonUtil.getMoney(map.get(key)));
                listLayout.addView(itemLayout);
            }
        }
        alertDialog = new AlertDialog.Builder(context).setTitle(title).setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopDialog();
            }
        }).show();
    }

    public void stopDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public interface OnPositiveListener {
        void OnPositive();
    }
}
