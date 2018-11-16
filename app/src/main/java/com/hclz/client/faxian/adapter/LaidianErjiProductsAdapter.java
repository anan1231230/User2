package com.hclz.client.faxian.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.faxian.bean.Type;
import com.hclz.client.faxian.listener.LaidianErjiProductsSelectListener;
import com.hclz.client.faxian.view.WordWrapView;

import java.util.ArrayList;
import java.util.List;

public class LaidianErjiProductsAdapter extends RecyclerView.Adapter<LaidianErjiProductsAdapter.ProductDetailHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_HEADER2 = 1;
    public static final int TYPE_NORMAL = 2;
    @SuppressWarnings("unused")
    private static final String TAG = LaidianErjiProductsAdapter.class.getSimpleName();
    Activity mContext;
    List<Type.Type1Entity.Type2Entity> mDatas = new ArrayList<Type.Type1Entity.Type2Entity>();
    private LaidianErjiProductsSelectListener mListener;
    private View mHeaderView, mTypeHeaderView;


    public LaidianErjiProductsAdapter(Activity context, LaidianErjiProductsSelectListener listener) {
        mContext = context;
        mListener = listener;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void setTypeHeaderView(View typeHeaderView) {
        mTypeHeaderView = typeHeaderView;
        notifyItemInserted(0);
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) {
            if (mTypeHeaderView == null) {
                return TYPE_NORMAL;
            } else if (position == 0) {
                return TYPE_HEADER2;
            } else {
                return TYPE_NORMAL;
            }
        } else {
            if (mTypeHeaderView == null) {
                if (position == 0) {
                    return TYPE_HEADER;
                } else {
                    return TYPE_NORMAL;
                }
            } else {
                if (position == 0) {
                    return TYPE_HEADER;
                } else if (position == 1) {
                    return TYPE_HEADER2;
                } else {
                    return TYPE_NORMAL;
                }
            }
        }
    }

    @Override
    public ProductDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new ProductDetailHolder(mHeaderView);
        if (mTypeHeaderView != null && viewType == TYPE_HEADER2)
            return new ProductDetailHolder(mTypeHeaderView);
        ProductDetailHolder holder = new ProductDetailHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_erji_product_detail, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductDetailHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_HEADER2)
            return;
        final int pos = getRealPosition(holder);
        final Type.Type1Entity.Type2Entity item = mDatas.get(pos);
        ProductDetailHolder itemHolder = ((ProductDetailHolder) holder);
        ImageUtility.getInstance(mContext).showImage(
                item.getIcon(), itemHolder.iv_img);
        if (item.getType3() != null && item.getType3().size() > 0) {
            itemHolder.mWrapView.removeAllViews();
            for (Type.Type1Entity.Type2Entity.Type3Entity type3 : item.getType3()) {
                final Type.Type1Entity.Type2Entity.Type3Entity tmp = type3;
                TextView textview = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_type3_string, null);
                textview.setText(type3.getName());
                textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClick(item, tmp);
                    }
                });
                itemHolder.mWrapView.addView(textview);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mTypeHeaderView == null ? mDatas.size() : mDatas.size() + 1 : mDatas.size() + 2;
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? mTypeHeaderView == null ? position : position - 1 : position - 2;
    }


    public void setData(List<Type.Type1Entity.Type2Entity> datas) {
        mDatas = datas == null ? new ArrayList<Type.Type1Entity.Type2Entity>() : datas;
    }

    public class ProductDetailHolder extends RecyclerView.ViewHolder {

        ImageView iv_img;
        WordWrapView mWrapView;

        public ProductDetailHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView || itemView == mTypeHeaderView) return;
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            mWrapView = (WordWrapView) itemView.findViewById(R.id.word_wrap);
            iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(mDatas.get(getRealPosition(ProductDetailHolder.this)));
                }
            });
        }
    }
}
