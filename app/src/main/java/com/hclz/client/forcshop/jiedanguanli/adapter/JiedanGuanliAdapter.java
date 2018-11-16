package com.hclz.client.forcshop.jiedanguanli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.CommonUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by handsome on 16/7/7.
 */
public class JiedanGuanliAdapter extends RecyclerView.Adapter<JiedanGuanliAdapter.JiedanItemHolder> {
    Context mContext;
    ArrayList<JiedanItem> mData;
    JiedanListener mListener;
    String mFrom;

    public JiedanGuanliAdapter(Context context, JiedanListener listener) {
        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
    }
    public JiedanGuanliAdapter(Context context, JiedanListener listener, String from) {
        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
        mFrom = from;
    }

    public void setData(ArrayList<JiedanItem> datas) {
        if (datas == null || datas.size() <= 0) {
            mData = new ArrayList<>();
        } else {
            mData = datas;
        }
    }

    @Override
    public JiedanItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        JiedanItemHolder holder = new JiedanItemHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_jiedan, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(JiedanItemHolder holder, int position) {
        JiedanItem item = mData.get(position);
        holder.tv_orderid.setText(item.getOrderId());
        holder.tv_price.setText("¥"+CommonUtil.getMoney(item.getPrice()));
        holder.tv_address.setText(item.getAddress());
        holder.tv_phone.setText(item.getPhone());
        if ("finished".equals(mFrom)){
            holder.tv_yidengdai.setText("下单时间:");
            holder.tv_wait.setText(item.getCt());
            holder.tv_wait.setTextColor(mContext.getResources().getColor(R.color.half_black));
            holder.tv_jiedan.setVisibility(View.GONE);
            holder.tv_quehuo.setVisibility(View.GONE);
            return;
        }
        holder.tv_yidengdai.setText("已等待:");
        holder.tv_wait.setText(item.getYidengdai());
        holder.tv_wait.setTextColor(mContext.getResources().getColor(R.color.red));
        if ("chengdaidongxiang".equals(mFrom)){
            switch (item.getStatus()){
                case 20:
                case 21:
                    holder.tv_jiedan.setText("城代尚未确认");
                    holder.tv_quehuo.setVisibility(View.GONE);
                    break;
                case 40:
                    holder.tv_jiedan.setText("城代已确认");
                    holder.tv_quehuo.setVisibility(View.GONE);
                    break;
                default:
                    holder.tv_jiedan.setText("未知");
                    holder.tv_quehuo.setVisibility(View.GONE);
                    break;
            }
        } else {
            switch (item.getStatus()){
                case 20:
                case 21:
                    holder.tv_jiedan.setText("接单");
                    holder.tv_quehuo.setVisibility(View.VISIBLE);
                    break;
                case 41:
                case 49:
                    holder.tv_jiedan.setText("接货");
                    holder.tv_quehuo.setVisibility(View.GONE);
                    break;
                case 50:
                    holder.tv_jiedan.setText("送货");
                    holder.tv_quehuo.setVisibility(View.GONE);
                    break;
                case 51:
                    holder.tv_jiedan.setText("送达");
                    holder.tv_quehuo.setVisibility(View.GONE);
                    break;
                default:
                    holder.tv_jiedan.setText("未知");
                    holder.tv_quehuo.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static interface JiedanListener {
        public void onItemSelected(JiedanItem item);
        public void onJiedan(JiedanItem item);
        public void onJieHuo(JiedanItem item);
        public void onQuehuo(JiedanItem item);
        public void onSonghuo(JiedanItem item);
        public void onSongda(JiedanItem item);
    }

    public static interface JiedanItem extends Serializable {

        public String getOrderId();

        public String getAddress();

        public String getPhone();

        public String getYidengdai();

        public int getPrice();

        public int getStatus();

        String getCt();
    }

    public class JiedanItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout ll_dingdan;
        TextView tv_orderid,tv_price,tv_address,tv_phone,tv_yidengdai,tv_wait,tv_jiedan,tv_quehuo;

        public JiedanItemHolder(View itemView) {
            super(itemView);

            ll_dingdan = (LinearLayout) itemView.findViewById(R.id.ll_dingdan);
            tv_orderid = (TextView) itemView.findViewById(R.id.tv_orderid);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_yidengdai = (TextView) itemView.findViewById(R.id.tv_yidengdai);
            tv_wait = (TextView) itemView.findViewById(R.id.tv_wait);
            tv_jiedan = (TextView) itemView.findViewById(R.id.tv_jiedan);
            tv_quehuo = (TextView) itemView.findViewById(R.id.tv_quehuo);
            ll_dingdan.setOnClickListener(this);
            tv_jiedan.setOnClickListener(this);
            tv_quehuo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_dingdan:
                    mListener.onItemSelected(mData.get(getLayoutPosition()));
                    break;
                case R.id.tv_jiedan:
                    if ("接单".equals(tv_jiedan.getText().toString().trim())){
                        mListener.onJiedan(mData.get(getLayoutPosition()));
                    } else if ("接货".equals(tv_jiedan.getText().toString().trim())){
                        mListener.onJieHuo(mData.get(getLayoutPosition()));
                    } else if ("送货".equals(tv_jiedan.getText().toString().trim())){
                        mListener.onSonghuo(mData.get(getLayoutPosition()));
                    } else if ("送达".equals(tv_jiedan.getText().toString().trim())){
                        mListener.onSongda(mData.get(getLayoutPosition()));
                    }
                    break;
                case R.id.tv_quehuo:
                        mListener.onQuehuo(mData.get(getLayoutPosition()));
                    break;
                default:
                    break;
            }
        }
    }
}
