package com.hclz.client.me.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.order.confirmorder.bean.hehuoren.NetHehuoren;

import java.util.ArrayList;

/**
 * Created by handsome on 16/6/21.
 */
public class SelectHehuorenAdapter extends RecyclerView.Adapter {

    Activity mContext;
    HehuorenSelectListener mListener;
    ArrayList<NetHehuoren> mDatas = new ArrayList<>();

    public SelectHehuorenAdapter(Activity context, HehuorenSelectListener listener) {
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
        NetHehuoren item = mDatas.get(position);
        ImageUtility.getInstance(mContext).showImage(item.album_thumbnail[0], hehuorenHolder.img_logo, R.mipmap.ic_dianpu);
        hehuorenHolder.txt_name.setText(item.title);
        hehuorenHolder.txt_phone.setText(item.getPhone());
        hehuorenHolder.txt_address.setText(item.getAddress());
        if (item.distance > 0){
//            hehuorenHolder.txt_distence.setText(CommonUtil.getDistance(item.distance));
            hehuorenHolder.txt_distence.setText(CommonUtil.getDistance(item.distance));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setData(ArrayList<NetHehuoren> datas) {
        if (datas == null || datas.size() <= 0) {
            mDatas = new ArrayList<>();
        } else {
            mDatas = datas;
        }
    }

    public interface HehuorenSelectListener {
        public void onHehuorenSelected(NetHehuoren item);
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
