package com.hclz.client.faxian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.faxian.bean.TypeBean;

import java.util.List;

/**
 * Created by hjm on 16/9/21.
 */

public class LaidianGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<TypeBean.SubsBean> mapList;

    public LaidianGridViewAdapter(Context context, List<TypeBean.SubsBean> mapList) {
        this.context = context;
        this.mapList = mapList;
    }

    @Override
    public int getCount() {
        return mapList == null ? 0 : mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList == null ? null : mapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview, null);
            holder.mTv = (TextView) convertView.findViewById(R.id.item_gridview_name);
            holder.mIv = (ImageView) convertView.findViewById(R.id.item_gridview_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTv.setText(mapList.get(position).getName());
//        ImageUtility.getInstance(context).showImage("http://img4q.duitang.com/uploads/item/201505/28/20150528134204_fEMPr.jpeg", holder.mIv);
        ImageUtility.getInstance(context).showImage(mapList.get(position).getIcon(), holder.mIv);
        return convertView;
    }

    class ViewHolder {
        ImageView mIv;
        TextView mTv;
    }
}
