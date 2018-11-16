package com.hclz.client.forcshop.jiedandetail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by handsome on 16/7/7.
 */
public class ProductInOrderAdapter extends RecyclerView.Adapter<ProductInOrderAdapter.ProductInOrderHolder> {
    Context mContext;
    ArrayList<ProductInOrderItem> mData;

    public ProductInOrderAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public void setData(ArrayList<ProductInOrderItem> datas) {
        if (datas == null || datas.size() <= 0) {
            mData = new ArrayList<>();
        } else {
            mData = datas;
        }
    }

    @Override
    public ProductInOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProductInOrderHolder holder = new ProductInOrderHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_product_inorder, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductInOrderHolder holder, int position) {
        ProductInOrderItem item = mData.get(position);
        holder.tv_name.setText(item.getName());
        holder.tv_num.setText("X"+item.getNum());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static interface ProductInOrderItem extends Serializable{
        public String getName();
        public int getNum();
    }

    public class ProductInOrderHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_num;

        public ProductInOrderHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }
}
