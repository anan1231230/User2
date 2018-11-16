package com.hclz.client.kitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.bean.Kitchen;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.view.CircleImageView;

import java.util.ArrayList;

//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.utils.DistanceUtil;

public class KitchenAdapter extends SanmiAdapter {
    private LayoutInflater inflater;
    private ArrayList<Kitchen> list;

    public KitchenAdapter(Context mContext) {
        super(mContext);
        this.list = new ArrayList<Kitchen>();
        this.inflater = LayoutInflater.from(mContext);
    }

    public KitchenAdapter(Context mContext, ArrayList<Kitchen> list, int emptyString) {
        super(mContext);
        this.list = list;
        this.inflater = LayoutInflater.from(mContext);
        setEmptyString(emptyString);
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
            v = inflater.inflate(R.layout.item_kitchen_list, null);
            vh = new ViewHolder();
            vh.img_logo = (CircleImageView) v.findViewById(R.id.img_logo);
            vh.txt_name = (TextView) v.findViewById(R.id.txt_name);
            vh.txt_phone = (TextView) v.findViewById(R.id.txt_phone);
            vh.txt_address = (TextView) v.findViewById(R.id.txt_address);
            vh.txt_juli = (TextView) v.findViewById(R.id.txt_juli);
            vh.txt_xiaoliang = (TextView) v.findViewById(R.id.txt_xiaoliang);
            vh.ll_huodong_list = (LinearLayout) v.findViewById(R.id.ll_huodong_list);
            vh.lv_huodong = (ListView) v.findViewById(R.id.lv_huodong);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        ImageUtility.getInstance(mContext).showImage(list.get(position).getAlbum_thumbnail()[0], vh.img_logo,R.mipmap.ic_dianpu);
        vh.txt_name.setText(list.get(position).getTitle());
        vh.txt_phone.setText(list.get(position).getPhone());
        vh.txt_address.setText(list.get(position).getAddress());
        double distance;
        if (list.get(position).getDistance() != 0) {
            distance = list.get(position).getDistance() / 1000.00;
        } else {
            double[] kitchenLocation = list.get(position).getLocation();
            double myLocationLongitude =
                    Double.parseDouble(SharedPreferencesUtil.get(mContext, ProjectConstant.APP_START_LONGITUDE));
            double myLocationlatitude = Double.parseDouble(SharedPreferencesUtil.get(mContext, ProjectConstant.APP_START_LATITUDE));
//            distance = DistanceUtil.getDistance(new LatLng(kitchenLocation[1], kitchenLocation[0]),
//                    new LatLng(myLocationlatitude, myLocationLongitude)) / 1000.00;
            distance = CommonUtil.getDistance(kitchenLocation[1], kitchenLocation[0], myLocationlatitude, myLocationLongitude);
        }
        vh.txt_juli.setText(mContext.getString(R.string.juli, distance));
        if (list.get(position).getPromotions() != null) {
            vh.ll_huodong_list.setVisibility(View.VISIBLE);
            HuoDongAdapter adapter = new HuoDongAdapter(mContext, list.get(position).getPromotions());
            vh.lv_huodong.setAdapter(adapter);
        } else {
            vh.ll_huodong_list.setVisibility(View.GONE);
        }
        return v;
    }

    public void clear() {
        this.list.clear();
    }

    //    public void addAll(ArrayList<Kitchen> kitchens) {
//        this.list.addAll(kitchens);
//    }
    public void setData(ArrayList<Kitchen> kitchens) {
        this.list = kitchens;
    }

    class ViewHolder {
        CircleImageView img_logo;
        TextView txt_name;
        TextView txt_phone;
        TextView txt_address;
        TextView txt_juli;
        TextView txt_xiaoliang;
        LinearLayout ll_huodong_list;
        ListView lv_huodong;
    }

}
