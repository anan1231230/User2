package com.hclz.client.kitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.bean.Coupon;

import java.util.List;

public class CouponAdapter extends SanmiAdapter {
    private LayoutInflater inflater;
    private List<Coupon> list;

    public CouponAdapter(Context mContext, List<Coupon> list, List<Coupon> usedCoupons) {
        super(mContext);
        this.list = list;
        if (usedCoupons != null && !usedCoupons.isEmpty()) {
            for (Coupon coupon : usedCoupons) {
                this.list.remove(coupon);
            }
        }
        this.inflater = LayoutInflater.from(mContext);
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
        Coupon coupon = list.get(position);
        View v = convertView;
        ViewHolder vh = null;
        if (v == null || v.getTag(R.id.VIEWTYPE_NORMAL) == null) {
            v = inflater.inflate(R.layout.item_coupon_list, null);
            vh = new ViewHolder();
            vh.ll_daijin = (LinearLayout) v.findViewById(R.id.ll_daijin);
            vh.tv_coupon_value = (TextView) v.findViewById(R.id.tv_coupon_value);

            vh.ll_dazhe = (LinearLayout) v.findViewById(R.id.ll_dazhe);
            vh.tv_coupon_rate = (TextView) v.findViewById(R.id.tv_coupon_rate);

            vh.ll_manjian = (LinearLayout) v.findViewById(R.id.ll_manjian);
            vh.tv_coupon_minpay = (TextView) v.findViewById(R.id.tv_coupon_minpay);
            vh.tv_coupon_cutpay = (TextView) v.findViewById(R.id.tv_coupon_cutpay);

            vh.tv_desc1 = (TextView) v.findViewById(R.id.tv_desc1);
            vh.tv_desc2 = (TextView) v.findViewById(R.id.tv_desc2);
            vh.tv_expire = (TextView) v.findViewById(R.id.tv_expire);

            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }

        if (coupon.getCoupon_type() == 1) {//代金券
            vh.ll_daijin.setVisibility(View.VISIBLE);
            vh.ll_dazhe.setVisibility(View.GONE);
            vh.ll_manjian.setVisibility(View.GONE);

            vh.tv_coupon_value.setText((coupon.getCoupon_value() * 0.01) + "");
        } else if (coupon.getCoupon_type() == 2) {//打折卡
            vh.ll_daijin.setVisibility(View.GONE);
            vh.ll_dazhe.setVisibility(View.VISIBLE);
            vh.ll_manjian.setVisibility(View.GONE);

            vh.tv_coupon_rate.setText((coupon.getCoupon_rate() * 0.1) + "");
        } else if (coupon.getCoupon_type() == 3) {//满减卡
            vh.ll_daijin.setVisibility(View.GONE);
            vh.ll_dazhe.setVisibility(View.GONE);
            vh.ll_manjian.setVisibility(View.VISIBLE);

            vh.tv_coupon_minpay.setText((coupon.getCoupon_minpay() * 0.01) + "");
            vh.tv_coupon_cutpay.setText((coupon.getCoupon_cutpay() * 0.01) + "");
        }
        vh.tv_desc1.setText(coupon.getDesc()[0]);
        vh.tv_desc2.setText(coupon.getDesc()[1]);
        vh.tv_expire.setText(coupon.getExpire_utcms() + "");
        return v;
    }

    class ViewHolder {
        LinearLayout ll_daijin;
        TextView tv_coupon_value;

        LinearLayout ll_dazhe;
        TextView tv_coupon_rate;

        LinearLayout ll_manjian;
        TextView tv_coupon_minpay, tv_coupon_cutpay;

        TextView tv_desc1, tv_desc2, tv_expire;
    }

}
