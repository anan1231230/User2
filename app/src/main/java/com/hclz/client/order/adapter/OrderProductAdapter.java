package com.hclz.client.order.adapter;        /**
 * Created by handsome on 2016/12/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.bean.Order;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;

import java.util.ArrayList;

public class OrderProductAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private Context mContext;
    private ArrayList<Order.ProductsBean> mData;
    private boolean isSetData;

    public OrderProductAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
        isSetData = false;
    }

    public void setData(ArrayList<Order.ProductsBean> data) {
        if (data != null) {
            mData = data;
            isSetData = true;
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null || mData.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (VIEW_TYPE_EMPTY == viewType) {
            holder = new OrderProductEmptyHolder(LayoutInflater.from(mContext).inflate(R.layout.empty_common_item, parent, false));
        } else {
            holder = new OrderProductViewHolder(LayoutInflater.from(
                    mContext).inflate(R.layout.item_product_inorder, parent,
                    false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (VIEW_TYPE_EMPTY == getItemViewType(position)) {
            return;
        }
        OrderProductViewHolder viewHolder = (OrderProductViewHolder) holder;
        Order.ProductsBean item = mData.get(position);
        ImageUtility.getInstance(mContext).showImage(item.album_thumbnail,viewHolder.iv_logo);
        viewHolder.tv_name.setText(item.name);
        viewHolder.tv_num.setText("x"+item.deal_count);
        viewHolder.tv_price.setText("￥" + CommonUtil.getMoney(item.deal_price));

    }

    @Override
    public int getItemCount() {
        return mData.size() > 0 ? mData.size() : isSetData ? 1 : 0;//data为空时显示长度为1,为了显示EMPTY提示
    }

    class OrderProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_logo;
        private TextView tv_name;
        private TextView tv_num;
        private TextView tv_price;

        public OrderProductViewHolder(View view) {
            super(view);
            iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_num = (TextView) view.findViewById(R.id.tv_num);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
        }

    }

    class OrderProductEmptyHolder extends RecyclerView.ViewHolder {
        public OrderProductEmptyHolder(View itemView) {
            super(itemView);
        }
    }
}
