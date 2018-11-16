package com.hclz.client.faxian.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hclz.client.R;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.faxian.bean.Type;
import com.hclz.client.faxian.listener.LaidianMainListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 16/1/22.
 */
public class LaidianMainAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_GUOJIA = 1;
    public static final int TYPE_NORMAL = 2;
    Activity mContext;
    List<Type.PromotionEntity> mDatas = new ArrayList<Type.PromotionEntity>();
    private LaidianMainListener mListener;
    private View mHeaderView;
    private View mGuojiaView;


    public LaidianMainAdapter(Activity context, LaidianMainListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void setGuojiaView(View guojiaView) {
        mGuojiaView = guojiaView;
        int pos = mHeaderView == null ? 1 : 2;
        notifyItemInserted(pos);
    }

    @Override
    public int getItemViewType(int position) {
        if (mGuojiaView == null && mHeaderView == null) return TYPE_NORMAL;
        if (mHeaderView != null && position == 0) return TYPE_HEADER;
        if (mHeaderView == null && mGuojiaView != null && position == 1) return TYPE_GUOJIA;
        if (mHeaderView != null && mGuojiaView != null && position == 2) return TYPE_GUOJIA;
        return TYPE_NORMAL;
    }

    public void setData(ArrayList<Type.PromotionEntity> datas) {
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<>();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new ProductDetailHolder(mHeaderView);
        if (mGuojiaView != null && viewType == TYPE_GUOJIA)
            return new ProductDetailHolder(mGuojiaView);
        ProductDetailHolder holder = new ProductDetailHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_laidian_main, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_GUOJIA || getItemViewType(position) == TYPE_HEADER)
            return;
        final int pos = getRealPosition(holder);
        String item = mDatas.get(pos).getPoster();
        ImageUtility.getInstance(mContext).showImage(
                item, ((ProductDetailHolder) holder).iv_img);
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        if (mHeaderView != null) {
            return mGuojiaView == null ? (position - 1) : (position - 2 < 0 ? 0 : position - 2);
        } else {
            return mGuojiaView == null ? position : (position - 1 < 0 ? 0 : position - 1);
        }
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null && mGuojiaView == null) return mDatas.size();
        if (mHeaderView != null && mGuojiaView != null) return mDatas.size() + 2;
        return mDatas.size() + 1;
    }

    public void setData(List<Type.PromotionEntity> datas) {
        mDatas = datas == null ? new ArrayList<Type.PromotionEntity>() : datas;
    }

    public void removeHeader() {
        if (mHeaderView != null) {
            mHeaderView = null;
            notifyItemRemoved(0);
        }
    }

    class ProductDetailHolder extends RecyclerView.ViewHolder {

        ImageView iv_img;

        public ProductDetailHolder(View itemView) {
            super(itemView);
            if (itemView == mGuojiaView || itemView == mHeaderView) return;
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(mDatas.get(getRealPosition(ProductDetailHolder.this)));
                }
            });
        }
    }
}
