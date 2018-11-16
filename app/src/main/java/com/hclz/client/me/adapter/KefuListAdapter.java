package com.hclz.client.me.adapter;
/**
 * Created by handsome on 2016/12/20.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.view.CircleImageView;
import com.hclz.client.me.bean.KefuListBean;

import java.util.ArrayList;

public class KefuListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private Context mContext;
    private KefuListListener mListener;
    private ArrayList<KefuListBean> mData;
    private boolean isSetData;

    public KefuListAdapter(Context context, KefuListListener listener) {
        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
        isSetData = false;
    }

    public void setData(ArrayList<KefuListBean> data) {
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
            holder = new KefuListEmptyHolder(LayoutInflater.from(mContext).inflate(R.layout.empty_common_item, parent, false));
        } else {
            holder = new KefuListViewHolder(LayoutInflater.from(
                    mContext).inflate(R.layout.item_kefu, parent,
                    false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (VIEW_TYPE_EMPTY == getItemViewType(position)) {
            return;
        }
        KefuListViewHolder viewHolder = (KefuListViewHolder) holder;
        KefuListBean item = mData.get(position);
        viewHolder.tv_name.setText(item.name);

    }

    @Override
    public int getItemCount() {
        return mData.size() > 0 ? mData.size() : isSetData ? 1 : 0;//data为空时显示长度为1,为了显示EMPTY提示
    }


    public interface KefuListListener {
        void onItemSelected(int position, KefuListBean item);
    }

    class KefuListViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView iv_img;
        private TextView tv_name;

        public KefuListViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_img = (CircleImageView) view.findViewById(R.id.iv_img);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(getLayoutPosition(), mData.get(getLayoutPosition()));
                }
            });
        }

    }

    class KefuListEmptyHolder extends RecyclerView.ViewHolder {
        public KefuListEmptyHolder(View itemView) {
            super(itemView);
        }
    }
}

