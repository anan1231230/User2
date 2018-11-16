package com.hclz.client.kitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.util.ImageUtility;

public class HuoDongAdapter extends SanmiAdapter {
    private LayoutInflater inflater;
    private String[][] list;

    public HuoDongAdapter(Context mContext, String[][] list) {
        super(mContext);
        this.mContext = mContext;
        this.list = list;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public boolean isEmpty() {
        if (list == null || list != null && list.length <= 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getCount() {
        if (list.length > 0) {
            return list.length;
        } else {
            return VIEWTYPE_EMPTY;
        }
    }

    @Override
    public Object getItem(int position) {
        return list != null ? list[position] : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return getEmptyView(parent);
        }
        View v = convertView;
        ViewHolder vh = null;
        if (v == null || v.getTag(R.id.VIEWTYPE_NORMAL) == null) {
            v = inflater.inflate(R.layout.item_huodong_list, null);
            vh = new ViewHolder();
            vh.tv_huodong = (TextView) v.findViewById(R.id.tv_huodong);
            vh.iv_huodong = (ImageView) v.findViewById(R.id.iv_huodong);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        ImageUtility.getInstance(mContext).showImage(list[position][0], vh.iv_huodong);
        vh.tv_huodong.setText(list[position][1]);
        return v;
    }

    class ViewHolder {
        TextView tv_huodong;
        ImageView iv_huodong;
    }

}
