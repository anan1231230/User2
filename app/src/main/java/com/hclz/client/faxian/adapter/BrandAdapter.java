package com.hclz.client.faxian.adapter;        /**
 * Created by handsome on 2016/12/19.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.bean.Order;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.shouye.adapter.*;
import com.hclz.client.shouye.adapter.ProductAdapter;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.newcart.DiandiCartItem;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private Context mContext;
    private BrandListener mListener;
    private ArrayList<ProductBean.ProductsBean> mData;
    private boolean isSetData;

    public BrandAdapter(Context context, BrandListener listener) {
        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
        isSetData = false;
    }

    public void setData(ArrayList<ProductBean.ProductsBean> data) {
        if (data != null) {
            mData = data;
            isSetData = true;
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null || mData.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (VIEW_TYPE_EMPTY == viewType) {
            holder = new BrandEmptyHolder(LayoutInflater.from(mContext).inflate(R.layout.empty_common_item, parent, false));
        } else {
            holder = new BrandViewHolder(LayoutInflater.from(
                    mContext).inflate(R.layout.item_brand, parent,
                    false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (VIEW_TYPE_EMPTY == getItemViewType(position)) {
            return;
        }
        BrandViewHolder viewHolder = (BrandViewHolder) holder;
        ProductBean.ProductsBean item = mData.get(position);
        viewHolder.tv_price.setText(CommonUtil.getMoney(item.price));
        viewHolder.tv_name.setText(item.name + " " + item.name_append);
        ImageUtility.getInstance(mContext).showImage(item.album_thumbnail, viewHolder.iv_img);

    }

    @Override
    public int getItemCount() {
        return mData.size() > 0 ? mData.size() : isSetData ? 1 : 0;//data为空时显示长度为1,为了显示EMPTY提示
    }


    public interface BrandListener {
        void onItemSelected(int position, ProductBean.ProductsBean item);

        void addCart(ProductBean.ProductsBean productsBean);
    }

    class BrandViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_img;
        private TextView tv_name, tv_price;
        private RelativeLayout rl_cart;

        public BrandViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            rl_cart = (RelativeLayout) view.findViewById(R.id.rl_cart);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(getLayoutPosition(), mData.get(getLayoutPosition()));
                }
            });
            rl_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductBean.ProductsBean item = mData.get(getRealPosition(BrandAdapter.BrandViewHolder.this));
                    if (item.virtual_goods == null) {
                        add(mData.get(getRealPosition(BrandAdapter.BrandViewHolder.this)));
                    } else {
                        if (item.virtual_goods.real_goods != null && item.virtual_goods.real_goods.size() > 0) {
                            for (ProductBean.ProductsBean bean : item.virtual_goods.real_goods) {
                                add(bean);
                            }
                        }
                    }
                    mListener.addCart(mData.get(getRealPosition(BrandAdapter.BrandViewHolder.this)));
                }
            });
        }

    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return position;
    }

    private void add(ProductBean.ProductsBean item) {
        Integer num = 0;
        if (DiandiCart.getInstance().contains(item.pid)) {
            DiandiCartItem cartItem = DiandiCart.getInstance().get(item.pid);
            num = cartItem.num;
        }
        int previous_num = num;
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


    class BrandEmptyHolder extends RecyclerView.ViewHolder {
        public BrandEmptyHolder(View itemView) {
            super(itemView);
        }
    }
}

