package com.hclz.client.forcshop.kucunguanli.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.ImageUtility;

import java.util.ArrayList;

/**
 * Created by handsome on 16/7/7.
 */
public class KucunGuanliAdapter extends RecyclerView.Adapter<KucunGuanliAdapter.KucunItemHolder> {

    Context mContext;
    ArrayList<KucunItem> mData;
    KucunListener mListener;

    public KucunGuanliAdapter(Context context, KucunListener listener) {
        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
    }

    public void setData(ArrayList<KucunItem> datas) {
        if (datas == null || datas.size() <= 0) {
            mData = new ArrayList<>();
        } else {
            mData = datas;
        }
    }

    @Override
    public KucunItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        KucunItemHolder holder = new KucunItemHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_kucunguanli, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(KucunItemHolder holder, int position) {
        KucunItem item = mData.get(position);
        ImageUtility.getInstance(mContext).showImage(item.getPic(), holder.img_logo, R.mipmap.ic_dianpu);
        holder.txt_name.setText(item.getName());
        holder.txt_name_append.setText((item.getNameAppend() == null ? "" : item.getNameAppend()) + " pid:" + item.getPid());
        holder.txt_invetory.setText(item.getKucunliang()+"");

        if (item.getKucunliang() <= 5) {
            holder.txt_invetory.setTextColor(Color.parseColor("#ff344d"));
        } else {
            holder.txt_invetory.setTextColor(Color.parseColor("#626262"));
        }

        if (item.getDelta() == 0) {
            holder.txt_delta.setVisibility(View.GONE);
        } else {
            holder.txt_delta.setVisibility(View.VISIBLE);
            holder.txt_delta.setText(item.getDelta() + "");
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static interface KucunListener {
        public void onItemSelected(View v, KucunItem item);
    }

    public static interface KucunItem {
        public String getPic();

        public String getName();

        public String getNameAppend();

        public String getPid();

        public int getKucunliang();

        public int getDelta();

        public void setDelta(int delta);

        public String getReason();

        public void setReason(String reason);
    }

    public class KucunItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout ll_product;
        ImageView img_logo;
        TextView txt_name, txt_name_append, txt_invetory, txt_delta;

        public KucunItemHolder(View itemView) {
            super(itemView);
            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_name_append = (TextView) itemView.findViewById(R.id.txt_name_append);
            txt_invetory = (TextView) itemView.findViewById(R.id.txt_invetory);
            txt_delta = (TextView) itemView.findViewById(R.id.txt_delta);
            ll_product = (LinearLayout) itemView.findViewById(R.id.ll_product);

            ll_product.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_product:
                    mListener.onItemSelected(v, mData.get(getLayoutPosition()));
                    break;
                default:
                    break;
            }
        }
    }


}
