package com.hclz.client.kitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.mall.bean.MallType;

import java.util.ArrayList;

public class TypeAdapter extends SanmiAdapter {
    private LayoutInflater inflater;
    private ArrayList<MallType.Type1Entity.Type2Entity> list;
    private int nowPos = 0;

    public TypeAdapter(Context mContext, ArrayList<MallType.Type1Entity.Type2Entity> list) {
        super(mContext);
        this.list = list;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isEmpty() {
        if (list == null || list != null && list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getCount() {
        if (list.size() > 0) {
            return list.size();
        } else {
            return VIEWTYPE_EMPTY;
        }
    }

    @Override
    public Object getItem(int position) {
        return (list != null && !list.isEmpty()) ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return getEmptyView(parent);
        }
        View v = convertView;
        ViewHolder vh = null;
        if (v == null || v.getTag(R.id.VIEWTYPE_NORMAL) == null) {
            v = inflater.inflate(R.layout.item_type_list, null);
            vh = new ViewHolder();
            vh.txt_name = (TextView) v.findViewById(R.id.tv_name);
            vh.tv_count = (TextView) v.findViewById(R.id.tv_count);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        if (nowPos == position) {
            vh.txt_name.setBackgroundResource(R.drawable.listitem_solid2);
        } else {
            vh.txt_name.setBackgroundResource(R.drawable.listitem_solid);
        }
        vh.txt_name.setText(list.get(position).getName());
        int count = 0;
        for (int i = 0; i < list.get(position).getProducts().size(); i++) {
            count += list.get(position).getProducts().get(i).getCount();
        }
        list.get(position).setCount(count);
        if (count <= 0) {
            vh.tv_count.setVisibility(View.INVISIBLE);
        } else {
            vh.tv_count.setVisibility(View.VISIBLE);
            vh.tv_count.getPaint().setAntiAlias(true);
            vh.tv_count.setText(count + "");
        }
        return v;
    }

    public void setSelected(int pos) {
        nowPos = pos;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView txt_name;
        TextView tv_count;
    }

}
