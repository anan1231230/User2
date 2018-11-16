package com.hclz.client.me.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.me.AddressActivity;
import com.hclz.client.order.confirmorder.bean.address.NetAddress;

import java.util.ArrayList;

public class AddressAdapter extends SanmiAdapter {

    private LayoutInflater mInflater;
    private ArrayList<NetAddress> list;

    public AddressAdapter(Context mContext, ArrayList<NetAddress> list) {
        super(mContext);
        this.mInflater = LayoutInflater.from(mContext);
        this.list = list;
    }

    @Override
    public boolean isEmpty() {
        if (list == null || list != null && list.size() <= 0) {
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
        return list != null ? list.get(position) : null;
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
        final NetAddress address = list.get(position);
        if (v == null || v.getTag(R.id.VIEWTYPE_NORMAL) == null) {
            v = mInflater.inflate(R.layout.item_address_list, null);
            vh = new ViewHolder();
            vh.txt_name = (TextView) v.findViewById(R.id.txt_name);
            vh.txt_phone = (TextView) v.findViewById(R.id.txt_phone);
            vh.txt_address = (TextView) v.findViewById(R.id.txt_address);
            vh.tv_moren = (TextView) v.findViewById(R.id.tv_moren);
            vh.ll_bijian = (LinearLayout) v.findViewById(R.id.ll_bijian);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        vh.txt_name.setText(address.getName());
        vh.txt_phone.setText(address.getPhone());
        if (TextUtils.isEmpty(address.province)) {
            vh.txt_address.setText("地址模块已升级，之前的地址已不可用，请重新编辑");
            vh.txt_address.setTextColor(Color.RED);
        } else {
            vh.txt_address.setText(address.getAddress());
            vh.txt_address.setTextColor(ContextCompat.getColor(mContext,R.color.color626262));
        }

        if (address.is_default == 1) {
            vh.tv_moren.setVisibility(View.VISIBLE);
        } else {
            vh.tv_moren.setVisibility(View.GONE);
        }

        vh.ll_bijian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressActivity.startMe(mContext, address);
            }
        });
        return v;
    }

    class ViewHolder {
        TextView txt_name, txt_phone, txt_address;
        TextView tv_moren;
        LinearLayout ll_bijian;
    }


}
