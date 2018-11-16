package com.hclz.client.faxian.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.faxian.bean.TypeBean;
import com.hclz.client.faxian.view.ScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjm on 16/9/20.
 */

public class LaidianAdapter extends RecyclerView.Adapter<LaidianAdapter.ItemViewHolder> {

    private List<TypeBean.SubsBean> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    public interface OnItemClickLitener {
        void onItemClick(int position, TypeBean.SubsBean subsBean);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public LaidianAdapter(Context context, OnItemClickLitener mOnItemClickLitener) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setDatas(List<TypeBean.SubsBean> datas) {
        if (datas != null){
            mDatas = datas;
        } else {
            mDatas = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public LaidianAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(R.layout.laidian_main_item_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final LaidianAdapter.ItemViewHolder holder, int position) {
        final int mPosition = position;
        //5.16隐藏品牌特色左侧标题
//        holder.tv.setText(mDatas.get(position).getName());
        holder.gridView.setAdapter(new LaidianGridViewAdapter(context, mDatas.get(position).getSubs()));
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mOnItemClickLitener.onItemClick(position, mDatas.get(mPosition).getSubs().get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ScrollGridView gridView;

        public ItemViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.item_title_tv);
            gridView = (ScrollGridView) view.findViewById(R.id.item_gridview);
        }
    }
}
