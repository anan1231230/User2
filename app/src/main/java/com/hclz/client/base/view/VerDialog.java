package com.hclz.client.base.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hclz.client.R;

public class VerDialog {

    private Context context;
    private String verNum;
    private String[] note;
    private Item item;

    private OnNewVersionButtonClickListener mListener;

    public VerDialog(Context context, String verNum, String[] note) {
        this.context = context;
        this.verNum = verNum;
        this.note = note;
    }

    public OnNewVersionButtonClickListener getmListener() {
        return mListener;
    }

    public void setmListener(OnNewVersionButtonClickListener mListener) {
        this.mListener = mListener;
    }

    public void showDialog() {
        final AlertDialog alert = new AlertDialog.Builder(context).create();
        item = new Item();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ver, null);
        item.tv_version_num = (TextView) view.findViewById(R.id.tv_version_num);
        item.lv_notes = (ListView) view.findViewById(R.id.lv_notes);
        item.tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        item.tv_ok = (TextView) view.findViewById(R.id.tv_ok);

        item.tv_version_num.setText(context.getString(R.string.server_version_num, verNum));
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.item_note_list, note);
        item.lv_notes.setAdapter(adapter);

        item.tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onIgnoreClick();
                }
                alert.cancel();
            }
        });
        item.tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onInstallClick();
                }
                alert.cancel();
            }
        });
        alert.setView(view, 0, 0, 0, 0);
        alert.show();
    }

    public interface OnNewVersionButtonClickListener {
        void onInstallClick();

        void onIgnoreClick();
    }

    public class Item {
        TextView tv_version_num;
        ListView lv_notes;
        TextView tv_cancel;
        TextView tv_ok;
    }

}
