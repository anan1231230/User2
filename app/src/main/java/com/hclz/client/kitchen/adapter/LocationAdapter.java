package com.hclz.client.kitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.bean.Location;
import com.hclz.client.base.bean.Position;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends SanmiAdapter {
    private LayoutInflater inflater;
    private ArrayList<Location> list;

    public LocationAdapter(Context mContext) {
        super(mContext);
        this.list = new ArrayList<Location>();
        this.inflater = LayoutInflater.from(mContext);
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
        return (list != null && list.size() > 0) ? list.get(position) : null;
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
            v = inflater.inflate(R.layout.item_location_list, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) v.findViewById(R.id.tv_name);
            vh.tv_address = (TextView) v.findViewById(R.id.tv_address);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        vh.tv_name.setText(list.get(position).getName());
        vh.tv_address.setText(list.get(position).getAddress());
        return v;
    }

    public void clear() {
        this.list.clear();
    }

    public ArrayList<Location> getList() {
        return list;
    }

    public void add(String name, String address, Position position) {
        Location location = new Location();
        location.setName(name);
        location.setAddress(address);
        location.setPosition(position);
        this.list.add(location);
    }

    public void addAll(List<Location> locations) {
        this.list.addAll(locations);
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_address;
    }

}
