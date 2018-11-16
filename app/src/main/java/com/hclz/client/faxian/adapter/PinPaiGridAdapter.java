package com.hclz.client.faxian.adapter;        /**
 * Created by handsome on 2016/12/19.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.faxian.bean.TypeBean;

import java.util.ArrayList;
import java.util.List;

public class PinPaiGridAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private Context mContext;
    private ZujiListener mListener;
    private List<TypeBean.SubsBean> mData;
    private boolean isSetData;

    public PinPaiGridAdapter(Context context, ZujiListener listener) {
        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
        isSetData = false;
    }

    public void setData(List<TypeBean.SubsBean> data) {
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
            holder = new ZujiEmptyHolder(LayoutInflater.from(mContext).inflate(R.layout.empty_common_item, parent, false));
        } else {
            holder = new ZujiViewHolder(LayoutInflater.from(
                    mContext).inflate(R.layout.item_pinpai, parent,
                    false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (VIEW_TYPE_EMPTY == getItemViewType(position)) {
            return;
        }
        ZujiViewHolder viewHolder = (ZujiViewHolder) holder;
        TypeBean.SubsBean item = mData.get(position);
        viewHolder.tv_name.setText(item.getName());
        ImageUtility.getInstance(mContext).showImage(item.getIcon(), viewHolder.iv_img);

    }

    @Override
    public int getItemCount() {
        return mData.size() > 0 ? mData.size() : isSetData ? 1 : 0;//data为空时显示长度为1,为了显示EMPTY提示
    }


    public interface ZujiListener {
        void onItemSelected(int position, TypeBean.SubsBean item);
//        void delHistory(int position, TypeBean.SubsBean item);
    }

    class ZujiViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_img;
        private TextView tv_name, iv_delete;

        public ZujiViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
            iv_delete = (TextView) view.findViewById(R.id.iv_delete);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(getLayoutPosition(), mData.get(getLayoutPosition()));
                }
            });
//            iv_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.delHistory(getLayoutPosition(),mData.get(getLayoutPosition()));
//                }
//            });
        }

    }

    class ZujiEmptyHolder extends RecyclerView.ViewHolder {
        public ZujiEmptyHolder(View itemView) {
            super(itemView);
        }
    }
}

