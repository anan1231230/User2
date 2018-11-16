package com.hclz.client.kitchen.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.bean.Kitchen;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;

import java.util.ArrayList;

/**
 * Created by handsome on 16/5/10.
 */
public class KitchenSearchAdapter extends RecyclerView.Adapter {

    public static final int VIEW_TYPE_RECENT_USE_TITLE = 1001;
    public static final int VIEW_TYPE_NEARBY_TITLE = 1002;
    public static final int VIEW_TYPE_SEARCH_RESULT_TITLE = 1003;
    public static final int VIEW_TYPE_NORMAL = 1004;
    Activity mContext;
    ArrayList<Kitchen> mDatas = new ArrayList<Kitchen>();
    KitchenSelectedListener mListener;

    public KitchenSearchAdapter(Activity context, KitchenSelectedListener kitchenSelectedListener) {
        mListener = kitchenSelectedListener;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        Kitchen item = mDatas.get(position);
        return item.getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        KitchenViewHolder holder = null;
        switch (viewType) {
            case VIEW_TYPE_NEARBY_TITLE:
                holder = new KitchenViewHolder(LayoutInflater.from(
                        mContext).inflate(R.layout.item_kitchen_list_titlefujin, parent,
                        false), mListener);
                break;
            case VIEW_TYPE_NORMAL:
                holder = new KitchenViewHolder(LayoutInflater.from(
                        mContext).inflate(R.layout.item_kitchen_list, parent,
                        false), mListener);
                break;
            case VIEW_TYPE_RECENT_USE_TITLE:
                holder = new KitchenViewHolder(LayoutInflater.from(
                        mContext).inflate(R.layout.item_kitchen_list_titletuijian, parent,
                        false), mListener);
                break;
            case VIEW_TYPE_SEARCH_RESULT_TITLE:
                holder = new KitchenViewHolder(LayoutInflater.from(
                        mContext).inflate(R.layout.item_kitchen_list_titlesearch, parent,
                        false), mListener);
                break;
            default:
                holder = new KitchenViewHolder(LayoutInflater.from(
                        mContext).inflate(R.layout.item_kitchen_list, parent,
                        false), mListener);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageUtility.getInstance(mContext).showImage(mDatas.get(position).getAlbum_thumbnail()[0] == null ? "" : mDatas.get(position).getAlbum_thumbnail()[0], ((KitchenViewHolder) holder).img_logo, R.mipmap.perm_pic);
        ((KitchenViewHolder) holder).txt_name.setText(mDatas.get(position).getTitle());
        ((KitchenViewHolder) holder).txt_address.setText(mDatas.get(position).getAddress());
        ((KitchenViewHolder) holder).txt_juli.setText(CommonUtil.getDistance(mDatas.get(position).getDistance()));
        ((KitchenViewHolder) holder).txt_phone.setText(mDatas.get(position).getContact() + "(" + mDatas.get(position).getPhone() + ")");

        if (mDatas.get(position).getPromotions() != null) {
            ((KitchenViewHolder) holder).ll_huodong_list.setVisibility(View.VISIBLE);
            HuoDongAdapter adapter = new HuoDongAdapter(mContext, mDatas.get(position).getPromotions());
            ((KitchenViewHolder) holder).lv_huodong.setAdapter(adapter);
        } else {
            ((KitchenViewHolder) holder).ll_huodong_list.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setData(ArrayList<Kitchen> datas) {
        mDatas = datas == null ? new ArrayList<Kitchen>() : datas;
    }

    class KitchenViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        ImageView img_logo;
        TextView txt_name, txt_juli, txt_phone, txt_address;
        ListView lv_huodong;
        KitchenSelectedListener mListener;
        LinearLayout ll_huodong_list;

        public KitchenViewHolder(View itemView, KitchenSelectedListener listener) {
            super(itemView);

            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_juli = (TextView) itemView.findViewById(R.id.txt_juli);
            txt_phone = (TextView) itemView.findViewById(R.id.txt_phone);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);
            lv_huodong = (ListView) itemView.findViewById(R.id.lv_huodong);
            ll_huodong_list = (LinearLayout) itemView.findViewById(R.id.ll_huodong_list);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onKitchenSelected(mDatas.get(getLayoutPosition()));
            }
        }
    }
}
