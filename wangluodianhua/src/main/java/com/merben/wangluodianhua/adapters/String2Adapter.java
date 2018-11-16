package com.merben.wangluodianhua.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.merben.wangluodianhua.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 16/1/22.
 */
public class String2Adapter extends BaseAdapter {

    LayoutInflater mInflater;
    Activity mContext;
    List<String> mDatas = new ArrayList<>();

    public String2Adapter(Activity context, List<String> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
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
            convertView = mInflater.inflate(R.layout.item_xing, parent, false);
            holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_text.setText(mDatas.get(position));
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_text;
    }
}
