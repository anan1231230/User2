package com.hclz.client.faxian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.cart.Cart;
import com.hclz.client.base.cart.CartItem;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.faxian.bean.Product;
import com.hclz.client.faxian.products.ProductIns;


public class CartsLaidianAdapter extends SanmiAdapter {
    private LayoutInflater inflater;
    private CartItemClickLisenter mCartChangeListener;
    private int mPriceNum;
    private CartItem item;

    public CartsLaidianAdapter(Context mContext, CartItemClickLisenter cartChangeListener, int priceNum) {
        super(mContext);
        this.inflater = LayoutInflater.from(mContext);
        this.mCartChangeListener = cartChangeListener;
        mPriceNum = priceNum;
    }

    @Override
    public boolean isEmpty() {
        if (Cart.getInstance().get() != null && Cart.getInstance().get().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getCount() {
        if (Cart.getInstance().get() != null) {
            return Cart.getInstance().get().size();
        } else {
            return VIEWTYPE_EMPTY;
        }
    }

    @Override
    public Object getItem(int position) {
        return Cart.getInstance().get() != null ? Cart.getInstance().get().get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return getEmptyView(parent);
        }
//        final CartItem item = CartLaidian.getInstance().get().get(position);
        item = Cart.getInstance().get().get(position);
        View v = convertView;
        ViewHolder vh = null;
        if (v == null || v.getTag(R.id.VIEWTYPE_NORMAL) == null) {
            v = inflater.inflate(R.layout.item_carts_productde_list, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) v.findViewById(R.id.tv_name);
            vh.tv_num = (TextView) v.findViewById(R.id.tv_num);
            vh.tv_price = (TextView) v.findViewById(R.id.tv_price);

            vh.tv_del = (TextView) v.findViewById(R.id.tv_del);
            vh.tv_add = (TextView) v.findViewById(R.id.tv_add);
            v.setTag(R.id.VIEWTYPE_NORMAL, vh);
        } else {
            vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
        }
        vh.tv_name.setText(item.name);
        vh.tv_num.setText(item.num + "");
        vh.tv_price.setText(CommonUtil.getMoney(item.price - item.pricedelta));
        vh.tv_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addLaidian(position);
                notifyDataSetChanged();
            }
        });
        vh.tv_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delLaidian(position);
                notifyDataSetChanged();
            }
        });
        if (item.inventory <= item.num) {
            vh.tv_add.setBackgroundResource(R.mipmap.btn_add);
            vh.tv_add.setClickable(false);
        } else {
            vh.tv_add.setBackgroundResource(R.mipmap.btn_add_pre);
            vh.tv_add.setClickable(true);
        }
        return v;
    }

    /**
     * 来点增加操作
     *
     * @param position
     */
    private void addLaidian(int position) {
        CartItem oldItem = Cart.getInstance().get(Cart.getInstance().get().get(position).pid);
        int addNum = 0;
        Product.Product2sEntity tmpProduct = null;
        String pid = Cart.getInstance().get().get(position).pid;
        for (Product.Product2sEntity product : ProductIns.getInstance().getmProduct().getProduct2s()) {
            if (pid.equals(product.getPid())) {
                tmpProduct = product;
                break;
            }
        }
        if (tmpProduct == null) {
            return;
        }
        if (oldItem != null) {
            if (mPriceNum == 1) {//城代
                addNum = tmpProduct.getMin_plus_amount().get(2);
            } else if (mPriceNum == 2) {//合伙人
                addNum = tmpProduct.getMin_plus_amount().get(1);
            } else {
                addNum = tmpProduct.getMin_plus_amount().get(0);
            }
            if (tmpProduct.getInventory(mPriceNum) < addNum + oldItem.num) {
                ToastUtil.showToast(mContext, "已经没有更多的货，请先选购其它产品");
                return;
            }

        } else {
            if (mPriceNum == 1) {//城代
                addNum = tmpProduct.getMin_purchase_amount().get(2);
            } else if (mPriceNum == 2) {//合伙人
                addNum = tmpProduct.getMin_purchase_amount().get(1);
            } else {
                addNum = tmpProduct.getMin_purchase_amount().get(0);
            }
            if (tmpProduct.getInventory(mPriceNum) < addNum) {
                ToastUtil.showToast(mContext, "已经没有更多的货，请先选购其它产品");
                return;
            }
        }

        CartItem cartItem = new CartItem(tmpProduct.getPid(),
                tmpProduct.getCurrentType(),
                tmpProduct.getName(),
                tmpProduct.getPrice().get(mPriceNum),
                tmpProduct.getPrice_delta(),
                addNum, tmpProduct.getInventory(mPriceNum));

        Cart.getInstance().put(cartItem,mContext);

        if (mCartChangeListener != null) {
            mCartChangeListener.addClick(tmpProduct.getPid());
        }
    }

    /**
     * 来点减少操作
     *
     * @param position
     */
    private void delLaidian(int position) {
        CartItem oldItem = Cart.getInstance().get(Cart.getInstance().get().get(position).pid);
        int minusNum = 0;
        Product.Product2sEntity tmpProduct = null;
        String pid = Cart.getInstance().get().get(position).pid;
        for (Product.Product2sEntity product : ProductIns.getInstance().getmProduct().getProduct2s()) {
            if (pid.equals(product.getPid())) {
                tmpProduct = product;
                break;
            }
        }
        if (tmpProduct == null) {
            return;
        }

        if (oldItem == null) {
            return;
        } else {
            if (mPriceNum == 1) {//城代
                if (oldItem.num <= tmpProduct.getMin_purchase_amount().get(2)) {
                    minusNum = -tmpProduct.getMin_purchase_amount().get(2);
                } else {
                    minusNum = -tmpProduct.getMin_plus_amount().get(2);
                }
            } else if (mPriceNum == 2) {//合伙人
                if (oldItem.num <= tmpProduct.getMin_purchase_amount().get(1)) {
                    minusNum = -tmpProduct.getMin_purchase_amount().get(1);
                } else {
                    minusNum = -tmpProduct.getMin_plus_amount().get(1);
                }
            } else {//普通
                if (oldItem.num <= tmpProduct.getMin_purchase_amount().get(0)) {
                    minusNum = -tmpProduct.getMin_purchase_amount().get(0);
                } else {
                    minusNum = -tmpProduct.getMin_plus_amount().get(0);
                }
            }
        }


        CartItem cartItem = new CartItem(tmpProduct.getPid(),
                tmpProduct.getCurrentType(),
                tmpProduct.getName(),
                tmpProduct.getPrice().get(mPriceNum),
                tmpProduct.getPrice_delta(),
                minusNum, tmpProduct.getInventory(mPriceNum));
        Cart.getInstance().put(cartItem,mContext);
        if (mCartChangeListener != null) {
            mCartChangeListener.delClick(tmpProduct.getPid());
        }
    }

    public void clear() {
        notifyDataSetChanged();
        mCartChangeListener.clear();
    }

    public interface CartItemClickLisenter {
        void addClick(String pid);

        void delClick(String pid);

        void clear();
    }

    class ViewHolder {
        TextView tv_name, tv_num, tv_price;
        TextView tv_del, tv_add;
    }
}
