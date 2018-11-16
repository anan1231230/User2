package com.hclz.client.faxian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;

import java.util.List;

/**
 * Created by hjm on 16/9/21.
 */

public class SearchHistoryListViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> mapList;
    private SearchHistoryListener mListener;

    public interface SearchHistoryListener {
        void onDeleteClick(int position);
        void onSelected(int position,String tag);
    }

    public SearchHistoryListViewAdapter(Context context, SearchHistoryListener listener) {
        this.context = context;
        this.mListener = listener;
    }

    public void setMapList(List<String> mapList) {
        this.mapList = mapList;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_history_lv, null);
            holder.mTv = (TextView) convertView.findViewById(R.id.history_tv);
            holder.mIv = (ImageView) convertView.findViewById(R.id.delete_history_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTv.setText(mapList.get(position));
        holder.mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDeleteClick(position);
                }
            }
        });
        holder.mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelected(position, mapList.get(position));
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView mIv;
        TextView mTv;
    }
}
