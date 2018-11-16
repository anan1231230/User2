package com.hclz.client.faxian.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.cart.CartItem;
import com.hclz.client.base.cart.Cart;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.faxian.bean.Product;
import com.hclz.client.faxian.listener.LaidianErjiProductsSelectListener;
import com.hclz.client.faxian.listener.ProductSelectListener;

import java.util.ArrayList;

public class SanjiProductAdapter extends RecyclerView.Adapter<SanjiProductAdapter.ProductViewHolder> implements ProductSelectListener {
    @SuppressWarnings("unused")
    private static final String TAG = SanjiProductAdapter.class.getSimpleName();

    private static final int ITEM_COUNT = 100;
    private static final int UI_STYLE1 = 1;
    private static final int UI_STYLE2 = 2;
    private static final int UI_DEFAULT = 3;
    private static final int UI_FOOTER = 4;
    private static final int UI_HEADER = 5;
    LaidianErjiProductsSelectListener.CartChangeListener mCartChangeListener;
    Activity mContext;
    private ArrayList<Product.Product2sEntity> mProductsList;
    private RecyclerView mListView;
    private StaggeredGridLayoutManager mLayoutManager;
    private View mHeaderView;
    private View mFooterView;
    private int mPriceNum = 4;//	[成本价0, 城代采购价1, 厨房采购价2, (在厨房吃)半成品价3,(带回家做)堂食价4]


    public SanjiProductAdapter(Activity context, LaidianErjiProductsSelectListener.CartChangeListener cartChangeListener, RecyclerView listView, StaggeredGridLayoutManager layoutManager) {
        super();

        mContext = context;
        mCartChangeListener = cartChangeListener;
        mProductsList = new ArrayList<>();
        mListView = listView;
        mLayoutManager = layoutManager;
    }

    public void setmPriceNum(int priceNum) {
        mPriceNum = priceNum;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(mProductsList.size() + 2);
    }

    @Override
    public int getItemViewType(int position) {
        //1.为最大 2.为第二大3.为普通4.为footerView 5.为headerview
        if (mFooterView != null && position >= (mProductsList.size() + (mHeaderView == null ? 0 : 1)))
            return UI_FOOTER;
        if (mHeaderView != null && position == 0) return UI_HEADER;
        int pos = mHeaderView == null ? position : position - 1;
        return UI_DEFAULT;
    }

    @Override
    public int getItemCount() {
        return mFooterView == null ? mProductsList.size() : mHeaderView == null ? mProductsList.size() + 1 : mProductsList.size() + 2;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (mFooterView != null && viewType == UI_FOOTER)
            return new ProductViewHolder(mFooterView, null);
        if (mHeaderView != null && viewType == UI_HEADER)
            return new ProductViewHolder(mHeaderView, null);
        switch (viewType) {
            case UI_STYLE1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list2, parent, false);
                break;
            case UI_STYLE2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list2, parent, false);
                break;
            case UI_DEFAULT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list2, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list2, parent, false);
                break;
        }
        return new ProductViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        if (getItemViewType(position) == UI_FOOTER || getItemViewType(position) == UI_HEADER) {
            final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
                sglp.setFullSpan(true);
            }
            return;
        }
        final int pos = getRealPosition(holder);
        final Product.Product2sEntity item = mProductsList.get(pos);
        setupViewStyle(holder, mProductsList.get(pos));
        setupViewHolderValues(holder, mProductsList.get(pos), pos);
    }

    private void setupViewStyle(ProductViewHolder holder, Product.Product2sEntity item) {
        final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
            sglp.setFullSpan(true);
            holder.itemView.setLayoutParams(sglp);
        }
    }

    private void setupViewHolderValues(ProductViewHolder holder, final Product.Product2sEntity item, final int position) {
        holder.txt_name.setText(item.getName());
        holder.txt_price.setText("¥" + CommonUtil.getMoney(item.getPrice().get(mPriceNum) - item.getPrice_delta()));
        holder.txt_price_prevoius.setText("¥" + CommonUtil.getMoney(item.getPrice_market()));

        if (mPriceNum == 1) {//城代
            holder.txt_price_prevoius.setVisibility(View.GONE);
            holder.txt_dshop2cshop.setVisibility(View.VISIBLE);
            holder.txt_cshop2user.setVisibility(View.VISIBLE);
            holder.txt_dshop2cshop.setText("合伙人价格:¥" + CommonUtil.getMoney(item.getPrice().get(mPriceNum + 1)) +
                    ",您盈利" + (item.getPrice().get(mPriceNum + 1) - item.getPrice().get(mPriceNum)) * 100 / (item.getPrice().get(mPriceNum) == 0 ? 1 : item.getPrice().get(mPriceNum)) + "%");
            holder.txt_cshop2user.setText("终端价格:¥" + CommonUtil.getMoney(item.getPrice().get(mPriceNum + 2)) +
                    ",合伙人盈利" + (item.getPrice().get(mPriceNum + 2) - item.getPrice().get(mPriceNum + 1)) * 100 / (item.getPrice().get(mPriceNum + 1) == 0 ? 1 : item.getPrice().get(mPriceNum + 1)) + "%");
            StringBuilder sb = new StringBuilder();
            sb.append(TextUtils.isEmpty(item.getName_append()) ? "" : item.getName_append());
            sb.append(" pid:");
            sb.append(item.getPid());
            holder.txt_name_append.setVisibility(View.VISIBLE);
            holder.txt_name_append.setText(sb.toString());
        } else if (mPriceNum == 2) {//合伙人
            holder.txt_price_prevoius.setVisibility(View.GONE);
            holder.txt_dshop2cshop.setVisibility(View.GONE);
            holder.txt_cshop2user.setVisibility(View.VISIBLE);
            holder.txt_cshop2user.setText("终端价格:¥" + CommonUtil.getMoney(item.getPrice().get(mPriceNum + 1)) +
                    ",您盈利" + (item.getPrice().get(mPriceNum + 1) - item.getPrice().get(mPriceNum)) * 100 / (item.getPrice().get(mPriceNum) == 0 ? 1 : item.getPrice().get(mPriceNum)) + "%");
            StringBuilder sb = new StringBuilder();
            sb.append(TextUtils.isEmpty(item.getName_append()) ? "" : item.getName_append());
            sb.append(" pid:");
            sb.append(item.getPid());
            holder.txt_name_append.setVisibility(View.VISIBLE);
            holder.txt_name_append.setText(sb.toString());
        } else {//普通用户
            if (item.getPrice_market() - item.getPrice().get(mPriceNum) + item.getPrice_delta() > 0){
                holder.txt_price_prevoius.setVisibility(View.VISIBLE);
            } else {
                holder.txt_price_prevoius.setVisibility(View.GONE);
            }
            holder.txt_dshop2cshop.setVisibility(View.GONE);
            holder.txt_cshop2user.setVisibility(View.GONE);
            if (TextUtils.isEmpty(item.getName_append())) {
                holder.txt_name_append.setVisibility(View.GONE);
            } else {
                holder.txt_name_append.setVisibility(View.VISIBLE);
                holder.txt_name_append.setText(item.getName_append());
            }
        }

        int count = getProductCount(item);
        if (item.getInventory(mPriceNum) <= count) {
            holder.iv_add.setImageResource(R.mipmap.btn_add);
            holder.iv_add.setClickable(false);
            holder.tv_add.setClickable(false);
        } else {
            holder.iv_add.setImageResource(R.mipmap.btn_add_pre);
            holder.iv_add.setClickable(true);
            holder.tv_add.setClickable(true);
        }
        if (count != 0) {
            holder.txt_account.setText(getProductCount(item) + "");
            holder.iv_del.setVisibility(View.VISIBLE);
            holder.tv_del.setClickable(true);
        } else {
            holder.txt_account.setText("");
            holder.iv_del.setVisibility(View.GONE);
            holder.tv_del.setClickable(false);
        }
        if (mProductsList.get(position).getInventory(mPriceNum) <= 0) {
            holder.rl_caozuo_panel.setVisibility(View.GONE);
            holder.iv_buhuozhong.setVisibility(View.VISIBLE);
        } else if (mProductsList.get(position).getInventory(mPriceNum) <= count) {
            holder.iv_buhuozhong.setVisibility(View.GONE);
            holder.rl_caozuo_panel.setVisibility(View.VISIBLE);
            holder.iv_add.setImageResource(R.mipmap.btn_add);
            holder.iv_add.setClickable(false);
            holder.tv_add.setClickable(false);
        } else {
            holder.iv_buhuozhong.setVisibility(View.GONE);
            holder.rl_caozuo_panel.setVisibility(View.VISIBLE);
            holder.iv_add.setImageResource(R.mipmap.btn_add_pre);
            holder.iv_add.setClickable(true);
            holder.tv_add.setClickable(true);
        }
        ImageUtility.getInstance(mContext).showImage(
                item.getAlbum_thumbnail().get(0), holder.img_logo);
    }

    private int getProductCount(Product.Product2sEntity item) {
        if (Cart.getInstance().contains(item.getPid())) {
            return Cart.getInstance().get(item.getPid()).num;
        } else {
            return 0;
        }
    }

    @Override
    public void onAdd(View view, final int position) {
        final int pos = mHeaderView == null ? position : position - 1;
        CartItem oldItem = Cart.getInstance().get(mProductsList.get(pos).getPid());
        int addNum = 0;
        if (oldItem != null) {
            if (mPriceNum == 1) {//城代
                addNum = mProductsList.get(position).getMin_plus_amount().get(2);
            } else if (mPriceNum == 2) {//合伙人
                addNum = mProductsList.get(position).getMin_plus_amount().get(1);
            } else {
                addNum = mProductsList.get(position).getMin_plus_amount().get(0);
            }
            if (mProductsList.get(position).getInventory(mPriceNum) < addNum + oldItem.num) {
                ToastUtil.showToast(mContext, "已经没有更多的货，请先选购其它产品");
                return;
            }

        } else {
            if (mPriceNum == 1) {//城代
                addNum = mProductsList.get(position).getMin_purchase_amount().get(2);
            } else if (mPriceNum == 2) {//合伙人
                addNum = mProductsList.get(position).getMin_purchase_amount().get(1);
            } else {
                addNum = mProductsList.get(position).getMin_purchase_amount().get(0);
            }
            if (mProductsList.get(position).getInventory(mPriceNum) < addNum) {
                ToastUtil.showToast(mContext, "已经没有更多的货，请先选购其它产品");
                return;
            }
        }
        CartItem cartItem = new CartItem(mProductsList.get(pos).getPid(),
                mProductsList.get(pos).getCurrentType(),
                mProductsList.get(pos).getName(),
                mProductsList.get(pos).getPrice().get(mPriceNum),
                mProductsList.get(pos).getPrice_delta(),
                addNum, mProductsList.get(pos).getInventory(mPriceNum));

        Cart.getInstance().put(cartItem,mContext);

        int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
        view.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
        if (mCartChangeListener != null) {
            mCartChangeListener.onAddtoCart(startLocation);
        }
        changeAccurateItem(cartItem.pid);
    }

    public void changeAccurateItem(String pid) {
        int position = getProductPosition(pid);
        if (position<0){
            return;
        }
        Product.Product2sEntity product = mProductsList.get(position);
        CartItem cartItem = Cart.getInstance().get(pid);
        if (mListView == null || mLayoutManager == null) {
            return;
        }
        // 获取点击的view
        int firstVisiblePosition = mLayoutManager.findFirstVisibleItemPositions(new int[2])[0];
        View view = mLayoutManager.getChildAt(position - firstVisiblePosition + (mHeaderView == null ? 0 : 1));
        if (view != null) {
            TextView txt_account = (TextView) view.findViewById(R.id.txt_account);
            ImageView iv_del = (ImageView) view.findViewById(R.id.iv_del);
            ImageView iv_add = (ImageView) view.findViewById(R.id.iv_add);
            // 获取mDataList.set(ids, item);更新的数据
            // 重新设置界面显示数据
            int count = cartItem == null ? 0 : cartItem.num;
            txt_account.setText(count + "");
            if (product.getInventory(mPriceNum) <= count) {
                iv_add.setImageResource(R.mipmap.btn_add);
                iv_add.setClickable(false);
            } else {
                iv_add.setImageResource(R.mipmap.btn_add_pre);
                iv_add.setClickable(true);
            }
            if (count != 0) {
                txt_account.setText(cartItem.num + "");
                iv_del.setVisibility(View.VISIBLE);
            } else {
                txt_account.setText("");
                iv_del.setVisibility(View.GONE);
            }
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    public int getProductPosition(String pid) {
        int position = 0;
        for (Product.Product2sEntity product : mProductsList) {
            if (product.getPid().equals(pid == null ? "" : pid)) {
                return position;
            }
            position += 1;
        }
        return -1;
    }

    @Override
    public void onDel(View view, int position) {
        final int pos = mHeaderView == null ? position : position - 1;
        CartItem oldItem = Cart.getInstance().get(mProductsList.get(pos).getPid());
        int minusNum = 0;
        if (oldItem == null) {
            return;
        } else {
            if (mPriceNum == 1) {//城代
                if (oldItem.num <= mProductsList.get(position).getMin_purchase_amount().get(2)) {
                    minusNum = -mProductsList.get(position).getMin_purchase_amount().get(2);
                } else {
                    minusNum = -mProductsList.get(position).getMin_plus_amount().get(2);
                }
            } else if (mPriceNum == 2) {//合伙人
                if (oldItem.num <= mProductsList.get(position).getMin_purchase_amount().get(1)) {
                    minusNum = -mProductsList.get(position).getMin_purchase_amount().get(1);
                } else {
                    minusNum = -mProductsList.get(position).getMin_plus_amount().get(1);
                }
            } else {//普通
                if (oldItem.num <= mProductsList.get(position).getMin_purchase_amount().get(0)) {
                    minusNum = -mProductsList.get(position).getMin_purchase_amount().get(0);
                } else {
                    minusNum = -mProductsList.get(position).getMin_plus_amount().get(0);
                }
            }
        }
        CartItem cartItem = new CartItem(mProductsList.get(pos).getPid(),
                mProductsList.get(pos).getCurrentType(),
                mProductsList.get(pos).getName(),
                mProductsList.get(pos).getPrice().get(mPriceNum),
                mProductsList.get(pos).getPrice_delta(),
                minusNum, mProductsList.get(pos).getInventory(mPriceNum));
        Cart.getInstance().put(cartItem,mContext);
        int[] endLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
        view.getLocationInWindow(endLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
        if (mCartChangeListener != null) {
            mCartChangeListener.onDelfromCart(endLocation);
        }
        changeAccurateItem(cartItem.pid);
    }

    public ArrayList getData() {
        ArrayList<Product.Product2sEntity> tmp = new ArrayList<>();
        return mProductsList == null ? tmp : mProductsList;
    }

    public void setData(ArrayList<Product.Product2sEntity> list) {
        this.mProductsList = list == null ? new ArrayList<Product.Product2sEntity>() : list;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        TextView txt_name;
        TextView txt_price;
        TextView txt_price_prevoius;
        TextView txt_name_append;
        TextView txt_dshop2cshop;
        TextView txt_cshop2user;
        ImageView img_logo;
        ImageView iv_add, iv_del;
        TextView txt_account;
        ImageView iv_buhuozhong;
        RelativeLayout rl_caozuo_panel;
        LinearLayout ll_product;
        TextView tv_add, tv_del;

        ProductSelectListener mProductSelectListener;

        public ProductViewHolder(View itemView, ProductSelectListener productSelectListener) {
            super(itemView);
            if (itemView == mFooterView || itemView == mHeaderView) return;
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_price_prevoius = (TextView) itemView.findViewById(R.id.txt_price_previous);
            txt_price_prevoius.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txt_name_append = (TextView) itemView.findViewById(R.id.txt_name_append);
            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            txt_dshop2cshop = (TextView) itemView.findViewById(R.id.txt_dshop2cshop);
            txt_cshop2user = (TextView) itemView.findViewById(R.id.txt_cshop2user);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
            iv_del = (ImageView) itemView.findViewById(R.id.iv_del);
            rl_caozuo_panel = (RelativeLayout) itemView.findViewById(R.id.rl_caozuo_panel);
            txt_account = (TextView) itemView.findViewById(R.id.txt_account);
            iv_buhuozhong = (ImageView) itemView.findViewById(R.id.iv_buhuozhong);
            tv_add = (TextView) itemView.findViewById(R.id.tv_add);
            tv_del = (TextView) itemView.findViewById(R.id.tv_del);
            ll_product = (LinearLayout) itemView.findViewById(R.id.ll_product);

            tv_add.setOnClickListener(this);
            tv_del.setOnClickListener(this);
            ll_product.setOnClickListener(this);

            iv_add.setOnClickListener(this);
            iv_del.setOnClickListener(this);
            img_logo.setOnClickListener(this);
            mProductSelectListener = productSelectListener;
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.tv_add:
                case R.id.iv_add:
                    mProductSelectListener.onAdd(iv_add, getLayoutPosition());
                    break;
                case R.id.tv_del:
                case R.id.iv_del:
                    mProductSelectListener.onDel(iv_del, getLayoutPosition());
                    break;
                case R.id.ll_product:
                case R.id.img_logo:
//                    ProductDetailActivity.startMe(mContext,mProductsList.get(getRealPosition(this)));
                    break;
                default:
                    //do nothing
                    break;
            }
        }
    }
}
