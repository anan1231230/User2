package com.hclz.client.faxian.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.newcart.DiandiCartItem;

import java.util.ArrayList;

/**
 * Created by handsome on 2016/10/11.
 */

public class SubProductAdapter extends RecyclerView.Adapter<SubProductAdapter.JiechesijiHolder> {

    private Activity mContext;
    private SubproductListener mListener;
    private ArrayList<ProductBean.ProductsBean> mData;

    public SubProductAdapter(Activity context, SubproductListener listener) {
        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
    }

    public void setData(ArrayList<ProductBean.ProductsBean> data) {
        if (data != null) {
            mData = data;
        } else {
            mData = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public JiechesijiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JiechesijiHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sub_product, parent, false));
    }

    @Override
    public void onBindViewHolder(JiechesijiHolder holder, int position) {
        ProductBean.ProductsBean item = mData.get(position);
        String title = item.name == null ? "" : item.name;
        String subtitle = item.name_append == null ? "" : item.name_append;
        holder.title.setText(title + "(" + subtitle + ")");
        holder.tv_price.setText(CommonUtil.getMoney(item.price));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public interface SubproductListener {
        void onAddCart(int pos, ProductBean.ProductsBean item);
    }

    public class JiechesijiHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView tv_price;
        private ImageView iv_cart;

        public JiechesijiHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            iv_cart = (ImageView) itemView.findViewById(R.id.iv_cart);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add(mData.get(getLayoutPosition()));
                    mListener.onAddCart(getLayoutPosition(), mData.get(getLayoutPosition()));
                }
            });
        }

        private void add(ProductBean.ProductsBean item) {
            Integer num = 0;
            if (DiandiCart.getInstance().contains(item.pid)) {
                DiandiCartItem cartItem = DiandiCart.getInstance().get(item.pid);
                num = cartItem.num;
            }
            if (num < item.minimal_quantity) {
                num = item.minimal_quantity;
            } else {
                num = num + item.minimal_plus;
            }

            if (num > item.inventory && item.is_bookable == 0) {
                ToastUtil.showToastCenter(mContext, "商品已被抢购一空,请选购其他商品");
                return;
            }
            DiandiCartItem newItem = new DiandiCartItem(item, num);
            DiandiCart.getInstance().put(newItem, mContext);
            ToastUtil.showToastCenter(mContext, "已加入购物袋");
        }
    }
}
