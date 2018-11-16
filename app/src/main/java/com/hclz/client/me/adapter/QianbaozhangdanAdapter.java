package com.hclz.client.me.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.bean.Qianbaozhangdan;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.me.listener.QianbaozhangdanCaozuoListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 16/1/22.
 */
public class QianbaozhangdanAdapter extends RecyclerView.Adapter {

    Activity mContext;
    List<Qianbaozhangdan> mDatas = new ArrayList<Qianbaozhangdan>();
    QianbaozhangdanCaozuoListener mListener;

    public QianbaozhangdanAdapter(Activity context, QianbaozhangdanCaozuoListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QianbaozhangdanViewHolder holder = new QianbaozhangdanViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_qianbaozhangdan, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Qianbaozhangdan item = mDatas.get(position);
        QianbaozhangdanViewHolder itemHolder = ((QianbaozhangdanViewHolder) holder);

        itemHolder.tv_zhangdanriqi.setText(item.getCt().substring(0, 10));

        //如果为充值钱包
        if (item.getBilltype() != null && item.getBilltype().equals("buycard") && item.getCardtype() != null && item.getCardtype().equals("balancecard")) {
//            itemHolder.iv_zhangdantype.setImageResource(R.drawable.icon_alipay);
            itemHolder.tv_zhangdantype.setText("钱包充值");
            itemHolder.tv_zhangdanjine.setText("充值" + CommonUtil.getMoney(item.getAmount()) + "吃货币");
            itemHolder.tv_price.setText("--支付¥" + CommonUtil.getMoney(item.getPrice()));
            if (item.getStatus() == 10) {
                itemHolder.tv_zhuangtai.setText("等待支付");
                itemHolder.tv_caozuo.setText("去支付");
                itemHolder.tv_caozuo.setVisibility(View.VISIBLE);
//                itemHolder.iv_zhangdantype.setImageResource(R.drawable.icon_alipay);
            } else {
                itemHolder.tv_zhuangtai.setText("交易成功");
                itemHolder.tv_caozuo.setVisibility(View.GONE);
//                itemHolder.iv_zhangdantype.setImageResource(R.drawable.icon_alipay);
            }
        }

        //如果为消费钱包
        if (item.getBilltype() != null && item.getBilltype().equals("order") && item.getZhifutype() != null && item.getZhifutype().equals("balance")) {
//            itemHolder.iv_zhangdantype.setImageResource(R.drawable.icon_alipay);
            itemHolder.tv_zhangdantype.setText("钱包消费");
            itemHolder.tv_zhangdanjine.setText("消费" + CommonUtil.getMoney(item.getZhifu_amount()) + "吃货币");
            itemHolder.tv_price.setText("");
            if (item.getStatus() == 10) {
                itemHolder.tv_zhuangtai.setText("等待支付");
                itemHolder.tv_caozuo.setText("去支付");
                itemHolder.tv_caozuo.setVisibility(View.VISIBLE);
            } else {
                itemHolder.tv_zhuangtai.setText("交易成功");
                itemHolder.tv_caozuo.setVisibility(View.GONE);
            }
        }

        //如果为买充值卡
        if (item.getBilltype() != null && item.getBilltype().equals("buycard") && item.getCardtype() != null && item.getCardtype().equals("prepaidcard")) {
//            itemHolder.iv_zhangdantype.setImageResource(R.drawable.icon_alipay);
            itemHolder.tv_zhangdantype.setText("充值卡购买");
            itemHolder.tv_zhangdanjine.setText("充值" + CommonUtil.getMoney(item.getZhifu_amount()) + "吃货币");
            itemHolder.tv_price.setText("--支付¥" + CommonUtil.getMoney(item.getPrice()));
            if (item.getStatus() == 10) {
//                itemHolder.iv_zhangdantype.setImageResource(R.drawable.icon_alipay);
                itemHolder.tv_zhuangtai.setText("等待支付");
                itemHolder.tv_caozuo.setText("去支付");
                itemHolder.tv_caozuo.setVisibility(View.VISIBLE);
            } else {
//                itemHolder.iv_zhangdantype.setImageResource(R.drawable.icon_alipay);
                itemHolder.tv_zhuangtai.setText("交易成功");
                itemHolder.tv_caozuo.setVisibility(View.GONE);
            }
        }

        //如果为消费充值卡
        if (item.getBilltype() != null && item.getBilltype().equals("order") && item.getZhifutype() != null && item.getZhifutype().equals("prepaidcard")) {
//            itemHolder.iv_zhangdantype.setImageResource(R.drawable.icon_alipay);
            itemHolder.tv_zhangdantype.setText("充值卡消费");
            itemHolder.tv_zhangdanjine.setText("消费" + CommonUtil.getMoney(item.getZhifu_amount()) + "吃货币");
            if (item.getStatus() == 10) {
                itemHolder.tv_zhuangtai.setText("等待支付");
                itemHolder.tv_caozuo.setText("去支付");
                itemHolder.tv_caozuo.setVisibility(View.VISIBLE);
            } else {
                itemHolder.tv_zhuangtai.setText("交易成功");
                itemHolder.tv_caozuo.setVisibility(View.GONE);
            }
        }

//        TextView tv = ((QianbaozhangdanViewHolder) holder).tvText;
//        tv.setTextColor(randomColor());
//        tv.setText(mDatas.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setData(List<Qianbaozhangdan> datas) {
        mDatas = datas == null ? new ArrayList<Qianbaozhangdan>() : datas;
    }

    class QianbaozhangdanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //        ImageView iv_zhangdantype;
        TextView tv_zhangdantype;
        TextView tv_zhangdanriqi;
        TextView tv_zhuangtai;
        TextView tv_zhangdanjine;
        TextView tv_caozuo;
        TextView tv_price;

        public QianbaozhangdanViewHolder(View itemView) {
            super(itemView);
//            iv_zhangdantype = (ImageView) itemView.findViewById(R.id.iv_zhangdantype);
            tv_zhangdantype = (TextView) itemView.findViewById(R.id.tv_zhangdantype);
            tv_zhangdanriqi = (TextView) itemView.findViewById(R.id.tv_zhangdanriqi);
            tv_zhuangtai = (TextView) itemView.findViewById(R.id.tv_zhuangtai);
            tv_zhangdanjine = (TextView) itemView.findViewById(R.id.tv_zhangdanjine);
            tv_caozuo = (TextView) itemView.findViewById(R.id.tv_caozuo);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_caozuo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_caozuo:
                    mListener.onDaxueSelected(mDatas.get(getLayoutPosition()));
                    break;
                default:
                    break;
            }
        }
    }
}
