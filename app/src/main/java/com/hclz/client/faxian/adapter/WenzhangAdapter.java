package com.hclz.client.faxian.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.faxian.bean.WenzhangBean;

import java.util.ArrayList;

/**
 * Created by handsome on 16/9/27.
 */

public class WenzhangAdapter extends RecyclerView.Adapter {

    private ArrayList<WenzhangBean> mDatas;
    private WenzhangClickListener mListener;
    private Activity mContext;
    //===============屏幕宽度/屏幕高度
    protected int mScreenWidth;
    protected int mScreenHeight;

    public WenzhangAdapter(Activity context, WenzhangClickListener listener){
        mContext = context;
        mListener = listener;
        mDatas = new ArrayList<>();
        getScreenSize();
    }

    private void getScreenSize() {
        Display d = mContext.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
    }

    public void setData(ArrayList<WenzhangBean> datas){
        mDatas = datas == null ? new ArrayList<WenzhangBean>(): datas;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wenzhang, parent, false);
        RecyclerView.ViewHolder vh = new WenzhangHolder(view);
        return vh;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WenzhangHolder wenzhangHolder = (WenzhangHolder) holder;
        WenzhangBean item = mDatas.get(position);
        wenzhangHolder.tv_type.setText(item.getType());
        wenzhangHolder.tv_title.setText(item.getTitle());
        ImageUtility.getInstance(mContext).showImage(item.getPic(),wenzhangHolder.iv_img);
        if (item.isRead()) {
            wenzhangHolder.tv_title.setTextColor(ContextCompat.getColor(mContext,R.color.half_black));
        } else {
            wenzhangHolder.tv_title.setTextColor(ContextCompat.getColor(mContext,R.color.little_black));
        }
    }

    public interface WenzhangClickListener{
        void onWenzhangClick(WenzhangBean bean);
    }

    public class  WenzhangHolder extends  RecyclerView.ViewHolder implements RecyclerView.OnClickListener{

        private TextView tv_type,tv_title;
        private ImageView iv_img;

        public WenzhangHolder(View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(
                    mScreenWidth * 3 / 10, mScreenWidth * 3 / 10 * 3 / 5);
            iv_img.setLayoutParams(imgparams);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onWenzhangClick(mDatas.get(getLayoutPosition()));
                tv_title.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
            }
        }
    }
}
