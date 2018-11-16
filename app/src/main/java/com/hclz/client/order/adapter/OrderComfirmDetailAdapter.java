package com.hclz.client.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.shouye.newcart.DiandiCartItem;

import java.util.ArrayList;

public class OrderComfirmDetailAdapter extends SanmiAdapter {
    private LayoutInflater inflater;
    private ArrayList<DiandiCartItem> list;

    public OrderComfirmDetailAdapter(Context mContext, ArrayList<DiandiCartItem> list) {
        super(mContext);
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
            v = inflater.inflate(R.layout.item_orderdetail2_list, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) v.findViewById(R.id.tv_name);
            vh.tv_num = (TextView) v.findViewById(R.id.tv_num);
            vh.tv_price = (TextView) v.findViewById(R.id.tv_price);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        vh.tv_name.setText(list.get(position).name);
        vh.tv_num.setText("x" + list.get(position).num + "");
        vh.tv_price.setText("￥" + CommonUtil.getMoney(list.get(position).price * list.get(position).num));
        return v;
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_num;
        TextView tv_price;
    }

}
