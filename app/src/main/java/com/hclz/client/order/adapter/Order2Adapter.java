package com.hclz.client.order.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.bean.Order;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.Utility;
import com.hclz.client.shouye.ShouyeFragment;
import com.hclz.client.order.SelectPayMethodActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 16/1/22.
 */
public class Order2Adapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private ArrayList<Order> mDatas;
    private Activity mContext;
    private boolean isSetData;

    private OrderItemClickListener mListener;
    //为了防止复用item造成的混乱,建立的标记使用的集合
    private ArrayList<String> mSelectList = new ArrayList<>();

    public Order2Adapter(Activity context, OrderItemClickListener listener) {
        mContext = context;
        mListener = listener;
        mDatas = new ArrayList<>();
        isSetData = false;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || mDatas.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (VIEW_TYPE_EMPTY == viewType) {
            holder = new EmptyHolder(LayoutInflater.from(mContext).inflate(R.layout.empty_order, parent, false));
        } else {
            holder = new OrderViewHolder(LayoutInflater.from(
                    mContext).inflate(R.layout.item_order_list, parent,
                    false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (VIEW_TYPE_EMPTY == getItemViewType(position)) {
            return;
        }
        final OrderViewHolder vh = (OrderViewHolder) viewHolder;
        final Order order = mDatas.get(position);
        vh.tv_num.setText(order.orderid);
        vh.tv_status.setText(order.status_detail.get(order.status_detail.size() - 1).desc);
        vh.tv_ordertime.setText(order.ct);
        String source = "";
        if ("central".equals(order.waretype)) {
            source = "总仓";
        } else if ("dshop".equals(order.waretype)) {
            source = "城代";
        } else {
            source = "社区代理";
        }
        vh.tv_ordertype.setText(source + "配送");
        if (order.freight_amount <= 0) {
            vh.tv_total.setText("共" + order.products.size() + "件商品    合计" + CommonUtil.getMoney(order.payment_amount) + "元");
        } else {
            vh.tv_total.setText("共" + order.products.size() + "件商品    合计" + CommonUtil.getMoney(order.payment_amount) + "元(含运费" + CommonUtil.getMoney(order.freight_amount) + "元)");
        }
        String user_type = SharedPreferencesUtil.get(mContext, "user_type");
        if (order.status >= 10 && order.status < 20) {
            vh.tv_quxiao.setVisibility(View.VISIBLE);
        } else {
            vh.tv_quxiao.setVisibility(View.GONE);
        }

        if ("dshop".equals(user_type)) {
            if (order.status >= 10 && order.status< 20) {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_weizhifu);
                vh.ll_tuding.setBackgroundResource(R.color.light_red2);
            } else if (order.status >= 20 && order.status < 31) {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_yizhifu);
                vh.ll_tuding.setBackgroundResource(R.color.light_green);
            } else {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_yiwancheng);
                vh.ll_tuding.setBackgroundResource(R.color.light_yellow);
            }
        } else if ("buser".equals(user_type)||"euser".equals(user_type)) {

            if (order.status >= 10 && order.status < 20) {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_weizhifu);
                vh.ll_tuding.setBackgroundResource(R.color.light_red2);
            } else if (order.status >= 20 && order.status < 41) {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_yizhifu);
                vh.ll_tuding.setBackgroundResource(R.color.light_green);
            } else {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_yiwancheng);
                vh.ll_tuding.setBackgroundResource(R.color.light_yellow);
            }
        }else if ("cshop".equals(user_type)) {

            if (order.status >= 10 && order.status < 20) {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_weizhifu);
                vh.ll_tuding.setBackgroundResource(R.color.light_red2);
            } else if (order.status >= 20 && order.status < 41) {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_yizhifu);
                vh.ll_tuding.setBackgroundResource(R.color.light_green);
            } else {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_yiwancheng);
                vh.ll_tuding.setBackgroundResource(R.color.light_yellow);
            }
        } else {
            if (order.status >= 10 && order.status < 20) {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_weizhifu);
                vh.ll_tuding.setBackgroundResource(R.color.light_red2);
            } else if (order.status >= 20 && order.status < 59) {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_yizhifu);
                vh.ll_tuding.setBackgroundResource(R.color.light_green);
            } else {
                vh.iv_tuding.setImageResource(R.mipmap.ic_tuding_yiwancheng);
                vh.ll_tuding.setBackgroundResource(R.color.light_yellow);
            }
        }
        OrderDetailAdapter adapter = new OrderDetailAdapter(mContext, order.products);
        vh.lv_products.setAdapter(adapter);

        if (order.products.size() >= SanmiConfig.ORDER_MAX_SHOW_NUM) {
            vh.lv_products.setVisibility(View.GONE);
            mSelectList.add(vh.getLayoutPosition() + "");
        }

        if (order.operation != null){
            if (order.operation.size() == 1){
                vh.tv_quxiao.setVisibility(View.GONE);
                vh.tv_caozuo.setVisibility(View.VISIBLE);
                vh.tv_caozuo.setText(order.operation.get(0).display);
            } else if (order.operation.size() == 2) {
                vh.tv_quxiao.setVisibility(View.VISIBLE);
                vh.tv_caozuo.setVisibility(View.VISIBLE);
                vh.tv_quxiao.setText(order.operation.get(0).display);
                vh.tv_caozuo.setText(order.operation.get(1).display);
            }
        } else {
            vh.tv_quxiao.setVisibility(View.GONE);
            vh.tv_caozuo.setVisibility(View.GONE);
        }

        LogisticsAdapter logisticsAdapter = new LogisticsAdapter(mContext, (ArrayList<Order.StatusDetailBean>) order.status_detail);
        vh.lv_logistics.setAdapter(logisticsAdapter);
        vh.tv_delivery_address.setText(mContext.getString(R.string.delivery_address, order.address.addr_detail));
    }

    @Override
    public int getItemCount() {
        return mDatas.size() > 0 ? mDatas.size() : isSetData ? 1 : 0;//data为空时显示长度为1,为了显示EMPTY提示
    }

    public void setData(List<Order> datas) {
        mDatas = datas == null ? new ArrayList<Order>() : (ArrayList<Order>) datas;
        isSetData = true;
    }

    public interface OrderItemClickListener {
        void onOp(Order order, int position, String op , String display);
        void onItemSelected(Order order , int position);
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_num, tv_status, tv_ordertype, tv_total, tv_delivery_address, tv_ordertime;
        ListView lv_products, lv_logistics;
        TextView tv_wuliu, tv_caozuo, tv_quxiao;
        LinearLayout ll_logistics, ll_tuding;
        RelativeLayout ll_show_products;
        ImageView iv_phone, iv_show, iv_tuding;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_ordertime = (TextView) itemView.findViewById(R.id.tv_ordertime);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_ordertype = (TextView) itemView.findViewById(R.id.tv_ordertype);
            tv_total = (TextView) itemView.findViewById(R.id.tv_total);
            iv_show = (ImageView) itemView.findViewById(R.id.iv_show);
            tv_delivery_address = (TextView) itemView.findViewById(R.id.tv_delivery_address);
            lv_products = (ListView) itemView.findViewById(R.id.lv_products);
            lv_logistics = (ListView) itemView.findViewById(R.id.lv_logistics);
            tv_wuliu = (TextView) itemView.findViewById(R.id.tv_wuliu);
            tv_quxiao = (TextView) itemView.findViewById(R.id.tv_quxiao);
            tv_caozuo = (TextView) itemView.findViewById(R.id.tv_caozuo);
            ll_show_products = (RelativeLayout) itemView.findViewById(R.id.ll_show_products);
            ll_tuding = (LinearLayout) itemView.findViewById(R.id.ll_tuding);
            iv_tuding = (ImageView) itemView.findViewById(R.id.iv_tuding);
            ll_logistics = (LinearLayout) itemView.findViewById(R.id.ll_logistics);
            iv_phone = (ImageView) itemView.findViewById(R.id.iv_phone);
            tv_wuliu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ll_logistics.getVisibility() == View.VISIBLE) {
                        ll_logistics.setVisibility(View.GONE);
                    } else {
                        ll_logistics.setVisibility(View.VISIBLE);
                    }
                }
            });
            ll_show_products.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lv_products.getVisibility() == View.VISIBLE) {
                        lv_products.setVisibility(View.GONE);
                        iv_show.setImageResource(R.mipmap.list_down);
                        mSelectList.add(getLayoutPosition() + "");
                    } else {
                        mSelectList.remove(getLayoutPosition() + "");
                        lv_products.setVisibility(View.VISIBLE);
                        iv_show.setImageResource(R.mipmap.list_up);
                    }
                }
            });
            if (mSelectList.indexOf(getLayoutPosition() + "") > -1) {
                lv_products.setVisibility(View.GONE);
                iv_show.setImageResource(R.mipmap.list_down);
            } else {
                lv_products.setVisibility(View.VISIBLE);
                iv_show.setImageResource(R.mipmap.list_up);
            }
            iv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.makePhone(mContext, mDatas.get(getLayoutPosition()).service_phone);
                }
            });
            tv_quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String op = mDatas.get(getLayoutPosition()).operation.get(0).op;
                    if (!TextUtils.isEmpty(op)){
                        mListener.onOp(mDatas.get(getLayoutPosition()),getLayoutPosition(),op,tv_quxiao.getText().toString());
                    }
                }
            });
            tv_caozuo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String op = mDatas.get(getLayoutPosition()).operation.get(mDatas.get(getLayoutPosition()).operation.size() - 1).op;
                    if (!TextUtils.isEmpty(op)){
                        mListener.onOp(mDatas.get(getLayoutPosition()),getLayoutPosition(),op,tv_caozuo.getText().toString());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(mDatas.get(getLayoutPosition()),getLayoutPosition());
                }
            });
        }
    }

    class EmptyHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        TextView tv_empty;

        public EmptyHolder(View itemView) {
            super(itemView);
            tv_empty = (TextView) itemView.findViewById(R.id.tv_empty);
            tv_empty.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_empty.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_empty:
                    ShouyeFragment.startMeFromSelfActivity(mContext);
                    break;
            }
        }
    }

}
