package com.hclz.client.order.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.bean.Order;

import java.util.ArrayList;

public class LogisticsAdapter extends SanmiAdapter {
    private LayoutInflater inflater;
    private ArrayList<Order.StatusDetailBean> list;

    public LogisticsAdapter(Activity mContext, ArrayList<Order.StatusDetailBean> list) {
        super(mContext);
        this.mContext = mContext;
        this.list = list;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public boolean isEmpty() {
        if (list == null || list != null && list.size() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getCount() {
        if (list.size() > 0) {
            return list.size();
        } else {
            return VIEWTYPE_EMPTY;
        }
    }

    @Override
    public Object getItem(int position) {
        return list != null ? list.get(position) : null;
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
            v = inflater.inflate(R.layout.item_logistics_list, null);
            vh = new ViewHolder();
            vh.tv_data = (TextView) v.findViewById(R.id.tv_data);
            vh.tv_status = (TextView) v.findViewById(R.id.tv_status);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        vh.tv_data.setText(list.get(position).timestamp);
        vh.tv_status.setText(list.get(position).desc);
        return v;
    }

    class ViewHolder {
        TextView tv_data, tv_status;
    }

}
