package com.hclz.client.me.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.bean.Chongzhika;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.me.listener.ChongzhiSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by handsome on 16/1/22.
 */
public class ChongzhiAdapter extends RecyclerView.Adapter {

    Activity mContext;
    List<Chongzhika> mDatas = new ArrayList<Chongzhika>();
    ChongzhiSelectedListener mListener;

    public ChongzhiAdapter(Activity context, ChongzhiSelectedListener chongzhiSelectedListener) {
        mContext = context;
        mListener = chongzhiSelectedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChongzhiViewHolder holder = new ChongzhiViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_chongzhika, parent,
                false), mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView tv = ((ChongzhiViewHolder) holder).tvText;
        TextView tv_des = ((ChongzhiViewHolder) holder).tvDescription;
        tv.setText(CommonUtil.getMoney(mDatas.get(position).getAmount()) + "吃货币");
        tv_des.setText("只需 ¥" + CommonUtil.getMoney(mDatas.get(position).getPrice()));
    }

    private int randomColor() {
        Random random = new Random();
        int i = random.nextInt(10);
        switch (i) {
            case 0:
                return Color.RED;
            case 1:
                return Color.CYAN;
            case 2:
                return 0xffff83fa;
            case 3:
                return 0xffee9a00;
            case 4:
                return 0xffbcd2ee;
            case 5:
                return Color.MAGENTA;
            case 6:
                return 0xff9f79ee;
            case 7:
                return 0xff8ee5ee;
            case 8:
                return 0xff8db6cd;
            case 9:
                return 0xffeead0e;
            default:
                return Color.CYAN;
        }
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setData(List<Chongzhika> datas) {
        mDatas = datas == null ? new ArrayList<Chongzhika>() : datas;
    }

    class ChongzhiViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        TextView tvText, tvDescription;
        ChongzhiSelectedListener mListener;

        public ChongzhiViewHolder(View itemView, ChongzhiSelectedListener listener) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onDaxueSelected(mDatas.get(getLayoutPosition()));
            }
        }
    }
}
