package com.hclz.client.faxian.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements ProductSelectListener {
    @SuppressWarnings("unused")
    private static final String TAG = ProductAdapter.class.getSimpleName();

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

    public ProductAdapter(Activity context, LaidianErjiProductsSelectListener.CartChangeListener cartChangeListener, RecyclerView listView, StaggeredGridLayoutManager layoutManager) {
        super();

        mContext = context;
        mCartChangeListener = cartChangeListener;
        mProductsList = new ArrayList<>();
        mListView = listView;
        mLayoutManager = layoutManager;
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
                break;
            case UI_STYLE2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
                break;
            case UI_DEFAULT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
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
        if (item.getPrice_delta() <= 0) {
            holder.txt_delta.setVisibility(View.GONE);
        } else {
            holder.txt_delta.setVisibility(View.VISIBLE);
            holder.txt_delta.setText("(已优惠" + CommonUtil.getMoney(item.getPrice_delta()) + "元)");
        }
        if (item.getName_append() == null || "".equals(item.getName_append())) {
            holder.txt_name_append.setVisibility(View.GONE);
        } else {
            holder.txt_name_append.setVisibility(View.VISIBLE);
            holder.txt_name_append.setText(item.getName_append().trim());
        }
        holder.txt_price.setText("¥" + CommonUtil.getMoney(item.getPrice().get(3) - item.getPrice_delta()));
        int count = getProductCount(item);
        if (item.getInventory() <= count) {
            holder.iv_add.setImageResource(R.mipmap.btn_add);
            holder.iv_add.setClickable(false);
        } else {
            holder.iv_add.setImageResource(R.mipmap.btn_add_pre);
            holder.iv_add.setClickable(true);
        }
        if (count != 0) {
            holder.txt_account.setText(getProductCount(item) + "");
            holder.iv_del.setVisibility(View.VISIBLE);
        } else {
            holder.txt_account.setText("");
            holder.iv_del.setVisibility(View.GONE);
        }
        if (mProductsList.get(position).getInventory() <= count) {
            holder.iv_add.setImageResource(R.mipmap.btn_add);
            holder.iv_add.setClickable(false);
        } else {
            holder.iv_add.setImageResource(R.mipmap.btn_add_pre);
            holder.iv_add.setClickable(true);
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
            addNum = 1;
            int tmp = mProductsList.get(pos).getInventory();
            if (mProductsList.get(pos).getInventory() < addNum + oldItem.num) {
                ToastUtil.showToast(mContext, "已经没有更多的货，请先选购其它产品");
                return;
            }

        } else {
            addNum = 1;
            if (mProductsList.get(pos).getInventory() < addNum) {
                ToastUtil.showToast(mContext, "已经没有更多的货，请先选购其它产品");
                return;
            }
        }
        CartItem cartItem = new CartItem(mProductsList.get(pos).getPid(),
                mProductsList.get(pos).getCurrentType(),
                mProductsList.get(pos).getName(),
                mProductsList.get(pos).getPrice().get(3),
                mProductsList.get(pos).getPrice_delta(),
                addNum, mProductsList.get(pos).getInventory());

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
            if (product.getInventory() <= count) {
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
        return 0;
    }

    @Override
    public void onDel(View view, int position) {
        final int pos = mHeaderView == null ? position : position - 1;
        CartItem oldItem = Cart.getInstance().get(mProductsList.get(pos).getPid());
        int minusNum = 0;
        if (oldItem == null) {
            return;
        } else {
            minusNum = -1;
        }

        CartItem cartItem = new CartItem(mProductsList.get(pos).getPid(),
                mProductsList.get(pos).getCurrentType(),
                mProductsList.get(pos).getName(),
                mProductsList.get(pos).getPrice().get(3),
                mProductsList.get(pos).getPrice_delta(),
                minusNum, mProductsList.get(pos).getInventory());
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
        for (Product.Product2sEntity product2sEntity : list) {
//            Log.e("price_delta",product2sEntity.getPrice_delta()+"");
//            Log.e("name_append",product2sEntity.getName_append()+"");
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        TextView txt_name;
        TextView txt_price;
        TextView txt_delta, txt_name_append;

        ImageView img_logo;
        ImageView iv_add, iv_del;
        TextView txt_account;

        ProductSelectListener mProductSelectListener;

        public ProductViewHolder(View itemView, ProductSelectListener productSelectListener) {
            super(itemView);
            if (itemView == mFooterView || itemView == mHeaderView) return;
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_name_append = (TextView) itemView.findViewById(R.id.txt_name_append);
            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
            iv_del = (ImageView) itemView.findViewById(R.id.iv_del);
            txt_account = (TextView) itemView.findViewById(R.id.txt_account);
            iv_add.setOnClickListener(this);
            iv_del.setOnClickListener(this);
            img_logo.setOnClickListener(this);
            mProductSelectListener = productSelectListener;
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.iv_add:
                    mProductSelectListener.onAdd(v, getLayoutPosition());
                    break;
                case R.id.iv_del:
                    mProductSelectListener.onDel(v, getLayoutPosition());
                    break;
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
