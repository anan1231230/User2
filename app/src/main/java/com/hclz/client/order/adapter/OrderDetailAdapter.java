package com.hclz.client.order.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.bean.Order;
import com.hclz.client.base.bean.Product;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdapter extends SanmiAdapter {
    private LayoutInflater inflater;
    private ArrayList<Order.ProductsBean> list;
    private Context context;

    public OrderDetailAdapter(Activity mContext, List<Order.ProductsBean> products) {
        super(mContext);
        this.context = mContext;
        if (products != null){
            this.list = (ArrayList<Order.ProductsBean>) products;
        } else {
            this.list = new ArrayList<>();
        }
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
            v = inflater.inflate(R.layout.item_orderdetail_list, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) v.findViewById(R.id.tv_name);
            vh.tv_num = (TextView) v.findViewById(R.id.tv_num);
            vh.tv_price = (TextView) v.findViewById(R.id.tv_price);
            vh.iv_img = (ImageView) v.findViewById(R.id.iv_img);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        if (TextUtils.isEmpty(list.get(position).name_append)) {
            vh.tv_name.setText(list.get(position).name);
        } else {
            vh.tv_name.setText(list.get(position).name + "(" + list.get(position).name_append + ")");
        }
        ImageUtility.getInstance(context).showImage(list.get(position).album_thumbnail.replace("[\"","").replace("\"]","").replace("\"","").split(",")[0],vh.iv_img);
        vh.tv_num.setText("X" + list.get(position).deal_count + "");
        vh.tv_price.setText("￥" + CommonUtil.getMoney(list.get(position).deal_price));
        return v;
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_num;
        TextView tv_price;
        ImageView iv_img;
    }

}
