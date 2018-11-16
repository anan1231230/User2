package com.hclz.client.faxian.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.faxian.bean.Type;
import com.hclz.client.faxian.listener.TypeSelectListener;

import java.util.ArrayList;

/**
 * Created by handsome on 1/11/16.
 */
public class TypeAdapter extends RecyclerView.Adapter implements TypeSelectListener {

    //一屏显示的item数
    public static final int ITEM_ONE_SCREEN = 5;
    public int mWidth;
    public Activity mContext;
    public OnNeedScrollListener mOnNeedScrollListener;
    ArrayList<Type.Type1Entity.Type2Entity> mTypeList;
    ArrayList<Boolean> mSelectedStatus;
    TypeChangeListner mTypeChangeListener;

    public TypeAdapter(int width, Activity context, OnNeedScrollListener onNeedScrollListener, TypeChangeListner typeChangeListner) {
        mWidth = width;
        mContext = context;
        mOnNeedScrollListener = onNeedScrollListener;
        mTypeList = new ArrayList<>();
        mSelectedStatus = new ArrayList<>();
        mTypeChangeListener = typeChangeListner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_type_list2, viewGroup, false);
        RecyclerView.ViewHolder vh = new TypeViewHolder(view, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TypeViewHolder) holder).mType.setWidth(mWidth / ITEM_ONE_SCREEN);
        ((TypeViewHolder) holder).mType.setText(mTypeList.get(position).getName());
        if (mSelectedStatus.get(position) == true) {
            ((TypeViewHolder) holder).mType.setSelected(true);
        } else {
            ((TypeViewHolder) holder).mType.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return mTypeList.size();
    }

    @Override
    public void onItemSelected(View view, int position) {
        for (int i = 0; i < mSelectedStatus.size(); i++) {
            mSelectedStatus.set(i, false);
        }
        mSelectedStatus.set(position, true);
        notifyDataSetChanged();
        mTypeChangeListener.onTypeChanged(mTypeList.get(position).getName());
        mOnNeedScrollListener.onNeedScroll(position);

    }

    public ArrayList getData() {
        ArrayList<Type.Type1Entity.Type2Entity> tmp = new ArrayList<>();
        return mTypeList == null ? tmp : mTypeList;
    }

    public void setData(ArrayList<Type.Type1Entity.Type2Entity> list, String currentType) {
        this.mTypeList = list == null ? new ArrayList<Type.Type1Entity.Type2Entity>() : list;
        ArrayList<Boolean> tmpBoolean = new ArrayList<>();
        for (int i = 0; i < mTypeList.size(); i++) {
            tmpBoolean.add(false);
        }

        if (TextUtils.isEmpty(currentType)) {
            tmpBoolean.set(0, true);
        } else {
            for (int i = 0; i < mTypeList.size(); i++) {
                if (mTypeList.get(i).getName().equals(currentType)) {
                    tmpBoolean.set(i, true);
                    break;
                }
            }
        }
        mSelectedStatus = tmpBoolean;
    }

    public interface TypeChangeListner {
        void onTypeChanged(String typeId);
    }

    public interface OnNeedScrollListener {
        public void onNeedScroll(int position);
    }

    public static class TypeViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {

        public TextView mType;
        public TypeSelectListener mTypeSelectListener;

        public TypeViewHolder(View itemView, TypeSelectListener typeSelectListener) {
            super(itemView);
            mType = (TextView) itemView.findViewById(R.id.tv_name);
            mTypeSelectListener = typeSelectListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mTypeSelectListener != null) {
                mTypeSelectListener.onItemSelected(v, getLayoutPosition());
            }
        }
    }
}
