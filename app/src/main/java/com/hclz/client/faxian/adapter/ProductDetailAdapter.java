package com.hclz.client.faxian.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hclz.client.R;
import com.hclz.client.base.util.ImageUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 16/1/22.
 */
public class ProductDetailAdapter extends RecyclerView.Adapter {

    Activity mContext;
    List<String> mDatas = new ArrayList<String>();


    public ProductDetailAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProductDetailHolder holder = new ProductDetailHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_product_detail, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        String item = mDatas.get(position);
        ProductDetailHolder itemHolder = ((ProductDetailHolder) holder);
        ImageUtility.getInstance(mContext).showImage(
                item, ((ProductDetailHolder) holder).iv_img);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setData(List<String> datas) {
        mDatas = datas == null ? new ArrayList<String>() : datas;
    }

    class ProductDetailHolder extends RecyclerView.ViewHolder {

        ImageView iv_img;

        public ProductDetailHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
        }
    }
}
