package com.hclz.client.faxian.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.faxian.bean.TypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 16/8/1.
 */
public class JiajuType2Adapter extends RecyclerView.Adapter<JiajuType2Adapter.Type2ViewHolder> {

    List<TypeBean.SubsBean> mData;
    Context mContext;
    Type2Listener mListener;

    TextView last_selected,last_selecteName;


    public JiajuType2Adapter(Context context, Type2Listener listener) {
        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
    }

    public void setData(List<TypeBean.SubsBean> types) {
        if (types == null) {
            mData = new ArrayList<>();
        } else {
            mData = types;
            for (TypeBean.SubsBean item : mData) {
                item.setSelected(false);
            }
            if (mData.size() > 0){
                mData.get(0).setSelected(true);
            }
        }
    }

    @Override
    public Type2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Type2ViewHolder holder = new Type2ViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_type2_jiaju, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(Type2ViewHolder holder, int position) {
        TypeBean.SubsBean item = mData.get(position);
        holder.tv_status.setSelected(item.isSelected());
        holder.tv_name.setSelected(item.isSelected());
        if (item.isSelected()) {
            last_selected = holder.tv_status;
            last_selecteName = holder.tv_name;
        }
        holder.tv_name.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface Type2Listener {
        public void onItemSelected(TypeBean.SubsBean type2);
    }

    public class Type2ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_status;

        public Type2ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (last_selected != null) {
                        last_selected.setSelected(false);
                        last_selecteName.setSelected(false);
                    }
                    tv_status.setSelected(true);
                    tv_name.setSelected(true);
                    last_selected = tv_status;
                    last_selecteName = tv_name;
                    for (TypeBean.SubsBean item : mData) {
                        item.setSelected(false);
                    }
                    mData.get(getLayoutPosition()).setSelected(true);
                    mListener.onItemSelected(mData.get(getLayoutPosition()));
                }
            });
        }
    }
}
