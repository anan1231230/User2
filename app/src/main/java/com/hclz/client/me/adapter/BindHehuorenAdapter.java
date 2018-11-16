package com.hclz.client.me.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.bean.Kitchen;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.SharedPreferencesUtil;

import java.util.ArrayList;

/**
 * Created by handsome on 16/6/21.
 */
public class BindHehuorenAdapter extends RecyclerView.Adapter {

    Activity mContext;
    HehuorenSelectListener mListener;
    ArrayList<Kitchen> mDatas = new ArrayList<>();

    public BindHehuorenAdapter(Activity context, HehuorenSelectListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HehuorenHolder holder = new HehuorenHolder(LayoutInflater.from(mContext).inflate(R.layout.item_hehuoren, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HehuorenHolder hehuorenHolder = (HehuorenHolder) holder;
        Kitchen item = mDatas.get(position);
        ImageUtility.getInstance(mContext).showImage(item.getAlbum_thumbnail()[0], hehuorenHolder.img_logo, R.mipmap.ic_dianpu);
        hehuorenHolder.txt_name.setText(item.getTitle());
        hehuorenHolder.txt_phone.setText(item.getPhone());
        hehuorenHolder.txt_address.setText(item.getAddress());
        if (!TextUtils.isEmpty(SharedPreferencesUtil.get(mContext, ProjectConstant.APP_START_LONGITUDE))) {
            hehuorenHolder.txt_distence.setText(item.getDistanceHehuoren() + "Km");
        }else{
            hehuorenHolder.txt_distence.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setData(ArrayList<Kitchen> datas) {
        if (datas == null || datas.size() <= 0) {
            mDatas = new ArrayList<>();
        } else {
            mDatas = datas;
        }
    }

    public interface HehuorenSelectListener {
        public void onHehuorenSelected(Kitchen item);
    }

    public class HehuorenHolder extends RecyclerView.ViewHolder {

        public ImageView img_logo;
        public TextView txt_name, txt_phone, txt_address, txt_distence;

        public HehuorenHolder(View itemView) {
            super(itemView);
            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_phone = (TextView) itemView.findViewById(R.id.txt_phone);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);
            txt_distence = (TextView) itemView.findViewById(R.id.txt_distence);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onHehuorenSelected(mDatas.get(getLayoutPosition()));
                }
            });
        }
    }

}
