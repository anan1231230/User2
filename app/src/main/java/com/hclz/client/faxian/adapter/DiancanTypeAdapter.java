package com.hclz.client.faxian.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hclz.client.R;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.faxian.bean.TypeBean;

import java.util.List;


/**
 * Created by hjm on 16/8/10.
 */

public class DiancanTypeAdapter extends RecyclerView.Adapter<DiancanTypeAdapter.MyViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<TypeBean.SubsBean> list;
    private int mScreenWidth, mScreenHeight;

    public interface OnItemClickListener {
        void onItemClick(int position, TypeBean.SubsBean subsBean);
    }

    public DiancanTypeAdapter(Context mContext, OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.context = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<TypeBean.SubsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_diancan_type, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ImageUtility.getInstance(context).showImage(list.get(position).getIcon(), holder.picTypeIv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position, list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView picTypeIv;

        public MyViewHolder(View view) {
            super(view);
            picTypeIv = (ImageView) view.findViewById(R.id.iv_img);
            getScreenSize();
            LinearLayout.LayoutParams imgparams_full = new LinearLayout.LayoutParams(
                    mScreenWidth, (int) ((mScreenWidth - CommonUtil.convertDpToPixel(16, context)) * 512 / 1024));
            imgparams_full.setMargins(0, (int) CommonUtil.convertDpToPixel(6, context), 0, 0);
            picTypeIv.setLayoutParams(imgparams_full);
        }
    }

    private void getScreenSize() {
        Display d = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
    }
}
