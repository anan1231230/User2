package com.hclz.client.faxian.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.newcart.DiandiCartItem;

/**
 * Created by hjm on 16/9/26.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context mContext;
    private OnNumberChangeListener mOnNumberChangeListener;

    public CartAdapter(Context context, OnNumberChangeListener mOnNumberChangeListener) {
        mContext = context;
        this.mOnNumberChangeListener = mOnNumberChangeListener;
    }
    public interface OnNumberChangeListener {
        void numberChanger();
        void onEdited(View v,DiandiCartItem item,int position);
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cart_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position) {
        final DiandiCartItem mData = DiandiCart.getInstance().get().get(position);
        ImageUtility.getInstance(mContext).showImage(mData.imgUrl, holder.iv);
        holder.titleTv.setText(mData.name);
        holder.appendTv.setText(mData.nameAppand);
        holder.priceTv.setText("￥" + CommonUtil.getMoney(mData.price));
        holder.numTv.setText(mData.num + "");
        holder.numTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnNumberChangeListener.onEdited(holder.itemView,mData,position);
            }
        });
        holder.addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int previous_num = mData.num;
                if (mData.num < mData.minimal_quantity){
                    mData.num = mData.minimal_quantity;
                } else {
                    mData.num = mData.num + mData.minimal_plus;
                }

                if (mData.num > mData.inventory && mData.is_bookable == 0) {
                    mData.num = previous_num;
                    if (mData.num > mData.inventory){
                        DiandiCart.getInstance().remove(mData.pid,mContext);
                    }
                    ToastUtil.showToastCenter(mContext, "商品已被抢购一空,请选购其他商品");
                }
                notifyDataSetChanged();

                if (mOnNumberChangeListener != null) {
                    mOnNumberChangeListener.numberChanger();
                }
            }
        });
        holder.delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mData.num <= mData.minimal_quantity){
                    mData.num = 0;
                    DiandiCart.getInstance().remove(mData.pid,mContext);
                } else {
                    mData.num = mData.num - mData.minimal_plus;
                }
                notifyDataSetChanged();

                if (mOnNumberChangeListener != null) {
                    mOnNumberChangeListener.numberChanger();
                }
            }
        });
        String user_type = SharedPreferencesUtil.get(mContext, "user_type");
        if ("dshop".equals(user_type) && mData.mProduct.dshop_detail != null){
            holder.tv_cshop_note.setVisibility(View.VISIBLE);
            holder.tv_dshop_note.setVisibility(View.VISIBLE);
            holder.tv_cshop_note.setText("终端价格：¥" + CommonUtil.getMoney(mData.mProduct.dshop_detail.cshop_selling_price) + " , 合伙人盈利：" + mData.mProduct.dshop_detail.cshop_selling_profit + "%");
            holder.tv_dshop_note.setText("合伙人价格：¥" + CommonUtil.getMoney(mData.mProduct.dshop_detail.selling_price) + " , 您盈利：" + mData.mProduct.dshop_detail.selling_profit + "%");

        } else if ("cshop".equals(user_type) && mData.mProduct.cshop_detail != null){
            holder.tv_cshop_note.setVisibility(View.VISIBLE);
            holder.tv_dshop_note.setVisibility(View.GONE);
            holder.tv_cshop_note.setText("终端价格：¥" + CommonUtil.getMoney(mData.mProduct.cshop_detail.selling_price) + " , 您盈利：" + mData.mProduct.cshop_detail.selling_profit + "%");
        } else {
            holder.tv_cshop_note.setVisibility(View.GONE);
            holder.tv_dshop_note.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return DiandiCart.getInstance().get() == null ? 0 : DiandiCart.getInstance().get().size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView titleTv, appendTv, priceTv, numTv, delTv, addTv,tv_cshop_note,tv_dshop_note;

        public CartViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.cart_img_logo);
            titleTv = (TextView) itemView.findViewById(R.id.cart_txt_name);
            appendTv = (TextView) itemView.findViewById(R.id.cart_txt_name_append);
            priceTv = (TextView) itemView.findViewById(R.id.cart_txt_price);
            numTv = (TextView) itemView.findViewById(R.id.cart_num_tv);
            addTv = (TextView) itemView.findViewById(R.id.cart_add_tv);
            delTv = (TextView) itemView.findViewById(R.id.cart_del_tv);
            tv_cshop_note = (TextView) itemView.findViewById(R.id.tv_cshop_note);
            tv_dshop_note = (TextView) itemView.findViewById(R.id.tv_dshop_note);
        }
    }
}
