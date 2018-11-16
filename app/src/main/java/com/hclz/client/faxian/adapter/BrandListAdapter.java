package com.hclz.client.faxian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.faxian.BrandDetailActivity;
import com.hclz.client.faxian.bean.Brand;

import java.util.ArrayList;


public class BrandListAdapter extends SanmiAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Brand> list;
    private Context context;

    public BrandListAdapter(Context mContext, ArrayList<Brand> list) {
        super(mContext);
        this.context = mContext;
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
        if (list != null) {
            if (list.size() > 0) {
                return list.size();
            } else {
                return VIEWTYPE_EMPTY;
            }
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
        final Brand brand = list.get(position);
        if (v == null || v.getTag(R.id.VIEWTYPE_NORMAL) == null) {
            v = mInflater.inflate(R.layout.item_brand_list, null);
            vh = new ViewHolder();
            vh.iv_brand_image = (ImageView) v.findViewById(R.id.iv_brand_image);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        ImageUtility.getInstance(context).showImage(list.get(position).getCover_img(), vh.iv_brand_image);


        vh.iv_brand_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandDetailActivity.startMe(mContext, brand);
            }
        });
        return v;
    }

    class ViewHolder {
        ImageView iv_brand_image;

    }


}
