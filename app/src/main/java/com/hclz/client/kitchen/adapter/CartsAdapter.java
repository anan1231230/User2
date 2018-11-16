package com.hclz.client.kitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.bean.Product;
import com.hclz.client.base.util.CommonUtil;

import java.util.ArrayList;

public class CartsAdapter extends SanmiAdapter {
    private LayoutInflater inflater;
    private ArrayList<Product> list;
    private int priceNum = 3;
    private onCartsItemClickListener mListener;

    public CartsAdapter(Context mContext, ArrayList<Product> list, int priceNum) {
        super(mContext);
        this.list = list;
        this.priceNum = priceNum;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public boolean isEmpty() {
        if (list == null || list != null && list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getCount() {
        if (list != null && list.size() > 0) {
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
        final Product product = list.get(position);
        View v = convertView;
        ViewHolder vh = null;
        if (v == null || v.getTag(R.id.VIEWTYPE_NORMAL) == null) {
            v = inflater.inflate(R.layout.item_carts_productde_list, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) v.findViewById(R.id.tv_name);
            vh.tv_num = (TextView) v.findViewById(R.id.tv_num);
            vh.tv_price = (TextView) v.findViewById(R.id.tv_price);

            vh.tv_del = (TextView) v.findViewById(R.id.tv_del);
            vh.tv_add = (TextView) v.findViewById(R.id.tv_add);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        vh.tv_name.setText(product.getName());
        vh.tv_num.setText(product.getCount() + "");
        vh.tv_price.setText((product.getCount() * product.getPrice()[priceNum] * 0.01) + "");
        vh.tv_price.setText(CommonUtil.getMoney(product.getCount() * product.getPrice()[priceNum]) + "");
        if (product.getCount() == 0) {
            vh.tv_num.setVisibility(View.GONE);
            vh.tv_del.setVisibility(View.GONE);
        } else {
            vh.tv_num.setVisibility(View.VISIBLE);
            vh.tv_del.setVisibility(View.VISIBLE);
        }

        vh.tv_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    product.setCount((product.getCount() - 1));
                    notifyDataSetChanged();
                    mListener.delClick(product);
                }
            }
        });
        vh.tv_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getCount() >= product.getInventory()) {
                    return;
                }
                if (mListener != null) {
                    product.setCount((product.getCount() + 1));
                    notifyDataSetChanged();
                    mListener.addClick(product);
                }
            }
        });
        return v;
    }

    public onCartsItemClickListener getmListener() {
        return mListener;
    }

    public void setmListener(onCartsItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface onCartsItemClickListener {
        void addClick(Product product);

        void delClick(Product product);
    }

    class ViewHolder {
        TextView tv_name, tv_num, tv_price;
        TextView tv_del, tv_add;
    }

}
