package com.hclz.client.faxian.adapter;

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

/**
 * Created by handsome on 16/8/1.
 */
public class JiajuType3Adapter extends RecyclerView.Adapter<JiajuType3Adapter.Type3ViewHolder>{

    List<TypeBean.SubsBean> mData;
    Context mContext;
    Type3Listener mListener;

    public JiajuType3Adapter(Context context, Type3Listener listener){
        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
    }

    public void setData(List<TypeBean.SubsBean> types){
        if (types == null){
            mData = new ArrayList<>();
        } else {
            mData = types;
        }
    }

    @Override
    public Type3ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Type3ViewHolder holder = new Type3ViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_type3_jiaju, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(Type3ViewHolder holder, int position) {
        TypeBean.SubsBean item = mData.get(position);
        holder.tv_name.setText(item.getName());
        ImageUtility.getInstance(mContext).showImage(item.getIcon(),holder.iv_pic);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface Type3Listener{
        public void onItemSelected(TypeBean.SubsBean type3);
    }
    public class Type3ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        ImageView iv_pic;
        public Type3ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);
            iv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(mData.get(getLayoutPosition()));
                }
            });
        }
    }
}
