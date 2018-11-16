package com.hclz.client.order.adapter;        /**
 * Created by handsome on 2016/12/27.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.bean.Order;

import java.util.ArrayList;

public class WuliuListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private Context mContext;
    private WuliuListListener mListener;
    private ArrayList<Order.StatusDetailBean> mData;
    private boolean isSetData;

    public WuliuListAdapter(Context context, WuliuListListener listener) {
        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
        isSetData = false;
    }

    public void setData(ArrayList<Order.StatusDetailBean> data) {
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
            holder = new WuliuListEmptyHolder(LayoutInflater.from(mContext).inflate(R.layout.empty_common_item, parent, false));
        } else {
            holder = new WuliuListViewHolder(LayoutInflater.from(
                    mContext).inflate(R.layout.item_wuliu_list, parent,
                    false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (VIEW_TYPE_EMPTY == getItemViewType(position)) {
            return;
        }
        WuliuListViewHolder viewHolder = (WuliuListViewHolder) holder;
        Order.StatusDetailBean item = mData.get(position);
        viewHolder.tv_status.setText(item.desc);
        viewHolder.tv_time.setText(item.timestamp);

        if (position == 0) {
            viewHolder.iv_status.setImageResource(R.mipmap.iv_wuliu_teshu);
            viewHolder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.blue));
        } else {
            viewHolder.iv_status.setImageResource(R.mipmap.iv_wuliu_putong);
            viewHolder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.gray_3));
        }

    }

    @Override
    public int getItemCount() {
        return mData.size() > 0 ? mData.size() : isSetData ? 1 : 0;//data为空时显示长度为1,为了显示EMPTY提示
    }


    public interface WuliuListListener {
        void onItemSelected(int position, Order.StatusDetailBean item);
    }

    class WuliuListViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_status;
        private TextView tv_status;
        private TextView tv_time;

        public WuliuListViewHolder(View view) {
            super(view);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            iv_status = (ImageView) view.findViewById(R.id.iv_status);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(getLayoutPosition(), mData.get(getLayoutPosition()));
                }
            });
        }

    }

    class WuliuListEmptyHolder extends RecyclerView.ViewHolder {
        public WuliuListEmptyHolder(View itemView) {
            super(itemView);
        }
    }
}

