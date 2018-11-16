package com.hclz.client.kitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.kitchen.permlink.PermlinkItem;

import java.util.ArrayList;
import java.util.List;

public class PermAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<PermlinkItem> mPermlinkItems;

    public PermAdapter(Context context, ArrayList<PermlinkItem> permlinkItems) {
        this.mContext = context;
        this.mPermlinkItems = permlinkItems;
    }

    @Override
    public int getCount() {
        return getData().size();
    }

    @Override
    public Object getItem(int position) {
        return getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        viewHolder = new ViewHolder();
        if (convertView == null || convertView.getTag(R.id.VIEWTYPE_NORMAL) == null) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_dshoppermlink, null);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.iv_no = (ImageView) convertView.findViewById(R.id.iv_no);
            viewHolder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(R.id.VIEWTYPE_NORMAL, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.id.VIEWTYPE_NORMAL);
        }

        PermlinkItem permlink = getData().get(position);

        if (permlink != null) {
            String url = permlink.icon;
            if (url != null) {
                ImageUtility.getInstance(mContext).showImage(url, viewHolder.iv_icon);
            }

            if (permlink.hint != null) {
                viewHolder.tv_text.setText(permlink.hint);
            }

            if (permlink.enabled == false) {
                viewHolder.iv_no.setVisibility(View.VISIBLE);
            } else {
                viewHolder.iv_no.setVisibility(View.GONE);
            }

        }

        return convertView;
    }

    private List<PermlinkItem> getData() {
        List<PermlinkItem> permlinks = new ArrayList<PermlinkItem>();
        if (mPermlinkItems == null) {
            return permlinks;
        } else {

            return mPermlinkItems;
        }
    }

    private static class ViewHolder {
        ImageView iv_icon;
        ImageView iv_no;
        TextView tv_text;
    }

}
