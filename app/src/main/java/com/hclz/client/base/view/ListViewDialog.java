package com.hclz.client.base.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hclz.client.R;

public class ListViewDialog {

    private Context context;
    private String title;
    private BaseAdapter adapter;
    private Item item;

    private OnDialogListItemClickListener mListener;

    public ListViewDialog(Context context, String title, BaseAdapter adapter) {
        this.context = context;
        this.title = title;
        this.adapter = adapter;
    }

    public void showDialog() {
        final AlertDialog alert = new AlertDialog.Builder(context, R.style.mydialog).create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.setCanceledOnTouchOutside(true);
        item = new Item();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_listview, null);
        item.tv_title = (TextView) view.findViewById(R.id.tv_title);
        item.lv_content = (ListView) view.findViewById(R.id.lv_content);
        item.tv_title.setText(title);
        if (adapter != null) {
            item.lv_content.setAdapter(adapter);
            item.lv_content.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    if (mListener != null) {
                        if (adapter.getItem(position) != null) {
                            mListener.onDialogListItemClick(adapter.getItem(position));
                        }
                    }
                    alert.dismiss();
                }
            });
        }
        alert.show();
        alert.setContentView(view);

        WindowManager.LayoutParams params = alert.getWindow().getAttributes();
        params.height = 500;
        alert.getWindow().setAttributes(params);
    }

    public OnDialogListItemClickListener getmListener() {
        return mListener;
    }

    public void setmListener(OnDialogListItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnDialogListItemClickListener {
        void onDialogListItemClick(Object object);
    }

    public class Item {
        TextView tv_title;
        ListView lv_content;
    }

    ;

}
